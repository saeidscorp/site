(ns site.db.entities
  (:require [hara.time :as t])
  (:use [korma.core])
  (:import (java.util UUID)))

(declare user author tag post media comment)

(defentity user
           (entity-fields :id :first_name :last_name :email
                          :last_login :is_active :role :profile_picture
                          :pass :activation_id :uuid)
           (has-many comment {:fk :writer})
           (has-one author))

(defentity author
           (pk :user_id)
           (belongs-to user)
           (has-many post {:fk :author})
           (has-many media {:fk :owner}))

(defentity tag
           (many-to-many post :post_tag)
           (entity-fields :id :name :desc))

(defentity post
           (belongs-to author {:fk :author})
           (belongs-to media {:fk :featured_image})
           (entity-fields :id :url_title :title :author :date_time :short :featured_image :content)
           (has-many comment {:fk :target})
           (many-to-many tag :post_tag))

(defentity media
           (has-one author {:fk :owner})
           (entity-fields :path :mime))

(defentity comment
           (belongs-to user {:fk :writer})
           (belongs-to post {:fk :target})
           (belongs-to comment {:fk :reply_to})
           (entity-fields :id :writer :date_time :is_reply :text))

;; user functions:

(defn get-all-users [& [where-email-like]]
  (select user (where {:email [like (str "%" where-email-like "%")]})
          (order :email :asc)))

(defn get-user-by-email [email] (first (select user (where {:email email}) (limit 1))))
(defn get-user-by-act-id [id] (first (select user (where {:activation_id id}) (limit 1))))
(defn get-user-by-uuid [uuid] (first (select user (where {:uuid uuid}) (limit 1))))

(defn username-exists? [email] (some? (get-user-by-email email)))

(defn create-user [email pw_crypted activationid & [is-active?]]
  (insert user (values {:email email :pass pw_crypted :activation_id activationid :is_active (or is-active? false)
                        :uuid  (str (UUID/randomUUID))})))

(defn set-user-active [activationid & [active]]
  (update user (set-fields {:is_active (or active true)}) (where {:activation_id activationid})))

(defn update-user [uuid fields] (update user (set-fields fields) (where {:uuid uuid})))
(defn delete-user [uuid] (delete user (where {:uuid uuid})))
(defn change-password [email pw] (update user (set-fields {:pass pw}) (where {:email email})))


;; media functions:

(defn get-media-by-id [id]
  (first (select media (where {:id id}))))

(defn create-media [path & [mime owner]]
  (insert media (values {:path  path
                         :mime  mime
                         :owner (or owner 1)})))

;; author functions:

(defn author-to-map [author]
  (as-> (first author) _
        (assoc _ :profile_picture
                 (:path (get-media-by-id (:profile_picture _))))))

(defn is-author? [user]
  (when (seq (select author (where {:user_id (:id user)}))) true))

;; comment functions:

(defn get-author-by-id [id]
  (->> (select user (where {:id id}))
       (author-to-map)))

(defn comment-to-map [com]
  (as-> com _
        (assoc _ :writer (get-author-by-id (:writer _)))))

(defn comments-for [post n]
  (->> (select comment (where {:target (:id post), :is_reply false})
               (order :date_time :DESC)
               (limit n))
       (map comment-to-map)))

(defn replies-to [com n]
  (->> (select comment (where {:is_reply true, :reply_to (:id com)})
               (order :date_time :DESC)
               (limit n))
       (map comment-to-map)))

;; post functions:

(defn all-comments-count [post]
  (-> (select :comment (aggregate (count :*) :replies-count)
              (where {:target (:id post)}))
      (first)
      (:replies-count)))

(defn nested-comments
  ([post n max-depth] (mapv #(nested-comments % n max-depth 0)
                            (comments-for post n)))
  ([this-comment n max-depth curr-depth]
   (if (= curr-depth max-depth)
     this-comment
     (assoc this-comment :replies (mapv #(nested-comments % n max-depth (inc curr-depth))
                                        (replies-to this-comment n))))))

(defmacro assoc-if [test form key val & kvs]
  `(if ~test (apply assoc ~form ~key ~val ~kvs) ~form))

(defn post-to-map [post & {{replies-num :num replies-depth :depth,
                            :as         replies} :replies,
                           :keys                 [category replies-count
                                                  author featured-image date-time]
                           :or                   {author         true
                                                  replies-count  true
                                                  featured-image true
                                                  date-time      true
                                                  category       "Programming"}}]
  (as-> post _
        (assoc-if author _ :author (get-author-by-id (:author _)))
        (assoc-if featured-image _ :image-src (:path (first (select media (where {:id (:featured_image _)})))))
        (assoc-if replies _ :replies (nested-comments _ replies-num replies-depth))
        (assoc-if replies-count _ :replies-count (all-comments-count _))
        ;; FIXME: I probably fail on a Postgres backend
        (assoc-if date-time _ :date_time (let [t (:date_time _)] (if (string? t) (t/parse t "yyy-MM-dd HH:mm:ss")
                                                                                 (t/coerce t {:type org.joda.time.DateTime}))))
        (assoc-if category _ :category category)))

(defn get-post-by-id [id]
  (->> (first (select post (with tag) (where {:id id})))
       (post-to-map)))

(defn get-post-by-title [title]
  (-> (first (select post (with tag) (where {:url_title title})))
      (post-to-map :replies {:num 5 :depth 5})))

(defn get-latest-posts
  ([n] (map post-to-map (select post (with tag) (order :date_time :DESC) (limit n))))
  ([] (get-latest-posts 10)))

(defn get-posts-range [start count]
  (map post-to-map (select post (with tag) (order :date_time :DESC) (limit count) (offset start))))

(defn get-latest-post []
  (first (get-latest-posts 1)))

(defn all-posts-count []
  (-> (select :post (aggregate (count :*) :cnt))
      (first)
      (:cnt)))

(defn create-post [title short-title short-content md-content author]
  (insert post (values {:title     title,
                        :short     short-content,
                        :url_title short-title,
                        :content   md-content,
                        :author    author})))

(defn update-post [id fields]
  (update post (set-fields fields) (where {:id id})))

(defn delete-post-by-id [id]
  (delete post (where {:id id})))
(ns site.db.entities
  (:require [site.utils :refer [->>>]])
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

;; author functions:

(defn author-to-map [author]
  (->>> (first author)
        (assoc _ :profile_picture
                 (get-media-by-id (:profile_picture _)))))

(defn get-author-by-id [id]
  (->> (select user (where {:id id}))
       (author-to-map)))

;; comment functions:

(defn comment-to-map [com]
  (->>> com
        (assoc _ :writer (get-author-by-id (:writer _)))))

(defn comments-for [post n]
  (->> (select comment (where {:target (:id post), :is_reply 0})
               (order :date_time :DESC)
               (limit n))
       (map comment-to-map)))

(defn replies-to [com n]
  (->> (select comment (where {:is_reply 1, :reply_to (:id com)})
               (order :date_time :DESC)
               (limit n))
       (map comment-to-map)))

(defn nested-comments
  ([post n max-depth] (mapv #(nested-comments % n max-depth 0)
                            (comments-for post n)))
  ([this-comment n max-depth curr-depth]
   (if (= curr-depth max-depth)
     this-comment
     (assoc this-comment :replies (mapv #(nested-comments % n max-depth (inc curr-depth))
                                        (replies-to this-comment n))))))

;; post functions:

(defn post-to-map [post]
  (->>> post
        (assoc _ :author (get-author-by-id (:author _)))
        (assoc _ :image-src (:path (first (select media (where {:id (:featured_image _)})))))
        (assoc _ :replies (nested-comments _ 5 5))))

(defn get-post-by-id [id]
  (->> (first (select post (with tag) (where {:id id})))
       (post-to-map)))

(defn get-post-by-title [title]
  (->> (first (select post (with tag) (where {:url_title title})))
       (post-to-map)))

(defn get-latest-posts
  ([n] (map post-to-map (select post (with tag) (order :date_time :DESC) (limit n))))
  ([] (get-latest-posts 10)))

(defn get-latest-post []
  (first (get-latest-posts 1)))

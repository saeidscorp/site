(ns site.db.entities
  (:require [site.utils :refer [->>>]])
  (:use [korma.core])
  (:import (java.util UUID)))

(declare user author tag post media comment)

(defentity user
           (entity-fields :id :first_name :last_name :email
                          :last_login :is_active :role
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
           (entity-fields :id :title :author :date_time :short :featured_image :content)
           (has-many comment {:fk :writer})
           (many-to-many tag :post_tag))

(defentity media
           (has-one author {:fk :owner})
           (entity-fields :path :mime))

(defentity comment
           (belongs-to user {:fk :writer})
           (belongs-to post {:fk :target})
           (belongs-to comment {:fk :reply_to})
           (entity-fields :writer :date_time :is_reply :text))

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



;; post functions:

(defn get-latest-posts
  ([n] (select post (order :date_time :DESC) (limit n)))
  ([] (get-latest-posts 10)))

(defn get-latest-post []
  (first (get-latest-posts 1)))

(defn post-to-map [post]
  (->>> post
        (assoc _ :author
               (let [a (into {} (filter #((set (:fields user)) (first %)) post))]
                 (assoc a :name (:first_name a))))
        (assoc _ :image-src (:path _))))

(defn get-post-by-id [id]
  (->>> (first (select post (where {:id id})))
        (assoc _ :author
                 (first (select user (where {:id (:author _)}))))
        (assoc _ :image-src (:path (first (select media (where {:id (:featured_image _)})))))))
  ;(first (select post (with author (with user)) (with media) (where {:id id}))))

;;
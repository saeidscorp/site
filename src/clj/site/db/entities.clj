(ns site.db.entities
  (:use [korma.core])
  (:import (java.util UUID)))

(declare user author tag post media comment)

(defentity user
           (entity-fields :first_name :last_name :email
                          :role :last_login :is_active
                          :pass :activationid :uuid)
           (has-many comment))

(defentity author
           (belongs-to user)
           (has-many post))

(defentity tag
           (many-to-many post :post_tag)
           (entity-fields :name :desc))

(defentity post
           (has-one author)
           (entity-fields :title :date_time :content)
           (many-to-many tag :post_tags))

(defentity media
           (entity-fields :path :mime))

(defentity comment
           (belongs-to user)
           (entity-fields :writer :date_time :text))

;; user functions:

(defn get-all-users [ & [where-email-like]]
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

(defn get-latest-posts [n]
  (select post (order :date :DESC) (limit (if (zero? n) 10 n))))

(defn get-latest-post []
  (first (select post (order :date :DESC) (limit 1))))

;;
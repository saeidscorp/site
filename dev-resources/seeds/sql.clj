(ns seeds.sql
  (:require [clojure.java.jdbc :as j]
            [clojure.tools.reader.edn :as edn]
            [clojure.tools.reader.reader-types :as t]
            [clojure.java.io :as io]))

(defn insert-seeds [entity db]
  (doseq [row (edn/read (-> (str "dev/seeds/" (name entity) "s.edn")
                            (io/resource)
                            (io/input-stream)
                            (t/input-stream-push-back-reader)))]
    (j/insert! db entity row)))

(defn run [target & _]
  (j/with-db-connection [db {:connection-uri (-> target :db :url)}]
    (doseq [entity [:user :author :media :post :tag :comment :post_tag]]
      (j/delete! db :sqlite_sequence [1])
      (j/delete! db entity [1])
      (insert-seeds entity db))))
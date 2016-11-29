(ns site.components.db
  (:require [com.stuartsierra.component :as component]
            [korma.db :refer [defdb]]))

(defrecord Db [config]
  component/Lifecycle
  (start [component]
    (let [db-spec (get-in config [:config :database-spec])]
      (defdb db db-spec))
    component)
  (stop [component] component))

(defn new-db []
  (map->Db {}))

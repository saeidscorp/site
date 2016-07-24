(ns site.components.templating
  (:require [com.stuartsierra.component :as component]))

(defrecord Templating [handler config]
  component/Lifecycle
  (start [component]
    (alter-var-root #'site.layout/routes (constantly (:routes handler)))
    component)
  (stop [component]
    (alter-var-root #'site.layout/routes (constantly nil))
    component))

(defn new-templating []
  (map->Templating {}))
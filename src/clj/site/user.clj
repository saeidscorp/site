(ns site.user
  (:require [system.repl :refer [reset stop]]
            [site.components.components :refer [dev-system]]))

(defn start-dev-system []
  (system.repl/go))

(defn go []
  (when site.components.server/server-instance
    (.stop site.components.server/server-instance)
    (alter-var-root #'site.components.server/server-instance (constantly nil)))
  (system.repl/go))

(system.repl/set-init! #'dev-system)

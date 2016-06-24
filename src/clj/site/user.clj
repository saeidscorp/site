(ns site.user
  (:require [system.repl :refer [go reset stop]]
            [site.components.components :refer [dev-system]]))

(defn start-dev-system []
  (go))

(system.repl/set-init! #'dev-system)

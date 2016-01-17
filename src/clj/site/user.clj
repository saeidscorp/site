(ns site.user
  (:require [reloaded.repl :refer [go reset stop]]
            [site.components.components :refer [dev-system]]))

(defn start-dev-system []
  (go))

(reloaded.repl/set-init! dev-system)

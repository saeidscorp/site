(ns site.user
  (:require [system.repl :refer [reset stop]]
            [com.stuartsierra.component]
            [site.components.components :refer [dev-system]]
            [site.migrations :as m]
            [leiningen.sass :refer [sass]]
            [leiningen.core.project :as prj]

            [taoensso.timbre :as timbre]))

(defn start-dev-system []
  (system.repl/go))

(defn go []
  (when site.components.server/server-instance
    (com.stuartsierra.component/stop site.components.server/server-instance)
    (alter-var-root #'site.components.server/server-instance (constantly nil)))
  (system.repl/go))

(system.repl/set-init! #'dev-system)

(timbre/info "reading project.clj file for sass tasks")

(def ^:dynamic *project*
  (try (prj/read "project.clj")
       (catch Exception _)))

(defn watch []
  (when *project*
    (future (sass *project* "watch"))))

(defn oneshot []
  (when *project*
    (sass *project* "once")))
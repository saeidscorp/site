(ns site.components.components
  (:require
    [com.stuartsierra.component :as component]
    (system.components
      [repl-server :refer [new-repl-server]])
    [site.components.server :refer [new-web-server new-web-server-prod]]
    [site.components.handler :refer [new-handler]]
    [site.components.templating :refer [new-templating]]
    [site.components.config :as c]
    [site.components.db :refer [new-db]]
    [site.components.locale :as l]))


(defn dev-system []
  (component/system-map
    :locale (l/new-locale)
    :config (c/new-config (c/prod-conf-or-dev))
    :db (component/using (new-db) [:config])
    :handler (component/using (new-handler) [:config :locale])
    :templating (component/using (new-templating) [:handler :config])
    :web (component/using (new-web-server) [:handler :templating :config])))


(defn prod-system []
  (component/system-map
    :locale (l/new-locale)
    :config (c/new-config (c/prod-conf-or-dev))
    :db (component/using (new-db) [:config])
    :handler (component/using (new-handler) [:config :locale])
    :web (component/using (new-web-server-prod) [:handler :config])))

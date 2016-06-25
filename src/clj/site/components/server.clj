(ns site.components.server
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre]
            [ring.server.standalone :refer [serve]]
            ;[org.httpkit.server :refer [run-server]]
            [immutant.web :as web]
            [environ.core :refer [env]]
            [cronj.core :as cronj]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [site.session :as session]))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "site is shutting down...")
  (cronj/shutdown! session/cleanup-job)
  (timbre/info "shutdown complete!"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  [config]
  (timbre/set-config! {})
  (timbre/merge-config!
    {:appenders {:rotor {:min-level             :info
                         :enabled?              true
                         :async?                false       ; should be always false for rotor
                         :max-message-per-msecs nil
                         :fn                    rotor/rotor-appender}}})

  (timbre/merge-config!
    {:shared-appender-config {:rotor {:path "logs/site.log" :max-size (* 512 1024) :backlog 10}}})

  (when (= (:env config) :dev) (parser/cache-off!))
  ;;start the expired session cleanup job
  (cronj/start! session/cleanup-job)
  (timbre/info "\n-=[ site started successfully"
               (when (= (:env config) :dev) "using the development profile") "]=-"))

(defrecord WebServer [handler config]
  component/Lifecycle
  (start [component]
    (let [handler (:handler handler)
          config  (:config config)
          server  (serve handler
                         {:port          (:port config)
                          :init          (partial init config)
                          :auto-reload?  true
                          :destroy       destroy
                          :join?         false
                          :open-browser? false})]
      (assoc component :server server)))
  (stop [component]
    (let [server (:server component)]
      (when server (.stop server)))
    component))

(defn new-web-server []
  (map->WebServer {}))

(defrecord WebServerProd [handler config]
  component/Lifecycle
  (start [component]
    (let [handler (:handler handler)
          server  (web/run handler {:port (env :openshift-dip-port (get-in config [:config :port] 8080)) :host (env :openshift-diy-ip "127.0.0.1")})]
      (assoc component :server server)))
  (stop [component]
    (let [server (:server component)]
      (when server (server)))
    component))

(defn new-web-server-prod []
  (map->WebServerProd {}))

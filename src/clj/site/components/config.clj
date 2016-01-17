(ns site.components.config
  (:require [com.stuartsierra.component :as component]
            [nomad :refer [read-config]]
            [environ.core :refer (env)]
            [clojure.java.io :as io]))

(defn prod-conf-or-dev []
  (if-let [config-path (env :closp-config-path)]
    (read-config (io/file config-path))
    (read-config (io/resource "closp.edn"))))

(defrecord Config [config]
  component/Lifecycle
  (start [component]
    (assoc component :config {:hostname                (:hostname config)
                              :mail-from               (:mail-from config)
                              :mail-type               (:mail-type config)
                              :activation-mail-subject (:activation-mail-subject config)
                              :activation-mail-body    (:activation-mail-body config)
                              :activation-placeholder  (:activation-placeholder config)
                              :smtp-data               (:smtp-data config) ; passed directly to postmap like {:host "postfix"}
                              :jdbc-url                (env :openshift-postgresql-db-url (:jdbc-url config))
                              :env                     (:env config)
                              :under-construction      (:under-construction config)
                              :registration-allowed?   (:registration-allowed? config)
                              :captcha-enabled?        (:captcha-enabled? config)
                              :private-recaptcha-key   (:private-recaptcha-key config)
                              :recaptcha-domain        (:recaptcha-domain config)
                              :captcha-public-key      (:captcha-public-key config)
                              :port                    (:port config)
                              :closp-definitions       (:closp-definitions config)}))
  (stop [component]
    (assoc component :config nil)))

(defn new-config [config]
  (->Config config))

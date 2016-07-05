(ns site.middleware
  (:require [taoensso.timbre :as timbre]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
            [noir-exception.core :refer [wrap-internal-error]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [buddy.auth.accessrules :refer [wrap-access-rules]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [noir.session :as sess]
            [taoensso.tower.ring :refer [wrap-tower]]
            [de.sveri.clojure.commons.middleware.util :refer [wrap-trimmings]]
            [clojure-miniprofiler :refer [wrap-miniprofiler in-memory-store]]
            [ring.middleware.transit :refer [wrap-transit-response]]
            [site.service.auth :refer [auth-backend]]
            [site.service.auth :as auth]))

(defonce in-memory-store-instance (in-memory-store))

(defn log-request [handler]
  (fn [req]
    (timbre/debug req)
    (handler req)))

(defn add-req-properties [handler config]
  (fn [req]
    (sess/put! :registration-allowed? (:registration-allowed? config))
    (sess/put! :captcha-enabled? (:captcha-enabled? config))
    (handler req)))

(def development-middleware
  [wrap-error-page
   #(wrap-miniprofiler % {:store in-memory-store-instance})
   wrap-exceptions])

(defn production-middleware [config tconfig]
  [#(add-req-properties % config)
   #(wrap-access-rules % {:rules auth/rules})
   #(wrap-authorization % auth/auth-backend)
   (if (= (:env config) :prod)
     #(wrap-internal-error % :log (fn [e] (timbre/error e)))
     identity)
   #(wrap-tower % tconfig)
   #(wrap-transit-response % {:encoding :json :opts {}})
   wrap-anti-forgery
   wrap-trimmings])

(defn load-middleware [config tconfig]
  (timbre/info "Running with" (:env config) "profile")
  (concat (production-middleware config tconfig)
          (when (= (:env config) :dev) development-middleware)))

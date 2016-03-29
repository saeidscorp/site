(ns site.components.handler
  (:require [taoensso.timbre :as timbre]
            [compojure.core :refer [defroutes]]
            [ring.middleware.ssl :as ssl]
            [noir.response :refer [redirect]]
            [noir.util.middleware :refer [app-handler]]
            [ring.middleware.defaults :refer [site-defaults]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [compojure.route :as route]
            [environ.core :refer (env)]
            [com.stuartsierra.component :as comp]
            [site.routes.home :refer [home-routes]]
            [site.routes.cc :refer [cc-routes]]
            [site.routes.user :refer [user-routes registration-routes]]
            [site.routes.blog :refer [blog-routes]]
            [site.middleware :refer [load-middleware]]))

(defroutes base-routes
  (route/not-found (site.layout/render "not-found.html")))

(defroutes construction-routes
  (compojure.core/GET "/" [] (site.layout/render "under-construction.html"))
  (compojure.core/GET "/*" [] (redirect "/")))

;; timeout sessions after 30 minutes
(def session-defaults
  {:timeout (* 60 30)
   :timeout-response (redirect "/")})

(defn- mk-defaults
       "set to true to enable XSS protection"
       [xss-protection?]
       (-> site-defaults
           (update-in [:session] merge session-defaults)
           (assoc-in [:security :anti-forgery] xss-protection?)))

(defmacro wrap-if [handler condition & args]
  `(if ~condition (-> ~handler ~@args) ~handler))

(defn get-handler [config locale]
  (timbre/info (str "USING CONSTRUCTION PROFILE: " (:under-construction config)))
  (-> (app-handler
        (into [] (concat (when (:registration-allowed? config) [(registration-routes config)])
                         ;; add your application routes here
                         (let [rts [(cc-routes config) home-routes blog-routes (user-routes config) base-routes]]
                           (if (:under-construction config) (vec (cons construction-routes rts))
                                                            rts))))
        ;; add custom middleware here
        :middleware (load-middleware config (:tconfig locale))
        :ring-defaults (mk-defaults false)
        ;; add access rules here
        :access-rules []
        ;; serialize/deserialize the following data formats
        ;; available formats:
        ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
        :formats [:json-kw :edn :transit-json])
      ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-resource "public")
      (wrap-file (str (env :openshift-data-dir "resources/") "public"))
      ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)
      (wrap-if (= (:env config) :prod)
               ssl/wrap-forwarded-scheme
               ssl/wrap-hsts
               ssl/wrap-ssl-redirect)))

(defrecord Handler [config locale]
  comp/Lifecycle
  (start [comp]
    (assoc comp :handler (get-handler (:config config) locale)))
  (stop [comp]
    (assoc comp :handler nil)))

(defn new-handler []
  (map->Handler {}))

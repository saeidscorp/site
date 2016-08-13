(ns site.components.handler
  (:require [taoensso.timbre :as timbre]
            [site.utils :refer [handler ->>>]]
            [bidi.ring :as bdr :refer [make-handler]]
            [ring.middleware.ssl :as ssl]
            [noir.response :refer [redirect]]

            [noir.util.middleware]

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
            [site.middleware :refer [load-middleware]]
            [site.service.media :as media]))

;; TODO: either fully move media serving facilities to somewhere else or make it work.
(def base-routes
  [true [["/media/*" (bdr/->Files {:dir "./resources/public/mediatwo"})]
         [true (route/not-found (site.layout/render "not-found.html"))]]])

(def construction-routes
  ["/" [["" (handler [] (site.layout/render "under-construction.html"))]
        [#".*" (handler [] (redirect "/"))]]])

;; timeout sessions after 30 minutes
(def session-defaults
  {:timeout          (* 60 30)
   :timeout-response (redirect "/")})

(defn- mk-defaults
  "set to true to enable XSS protection"
  [xss-protection?]
  (-> site-defaults
      (update-in [:session] merge session-defaults)
      (assoc-in [:security :anti-forgery] xss-protection?)))

(defmacro wrap-if [handler condition & args]
  `(if ~condition (-> ~handler ~@args) ~handler))

;; these two functions were stolen from ring.util.middleware namespace
(defn wrap-middleware [routes [wrapper & more]]
  (if wrapper (recur (wrapper routes) more) routes))

(defn app-handler
  [app-routes & {:keys [base-url session-options middleware access-rules formats ring-defaults]}]
  (letfn [(wrap-middleware-format [handler]
            (if formats (ring.middleware.format/wrap-restful-format handler :formats formats) handler))]
    (-> (make-handler app-routes)
        (wrap-middleware middleware)
        (noir.util.middleware/wrap-request-map)
        (ring.middleware.defaults/wrap-defaults (dissoc (or ring-defaults site-defaults) :session))
        (hiccup.middleware/wrap-base-url base-url)
        (wrap-middleware-format)
        (noir.util.middleware/wrap-access-rules access-rules)
        (noir.validation/wrap-noir-validation)
        (noir.cookies/wrap-noir-cookies)
        (noir.session/wrap-noir-flash)
        (noir.session/wrap-noir-session
          (update-in
            (or session-options (:session ring-defaults) (:session site-defaults))
            [:store] #(or % (ring.middleware.session.memory/memory-store noir.session/mem)))))))

(defn get-routes [config]
  (site.utils/merge-routes (into [] (concat (when (:registration-allowed? config) [(registration-routes config)]
                                              (let [rts [(cc-routes config) home-routes blog-routes (user-routes config) base-routes]]
                                                (if (:under-construction config) (vec (cons construction-routes rts))
                                                                             rts)))))))


(defn get-handler [routes {config :config} locale]
  (timbre/info (str "USING CONSTRUCTION PROFILE: " (:under-construction config)))
  (media/data-dir config)
  (media/media-dir config)
  (-> (app-handler
        routes
        ;; add custom middleware here
        :middleware (load-middleware config (:tconfig locale))
        :ring-defaults (merge (mk-defaults false) {:params {:multipart true}})
        ;; add access rules here
        :access-rules []
        ;; serialize/deserialize the following data formats
        ;; available formats:
        ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
        :formats [:json-kw :edn :transit-json])
      ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-resource "public")
      (wrap-file (media/data-path))
      ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)
      (wrap-if (= (:env config) :prod)
               ;ssl/wrap-forwarded-scheme
               ssl/wrap-hsts)))
               ;ssl/wrap-ssl-redirect)))

(defrecord Handler [config locale]
  comp/Lifecycle
  (start [comp]
    (->>> comp
          (assoc _ :routes (get-routes (:config config)))
          (assoc _ :handler (get-handler (:routes _) (:config _) locale))))
  (stop [comp]
    (assoc comp :handler nil)))

(defn new-handler []
  (map->Handler {}))

(ns site.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [site.layout :as layout]
            [site.db.entities :as post]
            [ring.util.response :refer [response]]))

(defn home-page []
  (layout/render "home/index.html"))

(defn contact-page []
  (layout/render "home/contact.html"))

(defn tos-page []
  (layout/render "home/tos.html"))

(defn cookies-page []
  (layout/render "home/cookies.html"))

(defn example-page []
  (layout/render "home/example.html"))

(defn ajax-page []
  (layout/render "home/ajax-example.html"))

(defn post []
  (layout/render "home/post.html" (first (post/get-latest-posts 1))))

(defn ajax-initial-data []
  (response {:ok "fooo" :loaded true}))

(defroutes home-routes
           (GET "/contact" [] (contact-page))
           (GET "/tos" [] (tos-page))
           (GET "/cookies" [] (cookies-page))
           (GET "/" [] (layout/render "under-construction.html"))
           (GET "/test" [] (layout/render "test.html"))
           (GET "/example" [] (example-page))
           (GET "/posts" [] (post))
           (GET "/ajax/page" [] (ajax-page))
           (GET "/ajax/page/init" [] (ajax-initial-data)))

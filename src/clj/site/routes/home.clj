(ns site.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [site.utils :refer [handler]]
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

(def home-routes
  ["/" [[:get [["contact" (handler [] (contact-page))]
               ["tos" (handler [] (tos-page))]
               ["cookies" (handler [] (cookies-page))]
               ["" (handler [] (layout/render "under-construction.html"))]
               ["test" (handler [] (layout/render "test.html"))]
               ["example" (handler [] (example-page))]
               ["posts" (handler [] (post))]
               ["ajax/" [["page" (handler [] (ajax-page))]
                         ["page/init" (handler [] (ajax-initial-data))]]]]]]])
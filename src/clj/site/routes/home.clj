(ns site.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [site.utils :refer [handler]]
            [site.layout :as layout]
            [site.db.entities :as post]
            [ring.util.response :refer [response]]

            [site.db.entities :as e]
            [clojure-miniprofiler :refer [trace]]))

(defn home-page []
  (let [recent-posts (trace "latest-posts" (e/get-latest-posts 3))]
    (layout/render "home/index.html" {:recent-posts recent-posts})))

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
  (layout/render "home/post.html" (post/get-latest-post)))

(defn ajax-initial-data []
  (response {:ok "fooo" :loaded true}))

(def home-routes
  ^{:name "Home"
    :url :home}
  ["/" [[:get [^{:name "Contact"
                 :url :contact}
               ["contact" (handler :contact [] (contact-page))]
               ["tos" (handler [] (tos-page))]
               ["cookies" (handler [] (cookies-page))]
               ["" (handler :home [] (home-page))]
               ["home" (handler :home [] (home-page))]
               ["test" (handler [] (layout/render "test.html"))]
               ["example" (handler [] (example-page))]
               ["posts" (handler [] (post))]
               ["ajax/" [["page" (handler [] (ajax-page))]
                         ["page/init" (handler [] (ajax-initial-data))]]]]]]])
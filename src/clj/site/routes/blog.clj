(ns site.routes.blog
  (:require [compojure.core :refer (defroutes GET PUT ANY)]
            [site.db.entities :as e]
            [site.layout :as layout]
            [ring.util.response :refer [redirect]]))

(defn latest-posts [title])

(defroutes blog-routes
   (GET "/blog/latest-posts" [] (layout/render "blog/multi-card-side.html" {}))
   (GET "/blog/card-boxed" [] (layout/render "blog/card-boxed.html"))
   (GET "/blog/card-side" [] (layout/render "blog/card-side.html"))
   (GET "/blog/multi-full" [] (layout/render "blog/multi-full.html"))
   (GET "/blog/single-full" [] (layout/render "blog/single-full.html"))
   (GET "/blog/single-side" [] (layout/render "blog/single-side.html"
                                  {:breadcrumb-path
                                                  [{:href "/" :name "Home"}
                                                   {:href "/blog" :name "blog"}]
                                    :popular-tags [{:href "/tag/clojure" :name "Clojure"}
                                                   {:href "/tag/java" :name "Java"}]
                                    :categories   [{:href "/tag/clojure" :name "Clojure"}
                                                   {:href "/tag/java" :name "Java"}]})))
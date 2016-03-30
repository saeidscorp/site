(ns site.routes.blog
  (:require [compojure.core :refer (defroutes GET PUT ANY)]
            [site.db.entities :as e]
            [site.layout :as layout]
            [ring.util.response :refer [redirect]]))

(defn latest-posts [title])

(defroutes blog-routes
           (GET "/blog/multi-card-boxed" [] (layout/render "blog/multi-card-boxed.html")) ;; check
           (GET "/blog/multi-card-side" [] (layout/render "blog/multi-card-side.html")) ;; check
           (GET "/blog/multi-full" [] (layout/render "blog/multi-full.html"))
           (GET "/blog/multi-side" [] (layout/render "blog/multi-side.html"))
           (GET "/blog/single-full" [] (layout/render "blog/single-full.html")) ;; check
           (GET "/blog/single-side" [] (layout/render "blog/single-side.html" ;; check
                                                      {:breadcrumb-path
                                                                     [{:href "/" :name "Home"}
                                                                      {:href "/blog" :name "blog"}]
                                                       :popular-tags [{:href "/tag/clojure" :name "Clojure"}
                                                                      {:href "/tag/java" :name "Java"}]
                                                       :categories   [{:href "/tag/clojure" :name "Clojure"}
                                                                      {:href "/tag/java" :name "Java"}]})))
(ns site.routes.blog
  (:require [compojure.core :refer (defroutes GET PUT ANY)]
            [korma.core :as k]
            [site.db.entities :as e]
            [site.layout :as layout]
            [ring.util.response :refer [redirect]]))

(def single-context-map {:breadcrumb-path [{:href "/" :name "Home"} {:href "/blog" :name "blog"}]
                         :popular-tags    [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                         :categories      [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                         :post            {:content "Lorem ispum dolor sit amet."
                                           :title   "A Blog Post"
                                           :author  {:name      "Saeid"
                                                     :image-src "/assets/images/blog/author/author_1.jpg"}
                                           :tags    [{:name "Welcome" :url "#"}]}})

(def multi-context-map {:breadcrumb-path [{:href "/" :name "Home"} {:href "/blog" :name "blog"}]
                        :popular-tags    [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                        :categories      [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                        :posts           [{:title       "Welcome to this blog!"
                                           :description "Lorem ispum dolor sit amet."
                                           :author      {:name      "Saeid"
                                                         :image-src "/assets/images/blog/author/author_1.jpg"}
                                           :tags        [{:name "Welcome" :url "#"}]}
                                          {:title       "Mac OS X Yosemite"
                                           :description "This is the first time I tried Mac OS X."
                                           :author      {:name      "Saeid"
                                                         :image-src "/assets/images/blog/author/author_3.jpg"}
                                           :tags        [{:name "OS" :url "#"}]
                                           :image-src   "/media/uploads/screenshot.png"}
                                          {:title       "OpenShift Hosting"
                                           :description "Lorem ipsum dolor sit amet."
                                           :author      {:name      "Saeid"
                                                         :image-src "/assets/images/blog/author/author_2.jpg"}
                                           :tags        [{:name "Clojure" :url "#"}
                                                         {:name "Cloud" :url "#"}]}]})

(def multi-card-context-map {:breadcrumb-path [{:href "/" :name "Home"} {:href "/blog" :name "blog"}]
                             :popular-tags    [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                             :categories      [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                             :blog-box-rows   [[{:title       "Welcome to this blog!"
                                                 :description "Lorem ispum dolor sit amet."
                                                 :author      {:name      "Saeid"
                                                               :image-src "/assets/images/blog/author/author_1.jpg"}
                                                 :tags        [{:name "Welcome" :url "#"}]
                                                 :categories [{:title "Welcome"}]
                                                 :has-image true
                                                 :date "02 Sep"}
                                                {:title       "Clojure for Summer!"
                                                 :description "A short outline of plans to do in summer."
                                                 :author      {:name "Saeid" :image-src ""}
                                                 :tag         [[:name "Plans" :url "#"] [:name "Clojure" :url "#"]]}]
                                               [{:title       "Mac OS X Yosemite"
                                                 :description "This is the first time I tried Mac OS X."
                                                 :author      {:name      "Saeid"
                                                               :image-src "/assets/images/blog/author/author_3.jpg"}
                                                 :tags        [{:name "OS" :url "#"}]
                                                 :image-src   "/media/uploads/screenshot.png"}
                                                {:title       "OpenShift Hosting"
                                                 :description "Lorem ipsum dolor sit amet."
                                                 :author      {:name      "Saeid"
                                                               :image-src "/assets/images/blog/author/author_2.jpg"}
                                                 :tags        [{:name "Clojure" :url "#"}
                                                               {:name "Cloud" :url "#"}]}]]})

(defn latest-posts
  ([n] (e/get-latest-posts n)))

(defroutes blog-routes
           (GET "/blog/multi-card-boxed" [] (layout/render "blog/multi-card-boxed.html" multi-card-context-map)) ;; check
           (GET "/blog/multi-card-side" [] (layout/render "blog/multi-card-side.html" multi-context-map)) ;; check
           (GET "/blog/multi-full" [] (layout/render "blog/multi-full.html" multi-context-map))
           (GET "/blog/multi-side" [] (layout/render "blog/multi-side.html" multi-context-map))
           (GET "/blog/single-full" [] (layout/render "blog/single-full.html" single-context-map)) ;; check
           (GET "/blog/single-side" [] (layout/render "blog/single-side.html" single-context-map)))
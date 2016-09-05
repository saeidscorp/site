(ns site.routes.blog
  (:require [compojure.core :refer (defroutes GET PUT ANY)]
            [site.utils :refer [handler ->>>]]
            [site.db.entities :as e]
            [site.service.media :as media]
            [site.layout :as layout]
            [ring.util.response :refer [redirect]]
            [bidi.bidi :as bd]
            [site.utils.markdown]
            [me.raynes.fs :as fs])
  (:use delimc.core
        hara.event)
  (:import (java.io IOException)))

(def sample-context-map {:breadcrumb-path [{:href "/" :name "Home"} {:href "/blog" :name "blog"}]
                         :popular-tags    [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                         :categories      [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                         :comments        true})

;; TODO: use site.service.user/get-logged-in-user
(defn handle-new-post [{{:keys [title short-title short-content post-content]}
                        :params :as reqmap}]
  ;#spy/p reqmap
  (reset (shift k (e/create-post title short-title short-content post-content) (k :ok))
         (layout/render "blog/redirect-after.html" {:status          :success
                                                    :message         "Post created successfully."
                                                    :detail-message  "Redirecting to post..."
                                                    :redirect-target (bd/path-for site.layout/routes :post :url-title short-title)})))

(defn handle-image-upload [{{{:keys [filename tempfile content-type] :as file-props} :editormd-image-file} :params :as reqmap}]
  (cheshire.core/encode
    (reset (let [path (str (media/media-path reqmap) filename)]
             (shift k (try (fs/copy+ tempfile path)
                           (k :ok)
                           (catch IOException e
                             (signal [:upload-image-error {:exception  e
                                                           :file-props file-props}])
                             {:success 0})))
             ;; FIXME: relying on content-type sent from client is dangerous.
             (shift k (e/create-media path content-type)
                    (k {:success 1
                        :message "successfully uploaded."
                        :url     (str "/media/uploads/" filename)}))))))

(defn make-cards [posts n]
  (into [] (map vec (partition n posts))))

(def blog-routes
  ["/" [["blog/" [[:get [["multi-card-boxed" (handler [] (layout/render "blog/multi-card-boxed.html" sample-context-map))]
                         ["multi-card-side" (handler [] (layout/render "blog/multi-card-side.html" sample-context-map))]
                         ["multi-full" (handler [] (layout/render "blog/multi-full.html" (assoc sample-context-map
                                                                                           :posts (e/get-latest-posts))))]
                         ["multi-side" (handler [] (layout/render "blog/multi-side.html" (assoc sample-context-map
                                                                                           :posts (e/get-latest-posts))))]
                         [["single-full/" :id] (handler [id] (layout/render "blog/single-full.html" (assoc sample-context-map
                                                                                                      :post (e/get-post-by-id id))))]
                         [["single-side/" :id] (handler :post-id [id] (layout/render "blog/single-side.html"
                                                                                     (assoc sample-context-map :post (->>> (e/get-post-by-id id)
                                                                                                                           (assoc _
                                                                                                                             :content (site.utils.markdown/markdown-to-html (:content _)))))))]
                         [#"all/?" (handler :all-posts []
                                         (layout/render "blog/multi-card-boxed.html"
                                                        (assoc sample-context-map :posts-card (make-cards (e/get-latest-posts 12) 3))))]
                         ;; single blog post
                         [[:url-title] (handler :post [url-title]
                                         (layout/render "blog/single-full.html"
                                                        (assoc sample-context-map :post (->>> (e/get-post-by-title url-title)
                                                                                              (assoc _
                                                                                                :content (site.utils.markdown/markdown-to-html (:content _)))))))]]]
                  ["admin/" [["post" [[:get (handler :post-page []
                                              (layout/render "blog/write-post.html" {:image-upload-url (bd/path-for site.layout/routes :upload-image)}))]
                                      [:post (handler :post-do [:as reqmap]
                                               (handle-new-post reqmap))]]]
                             ["upload-image" [[:post (handler :upload-image [:as reqmap]
                                                       (handle-image-upload reqmap))]]]]]]]
        [#"blog/?" [[:get (handler [] (layout/render "blog/multi-full.html"
                                                  (assoc sample-context-map
                                                    :posts (e/get-latest-posts))))]]]

        ["author/" [[:get [[[[#"\d+" :id]] (handler :author-id [id]
                                             {:body (ring.util.response/response (str "author: " id))})]
                           [[:name] (handler :author [name]
                                      (ring.util.response/response (str "author name: " name)))]]]]]]])
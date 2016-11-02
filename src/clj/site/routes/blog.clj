(ns site.routes.blog
  (:require [compojure.core :refer (defroutes GET PUT ANY)]
            [site.utils :refer [handler]]
            [site.db.entities :as e]
            [site.service.media :as media]
            [site.layout :as layout]
            [ring.util.response :refer [redirect]]
            [bidi.bidi :as bd]
            [site.utils.markdown]
            [me.raynes.fs :as fs])
  (:use delimc.core
        hara.event)
  (:import (java.io IOException)
           (java.sql SQLException)))

(def sample-context-map {:popular-tags     [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                         :categories       [{:href "/tag/clojure" :name "Clojure"} {:href "/tag/java" :name "Java"}]
                         :comments         true
                         :pagination-pages [{:number "&#8544"}
                                            {:number "&#8545"}]})

;; TODO: use site.service.user/get-logged-in-user
(defn handle-new-post [{{:keys [title short-title short-content post-content]}
                        :params :as reqmap}]
  ;#spy/p reqmap
  (reset (shift k (e/create-post title short-title short-content post-content
                                 (:id (site.service.user/get-logged-in-user))) (k :ok))
         (layout/render "blog/redirect-after.html" {:status          :success
                                                    :message         "Post created successfully."
                                                    :detail-message  "Redirecting to post..."
                                                    :redirect-target (bd/path-for site.layout/routes :post :url-title short-title)})))

(defn handle-edit-post [id {{:keys [title short-title short-content post-content]}
                            :params :as reqmap}]
  (let [post (e/get-post-by-id id)]
    (reset (shift k (when (= (:id (site.service.user/get-logged-in-user)) id) (k :ok)))
           (shift k (e/update-post id {:title     title
                                       :url_title short-title
                                       :short     short-content
                                       :content   post-content}) (k :ok))
           (layout/render "blog/redirect-after.html" {:status          :success
                                                      :message         "Post changed successfully."
                                                      :detail-message  "Redirecting to post..."
                                                      :redirect-target (bd/path-for site.layout/routes :post :url-title short-title)}))))

(defn handle-image-upload [{{{:keys [filename tempfile content-type] :as file-props} :editormd-image-file} :params :as reqmap}]
  (cheshire.core/encode
    (reset (let [file-path (str (media/media-path reqmap) filename)
                 url-path  (str (media/media-url-path reqmap) filename)]
             (shift k (try (fs/copy+ tempfile file-path)
                           (k :ok)
                           (catch IOException e
                             (signal [:upload-image-error {:exception  e
                                                           :file-props file-props}])
                             {:success 0})))
             ;; FIXME: relying on content-type sent from client is dangerous.
             (shift k (e/create-media url-path content-type)
                    (k {:success 1
                        :message "successfully uploaded."
                        :url     (str (media/media-url-path reqmap) filename)}))))))

(defn make-cards [posts n]
  (into [] (map vec (partition-all n posts))))

(defn pagination-params [{:keys [page items-per-page]}
                         & [{:keys [default-ipp default-page]
                             :or   {default-ipp 6 default-page 1}}]]
  (let [page           (if page (Long. ^String page) default-page)
        items-per-page (if items-per-page (Long. ^String items-per-page) default-ipp)]
    [page items-per-page]))

(defn pagination-pages [total-items page items-per-page]
  (let [total-pages (int (Math/ceil (/ (float total-items) items-per-page)))]
    [:pagination-pages (for [p (range 1 (inc total-pages))]
                         {:number p, :current (= p page)})
     :pagination-prev (let [prev (dec page)]
                        (if (pos? prev) {:active true, :number prev}
                                        {:active false}))
     :pagination-next (let [next (inc page)]
                        (if (<= next total-pages) {:active true, :number next}
                                                  {:active false}))]))

(def blog-routes
  ^{:name "Home"
    :url  :home}
  ["/" [^{:name "Blog"} ["blog/" [[:get [["multi-card-boxed" (handler [] (layout/render "blog/multi-card-boxed.html" sample-context-map))]
                                         ["multi-card-side" (handler [] (layout/render "blog/multi-card-side.html" sample-context-map))]
                                         ["multi-full" (handler [] (layout/render "blog/multi-full.html" (assoc sample-context-map
                                                                                                           :posts (e/get-latest-posts))))]
                                         ["multi-side" (handler [] (layout/render "blog/multi-side.html" (assoc sample-context-map
                                                                                                           :posts (e/get-latest-posts))))]
                                         [["single-full/" :id] (handler [id] (layout/render "blog/single-full.html" (assoc sample-context-map
                                                                                                                      :post (e/get-post-by-id id))))]
                                         [["post/" :id] (handler :post-id [id] (layout/render "blog/single-side.html"
                                                                                              (assoc sample-context-map :post (as-> (e/get-post-by-id id) _
                                                                                                                                    (assoc _
                                                                                                                                      :content (site.utils.markdown/markdown-to-html (:content _)))))))]
                                         ;; TODO: implement pagination as a middleware
                                         ^{:name "All"
                                           :url  :all-posts}
                                         ;; FIXME: any invalid url input would cause a 500.
                                         [#{"all" "all/"} (handler :all-posts [:as reqmap]
                                                            (layout/render "blog/multi-card-boxed.html"
                                                                           (if-let [[page items-per-page] (pagination-params (:params reqmap) {:default-ipp 12})]
                                                                             (as-> sample-context-map _
                                                                                   (assoc _ :posts-card (make-cards (e/get-posts-range
                                                                                                                      (* (dec page) items-per-page) items-per-page)
                                                                                                                    3))
                                                                                   (apply assoc _ (pagination-pages (e/all-posts-count) page items-per-page)))
                                                                             (assoc sample-context-map :posts-card (make-cards (e/get-latest-posts 12) 3)))))]
                                         ;; single blog post
                                         [[:url-title] (handler :post [url-title]
                                                         (layout/render "blog/single-full.html"
                                                                        (assoc sample-context-map :post (as-> (e/get-post-by-title url-title) _
                                                                                                              (assoc _
                                                                                                                :content (site.utils.markdown/markdown-to-html (:content _)))))))]]]
                                  ^{:name "Admin"
                                    :url  :admin}
                                  ["admin/" [["post" [[:get (handler :post-page []
                                                              (layout/render "blog/write-post.html" {:image-upload-url (bd/path-for site.layout/routes :upload-image)}))]
                                                      [:post (handler :post-do [:as reqmap]
                                                               (handle-new-post reqmap))]]]
                                             [["edit/" :id] [[:get (handler :post-edit [id]
                                                                     (let [post (e/get-post-by-id (Integer/parseInt id))]
                                                                       (layout/render "blog/edit-post.html" {:image-upload-url   (bd/path-for site.layout/routes :upload-image)
                                                                                                             :post-title         (:title post)
                                                                                                             :post-short-title   (:url_title post)
                                                                                                             :post-short-content (:short post)
                                                                                                             :post-content       (:content post)
                                                                                                             :post               post})))]
                                                             [:post (handler [id :as reqmap]
                                                                      (let [post-id (Integer/parseInt id)]
                                                                        (handle-edit-post post-id reqmap)))]]
                                              ["delete/" :id] [[:post (handler :post-delete [id]
                                                                        (e/delete-post-by-id (Integer/parseInt id)))]]]
                                             ["upload-image" [[:post (handler :upload-image [:as reqmap]
                                                                       (handle-image-upload reqmap))]]]]]]]
        ^{:name "Blog"
          :url  :recent-posts}
        [#{"blog" "blog/"} [[:get (handler :recent-posts [:as reqmap]
                                    (layout/render "blog/multi-full.html"
                                                   (if-let [[page items-per-page] (pagination-params (:params reqmap) {:default-ipp 4})]
                                                     (as-> sample-context-map _
                                                           (assoc _
                                                             :posts (e/get-posts-range (* (dec page) items-per-page) items-per-page))
                                                           (apply assoc _ (pagination-pages (e/all-posts-count) page items-per-page)))
                                                     (assoc sample-context-map :posts (e/get-latest-posts)))))]]]

        ["author/" [[:get [[[[#"\d+" :id]] (handler :author-id [id]
                                             {:body (ring.util.response/response (str "author: " id))})]
                           [[:name] (handler :author [name]
                                      (ring.util.response/response (str "author name: " name)))]]]]]]])
(ns site.routes.cc
  (:require [compojure.core :refer [routes GET POST]]
            [site.utils :refer [handler]]
            [site.layout :as layout]
            [site.service.cc :as serv-cc]
            [ring.util.response :refer [response]])
  (:import (java.io File)))

(defn cc-page []
  (layout/render "cc/index.html"))

(defn initial-data [config]
  (let [files            (->> (:closp-definitions config)
                              (File.)
                              (file-seq)
                              (filter #(.isFile %)))
        files-beautified (map (fn [f] {:name (.getName f) :content (slurp f)}) files)]
    (response {:ex-entities files-beautified :loaded true})))

(defn cc-routes [config]
  ["/admin/cc" [[:get [["" (handler [] (cc-page))]
                       ["/initial" (handler [] (initial-data config))]]]]])

(ns site.service.media
  (:require [environ.core :refer [env]]
            [bundle-reader.core :as br]
            [optimus.assets :as assets]
            [optimus-sass.core]))

(defonce ^:private data-dir-state (atom nil))
(defn data-dir [config]
  (reset! data-dir-state (if-let [dir (:data-dir config)]
                           dir
                           (str (env :openshift-data-dir "resources/") "public/")))
  @data-dir-state)

(defn data-path
  ([request] (data-path))
  ([] @data-dir-state))

(defonce ^:private media-url-state (atom nil))
(defn media-url [config]
  (reset! media-url-state (env :media-url (:media-url config "/media/uploads/"))))

(defn media-url-path
  ([request] (media-url-path))
  ([] @media-url-state))

(defonce ^:private media-dir-state (atom nil))
(defn media-dir [config]
  (reset! media-dir-state (env :media-dir (:media-dir config (str (data-dir config) (subs @media-url-state 1))))))

(defn media-path
  ([request] (media-path))
  ([] @media-dir-state))

;; TODO: make it work with other file provider mechanisms
(defn prepare-assets [config]
  (fn get-assets []
    (let [[app-bundles app-resources] (br/get-assets-from-resources "public/" [] [])
          [libs-bundles libs-resources] (br/get-assets-from-resources "libs/" [] [])]
      (concat (apply assets/load-bundles app-bundles)
              (apply assets/load-assets app-resources)
              (apply assets/load-bundles libs-bundles)
              (apply assets/load-assets libs-resources)))))
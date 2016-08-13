(ns site.service.media
  (:require [environ.core :refer [env]]))

(defonce ^:private data-dir-state (atom nil))
(defn data-dir [config]
  (reset! data-dir-state (env :openshift-data-dir (:data-dir config "resources/public/")))
  @data-dir-state)

(defn data-path
  ([request] (data-path))
  ([] @data-dir-state))

(defonce ^:private media-dir-state (atom nil))
(defn media-dir [config]
  (reset! media-dir-state (env :media-dir (:media-dir config (str (data-dir config) "media/uploads/")))))

(defn media-path
  ([request] (media-path))
  ([] @media-dir-state))
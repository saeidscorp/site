(ns site.utils.markdown
  (:require [clojure.java.io :as jio])
  (:import (javax.script ScriptEngine ScriptEngineManager)))

(defonce ^:private javascript-engine
         (memoize #(-> (ScriptEngineManager.)
                       (.getEngineByMimeType "application/javascript"))))

(defn- eval-js [^String script]
  (.eval ^ScriptEngine (javascript-engine)
         script))

(defn- eval-js-file [path]
  (eval-js (slurp path)))

(defn- variable-name [var-name]
  (cond
    (keyword? var-name) (name var-name)
    :else (str var-name)))

(defn- get-js [var-name]
  (.get (javascript-engine)
        (variable-name var-name)))

(defn- set-js [var-name val]
  (.put (javascript-engine)
        (variable-name var-name) val))

(def ^:private script-path "scripts/marked.min.js")

(defonce ^:private load-marked
  (memoize #(eval-js-file (jio/resource script-path))))

(defn markdown-to-html [html-string]
  (locking javascript-engine
    (load-marked)
    (set-js :input html-string)
    (let [output (eval-js "marked(input)")]
      (eval-js "input = undefined;")
      output)))
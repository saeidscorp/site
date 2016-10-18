(ns site.utils.markdown
  (:require [clojure.java.io :as jio]
            [environ.core :refer [env]]
            [clojure.string :as str])
  (:import (javax.script ScriptEngine ScriptEngineManager)))

(defonce ^:private javascript-engine
  (delay (-> (ScriptEngineManager.)
             (.getEngineByName (env :optimus-js-engine "nashorn")))))

(defn- eval-js [^String script]
  (.eval ^ScriptEngine @javascript-engine
         script))

(defn- eval-js-file [path]
  (eval-js (slurp path)))

(defn- variable-name [var-name]
  (cond
    (keyword? var-name) (name var-name)
    :else (str var-name)))

(defn- get-js [var-name]
  (.get @javascript-engine
        (variable-name var-name)))

(def js-escape
  {\'       "\\'"
   \"       "\\\""
   \\       "\\\\"
   \newline "\\n"
   \return  "\\r"
   \u2028   "\\u2028"
   \u2029   "\\u2029"})

(defn- escape-val [str]
  (str/escape str js-escape))

(defn- set-js-str [var-name val]
  (.eval @javascript-engine
         (str "var " (variable-name var-name) " = \"" (escape-val val) "\";")))

(def ^:private script-path "libs/components/editor.md/lib/marked.min.js")

(defonce ^:private load-marked
         (memoize #(eval-js-file (jio/resource script-path))))

(defn markdown-to-html [md-string]
  (locking javascript-engine
    (load-marked)
    (set-js :input md-string)
    (let [output (eval-js "marked(input)")]
      (eval-js "input = undefined;")
      output)))
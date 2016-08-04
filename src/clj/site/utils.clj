(ns site.utils
  (:require [compojure.response]
            [clojure.walk :as w]
            [bidi.bidi :as bd]))

(defmacro ->>> [init & forms]
  (let [[bindings result] (reduce #(let [[prev-bindings prev-sym] %1
                                         changed (w/postwalk
                                                   (fn [x] (if (= x '_) prev-sym x)) %2)
                                         sym     (gensym)]
                                    [(conj prev-bindings sym changed) sym])
                                  (let [sym (gensym)] [[sym init] sym]) forms)]
    `(let ~bindings ~result)))

(defmacro handler [tag params & body]
  (let [[params body] (if (keyword? tag) [params body]
                                         [tag (vec (cons params body))])
        c          (count (take-while #(not= % :as) params))
        params-map (take c params)
        reqmap     (drop (inc c) params)
        reqmap     (if (empty? reqmap) ['-req#] reqmap)
        function   `(fn [req#] (let [{:keys ~params-map} (merge (:params req#) (:route-params req#))
                                     ~@reqmap req#]
                                 (compojure.response/render (do ~@body) req#)))]
    (if (keyword? tag) (bd/tag (eval function) tag) function)))

(defn merge-routes [routes]
  ["" (vec routes)])

(defn seqable? [x]
  (some boolean ((juxt map? seq? vector? set? list?) x)))
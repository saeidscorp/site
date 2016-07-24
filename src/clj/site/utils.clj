(ns site.utils
  (:require [compojure.response]
            [clojure.walk :as w]
            [bidi.bidi :as bd]))

;; O(n)

;; version 1
;(defmacro ->>> [init & forms]
;  (reduce #(w/postwalk (fn [x] (if (= x '_) %1 x)) %2)
;          init forms))

;; version 2
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

(handler :post [id] nil)
(handler [id] nil)

(defn merge-routes [routes]
  ["" (vec routes)])

;( (handler [a b] (println request) (+ a b))
;  {:route-params {:a 0 :b 1} :a 1})

;(defmacro test [params & body]
;  `[:mac-params ~params :mac-body [~@body]])

;(test :params :body)

;(s/check bidi.schema/RoutePair structure)
;(bd/match-route structure "/admin/delete")

;(def structure ["/" [["user/" [[:get [[[[#"\d+" :id]] :user]]]
;                               [:post [["welcome/" :user-welcome]]]
;                               ["change/" [["password" :user-change-password]]]]]
;                     ["admin/" [["delete/" [["user/" :admin-delete-user]]]
;                                [[:url] :admin]]]
;                     ["user/" [["change/" [[#"profile/?" :user-change-profile]]]]]]])
#_(
    :pair = [:pattern [:pair*]] | [:pattern :matched] | [:guard [:pair]]
    :pattern = :string | :regex | :boolean
    :guard = :method | :custom-guard
    :custom-guard = {}
    :method = :keyword
    :matched = :fn | :symbol | :keyword)

;(def z (zipper seq seq second structure))
;
;(->> (dz/children (-> z down right)) (map node))
;
;(-> z down right down right root)
;
;(def simple [:a [:b [:a [:a :x]]
;                 :a [:a [] :b []]
;             :a [:a [:b []
;                     :c [:a []]
;             :b [:g])
;
;(def simple-sol [:a [:a [:a [] :b [] :c [:a []]]
;                     :b [:a [:a :x]]
;                 :b [:g])
;
;(def sz (zipper seq #(partition 2 %) second simple))
;
;(->> sz dz/children (map ffirst))
;
;(->> sz down right node)

;; O(n log n)
(defn batch-by [f coll]
  (loop [result    {}
         remaining coll]
    (if (seq remaining)
      (let [item (first remaining)
            fres (f item)]
        (recur (assoc result fres (conj (get result fres) item))
               (rest remaining)))
      result)))

(defn children-folded [data]
  (if (map? data) (seq data) (partition 2 data)))

(defn children-flattened [data]
  (if (map? data) (mapcat identity data) (seq data)))

(defn children-double-flattened [data]
  (apply concat (apply concat data)))

(defn seqable? [x]
  (some boolean ((juxt map? seq? vector? set? list?) x)))

;(defn power-merge
;  [data]
;  (if (<= #spy/d (count data) 1)
;    data
;    (let [batches (batch-by first #spy/p (children-folded data))
;          unified (for [[prefix batch] batches]
;                    (conj [] prefix
;                      (power-merge (->>> batch
;                                         (map second _)
;                                         #spy/d (reduce conj [] _)
;                                         (if #spy/p (and (some seqable? _) (some (complement seqable?) _))
;                                           (map #(if-not (seqable? %) ["" %] %) _)
;                                           _)
;                                         (if (and (>= (count _) 1) (seqable? (nth _ 0)) (not-empty (nth _ 0)))
;                                           (map #(if (map? %) (mapcat identity %) %) _)
;                                           _)
;                                         #spy/p (if (and (<= (count _) 1) ((complement seqable?) (nth _ 0)))
;                                                  _ (mapcat identity _))
;                                         ;#spy/p (children-flattened _)
;                                         (vec _)))))]
;      #spy/d (vec (mapcat identity unified)))))


;; version 2
;; types:
;; pair only in top-level invocation
;; [:pair +] in [:pattern [:pair +]]
;; [:pair +] in [:guard [:pair +]]
;(defn power-merge [pack]
;  ())

;(s/check bidi.schema/RoutePair structure)
;(bd/match-route structure "/user/change/profile/")

;(def structure ["/" [["user/" [[:get [[[[#"\d+" :id]] :user-id]
;                                      ["" :user]
;                               [:get [["welcome/" :user-welcome]]]
;                               ["change/" [["password" :user-change-password]]]
;                     ["admin/" [["delete/" [["user/" :admin-delete-user]]]
;                                [[:url] :admin]
;                                ["delete/" :admin-delete]
;                     ["user/" [["change/" [[#"profile/?" :user-change-profile]]]]])))
#_(
    :pair = [:pattern [:pair +]] | [:guard [:pair +]] | [:pattern :matched]
    :pattern = :string | :regex | :boolean | [:pattern-segment +]
    :pattern-segment = :string | :regex | [(:string | :regex) :keyword]
    :guard = :method | :custom-guard
    :custom-guard = {}
    :method = :keyword
    :matched = :fn | :symbol | :keyword)
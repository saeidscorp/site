(ns site.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [ring.middleware.anti-forgery :as af]
            [bidi.bidi :as bd]
            [compojure.response :refer [Renderable]]
            [noir.session :as sess]
            [cuerdas.core :as str]
            [clojure.contrib.humanize :as hmz]
            [hara.time :as t]
            [hara.time.joda])
  (:use delimc.core
        hara.event))

(defonce routes nil)

(defn merge-flash-messages
  "Expects a map containing keys and a values which will be put into the sessions flash"
  [messages]
  (doseq [m messages]
    (sess/flash-put! (key m) (val m))))

(defn flash-result [message div-class]
  (merge-flash-messages {:flash-message message :flash-alert-type div-class}))

(parser/set-resource-path!  (clojure.java.io/resource "templates"))

(parser/add-tag! :url
                 (fn [[url-type & params] context-map]
                   (let [[handler handler-params] (case url-type
                                                    "post" [:post [:url-title]]
                                                    "author" [:author [:name]]
                                                    "tag" [:tag [:id]]
                                                    "page" [(keyword (first params)) []])]
                     (apply bd/path-for (:routes context-map) handler
                            (mapcat vector handler-params (map (selmer.filter-parser/lookup-args context-map) params))))))

(parser/add-tag! :rec-include
                 (fn [[filename & [_with_ & bindings] :as args] context-map]
                   (if _with_
                     (reset
                       (shift k (if (not= _with_ "with")
                                  (raise [:template-error
                                          {:msg (str "syntax error: unknown keyword " _with_)}])
                                  (k :ok)))
                       (shift k (if (empty? bindings)
                                  (raise [:template-error
                                          {:msg "with specified but no bindings provided"}])
                                  (k :ok)))
                       (let [raw-bs (map #(str/split % "=")
                                     bindings)
                             bs (map (fn [[k v]] [(keyword k) (context-map (keyword v))]) raw-bs)
                             new-cmap (reduce #(apply assoc %1 %2) context-map bs)]
                         (parser/render-file (str/unsurround filename "\"") new-cmap))))))

(t/default-type org.joda.time.DateTime)

(def month-name
  {1  ["January" "Jan."], 2 ["February" "Feb."], 3  ["March" "Mar."], 4 ["April" "Apr."],
   5  ["May" "May"], 6 ["June" "Jun."], 7  ["July" "Jul."], 8 ["August" "Aug."],
   9  ["September" "Sep."], 10 ["October" "Oct."], 11 ["November" "Nov."], 12 ["December" "Dec."]})

(defn humanize-date [date]
  (let [year (t/year date)
        month (t/month date)
        [month-full _] (month-name month)
        day (t/day date)]
    (str month-full " " day ", " year)))

(selmer.filters/add-filter! :pretty-date-span hmz/datetime)
(selmer.filters/add-filter! :pretty-date humanize-date)

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type
      (->> (assoc params
             (keyword (s/replace template #".html" "-selected")) "active"
             :servlet-context
             (if-let [context (:servlet-context request)]
               ;; If we're not inside a serlvet environment (for
               ;; example when using mock requests), then
               ;; .getContextPath might not exist
               (try (.getContextPath context)
                    (catch IllegalArgumentException _ context)))
             :identity (sess/get :identity)
             :role (sess/get :role)
             :af-token af/*anti-forgery-token*
             :page (:page request)
             :page-tempalate template
             :routes routes
             :registration-allowed? (sess/get :registration-allowed?)
             :captcha-enabled? (sess/get :captcha-enabled?)
             :flash-message (sess/flash-get :flash-message)
             :flash-alert-type (sess/flash-get :flash-alert-type))
           (parser/render-file (str template))
           response)
      "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))


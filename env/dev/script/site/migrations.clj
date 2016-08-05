(ns site.migrations
  (:require [joplin.alias :as ja]
            [joplin.repl :as repl]))


(defn migrate [config-file env & [db]]
  (let [conf (ja/*load-config* config-file)]
    (if db
      (repl/migrate conf (keyword env) (keyword db))
      (repl/migrate conf (keyword env)))))

(defn seed [config-file env & [db]]
  (let [conf (ja/*load-config* config-file)]
    (if db
      (repl/seed conf (keyword env) (keyword db))
      (repl/seed conf (keyword env)))))

(defn rollback [config-file env & [db num]]
  (let [conf (ja/*load-config* config-file)]
    (when (and db num)
      (repl/rollback conf
                     (keyword env) (keyword db)
                     (Long/parseLong num)))))

(defn reset [config-file env & [db]]
  (let [conf (ja/*load-config* config-file)]
    (when db
      (repl/reset conf (keyword env) (keyword db)))))

(defn pending [config-file env & [db]]
  (let [conf (ja/*load-config* config-file)]
    (when db
      (repl/pending conf (keyword env) (keyword db)))))

(defn create [config-file env & [db id]]
  (let [conf (ja/*load-config* config-file)]
    (when (and db id)
      (repl/create conf (keyword env) (keyword db) id))))

(ns site.session
  (:require [noir.session :refer [clear-expired-sessions]]
            [hara.io.scheduler :as sch]))

(def cleanup-job
  (sch/scheduler {:session-cleanup
                  {:handler  (fn [_ _] (clear-expired-sessions))
                   :schedule "* /30 * * * * *"
                   :params   {}}}))

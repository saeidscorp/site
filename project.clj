(defproject site "0.1.0-SNAPSHOT"
  :description "My personal site/blog system written in Clojure."
  :url "https://github.com/saeidscorp/site"
  :license {:name "GNU General Public License v3.0"
            :url  "https://www.gnu.org/licenses/gpl-3.0.en.html"}

  :source-paths ["src/clj" "src/cljs" "src/cljc"]

  ; TODO: remove java bullshits
  ;:java-source-paths ["src/java"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;[org.clojure/clojurescript "0.0-3308"]
                 ;[org.clojure/clojurescript "1.7.170"]
                 [org.clojure/clojurescript "1.9.89"]

                 ;[http-kit "2.1.19"]
                 ;[org.immutant/immutant "2.1.1"]
                 [org.immutant/immutant "2.1.4"]

                 [org.clojure/core.cache "0.6.4"]
                 ;[org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/core.async "0.2.385"]

                 ;[ring "1.4.0"]
                 [ring "1.5.0"]
                 [ring/ring-ssl "0.2.1"]

                 [lib-noir "0.9.9"]
                 [ring-server "0.4.0"]
                 ;[ring/ring-anti-forgery "1.0.0"]
                 [ring/ring-anti-forgery "1.0.1"]
                 ;[compojure "1.4.0"]
                 [compojure "1.5.1"]
                 [reagent "0.5.1"]
                 ;[environ "1.0.0"]
                 [environ "1.0.3"]
                 [leiningen "2.5.1"]
                 ;[selmer "0.8.5"]
                 [selmer "1.0.6"]
                 [prone "1.1.1"]
                 [im.chit/cronj "1.4.3"]
                 ;[com.taoensso/timbre "3.2.1"]
                 [com.taoensso/timbre "4.4.0"]
                 [noir-exception "0.2.5"]

                 [buddy/buddy-auth "0.6.0"]
                 ;[buddy/buddy-auth "0.13.0"]
                 [buddy/buddy-hashers "0.6.0"]
                 ;[buddy/buddy-hashers "0.14.0"]

                 ;[log4j "1.2.17" :exclusions [javax.mail/mail]]
                 [log4j "1.2.17" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]

                 [org.clojure/java.jdbc "0.3.7"]
                 ;[org.clojure/java.jdbc "0.6.1"]
                 [korma "0.4.2"]
                 [com.h2database/h2 "1.4.187"]
                 ;[org.xerial/sqlite-jdbc "3.8.10.1"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]

                 ;[com.draines/postal "1.11.3"]
                 [com.draines/postal "2.0.0"]

                 ;[jarohen/nomad "0.7.1"]
                 [jarohen/nomad "0.7.2"]

                 [de.sveri/clojure-commons "0.2.0"] ; do not touch :_

                 ;[clojure-miniprofiler "0.4.0" :exclusions [fipp]]
                 [clojure-miniprofiler "0.5.0" :exclusions [fipp]]
                 ;[fipp "0.6.2"]
                 [fipp "0.6.5"]

                 ;[org.danielsz/system "0.1.8"]
                 ;[org.danielsz/system "0.2.0"]
                 [org.danielsz/system "0.3.0"]

                 ;[datascript "0.11.6"]
                 [datascript "0.15.0"]
                 ;[cljs-ajax "0.3.14"]
                 [cljs-ajax "0.5.8"] ;;
                 ;[ring-transit "0.1.3"]
                 [ring-transit "0.1.5"]
                 ;[org.clojars.franks42/cljs-uuid-utils "0.1.3"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]

                 [net.tanesha.recaptcha4j/recaptcha4j "0.0.8"]

                 [com.taoensso/tower "3.0.2"]

                 ;[org.clojure/core.typed "0.3.11"]
                 [org.clojure/core.typed "0.3.23"]]

  :plugins [[lein-immutant "2.1.0"]
            [de.sveri/closp-crud "0.1.3"]
            ;[lein-cljsbuild "1.0.5"]
            [lein-cljsbuild "1.1.3"]]

  ;database migrations
  :joplin {:migrators {:sqlite-mig "resources/migrators/sqlite"
                       :h2-mig     "resources/migrators/h2"}}

  :closp-crud {:jdbc-url               "jdbc:sqlite:./db/site.sqlite"
               :migrations-output-path "./resources/migrators/sqlite"
               :clj-src                "src/clj"
               :ns-db                  "site.db"
               :ns-routes              "site.routes"
               :ns-layout              "site.layout"
               :templates              "resources/templates"}

  :min-lein-version "2.5.0"

  ; leaving this commented because of: https://github.com/cursiveclojure/cursive/issues/369
  ;:hooks [leiningen.cljsbuild]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild
  {:builds {:dev {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                  :figwheel     {:websocket-host "localhost"
                                 :on-jsload "site.dev/main"}
                  :compiler     {:main                 "site.dev"
                                 :asset-path           "/js/compiled/out"
                                 :output-to            "resources/public/js/compiled/app.js"
                                 :output-dir           "resources/public/js/compiled/out"
                                 ;:source-map           "resources/public/js/compiled/out.js.map"
                                 :source-map           true
                                 :optimizations        :none
                                 :cache-analysis       true
                                 :pretty-print         true
                                 :source-map-timestamp true}}
            :adv {:source-paths ["src/cljs" "src/cljc"]
                  :compiler     {:output-to     "resources/public/js/compiled/app.js"
                                 ; leaving this commented because of: https://github.com/cursiveclojure/cursive/issues/369
                                 ;:jar           true
                                 :optimizations :advanced
                                 :pretty-print  false}}}}

  :figwheel {:css-dirs   ["resources/public/css" "resources/public/assets/css"]
             :repl false
             :nrepl-port 3450
             :server-logfile "logs/figwheel.log"} ;; watch and update CSS}

  :sass {:src   "resources\\styles"
         :dst   "resources/public/assets/css/"
         :style :expanded}

  :profiles {:dev     {:repl-options {:init-ns site.user}

                       :source-paths ["env/dev/script"]

                       :plugins      [[lein-ring "0.9.0"]
                                      ;[lein-expectations "0.0.7"]
                                      [lein-expectations "0.0.8"]
                                      [lein-autoexpect "1.9.0"]
                                      ;[lein-figwheel "0.3.3"]
                                      [lein-figwheel "0.5.4-4"]
                                      ;[joplin.lein "0.2.17"]
                                      [joplin.lein "0.2.18"]
                                      ;[lein-cooper "1.2.2"]
                                      ;[test2junit "1.1.1"]
                                      ;[test2junit "1.2.2"] ;; we don't use clojure.test at all
                                      [lein-immutant "2.1.0"]
                                      [lein-sassy "1.0.7"]]

                       :dependencies [[org.bouncycastle/bcprov-jdk15on "1.52"]

                                      [figwheel-sidecar "0.5.4-4"]
                                      [com.cemerick/piggieback "0.2.1"]

                                      ; use this for htmlunit or an older firefox version
                                      ;[clj-webdriver "0.6.1"]
                                      [clj-webdriver "0.7.2"
                                       :exclusions [org.seleniumhq.selenium/selenium-server]]

                                      ; uncomment this to use current firefox version (does not work with htmlunit
                                      ;[clj-webdriver "0.6.1"
                                      ; :exclusions
                                      ; [org.seleniumhq.selenium/selenium-server
                                      ;  org.seleniumhq.selenium/selenium-java
                                      ;  org.seleniumhq.selenium/selenium-remote-driver]]

                                      ;[expectations "2.0.9"]
                                      [expectations "2.1.8"]

                                      ;[org.seleniumhq.selenium/selenium-server "2.46.0"]
                                      [org.seleniumhq.selenium/selenium-server "2.53.0"]
                                      [org.seleniumhq.selenium/selenium-htmlunit-driver "2.9.0" :exclusions [org.apache.httpcomponents/httpclient]]
                                      [org.seleniumhq.selenium/selenium-remote-driver "2.9.0" :exclusions [org.apache.httpcomponents/httpclient]]
                                      [org.seleniumhq.selenium/selenium-java "2.9.0" :exclusions [org.apache.httpcomponents/httpclient]]
                                      ;[ring-mock "0.1.5"]
                                      [ring/ring-mock "0.3.0"]
                                      ;[ring/ring-devel "1.4.0"]
                                      [ring/ring-devel "1.5.0"]
                                      ;[pjstadig/humane-test-output "0.7.0"] ; we don't use clojure.test so we don't need this
                                      ;[joplin.core "0.2.17"]
                                      [joplin.core "0.2.18"]
                                      ;[joplin.jdbc "0.2.17"]
                                      [joplin.jdbc "0.2.18"]]

                       ;:injections   [(require 'pjstadig.humane-test-output)
                       ;               (pjstadig.humane-test-output/activate!)]

                       :joplin       {:databases    {:sqlite-dev {:type :sql, :url "jdbc:sqlite:./db/site.sqlite"}
                                                     :h2-dev     {:type :sql, :url "jdbc:h2:./db/korma.db;DATABASE_TO_UPPER=FALSE"}}
                                      :environments {:sqlite-dev-env [{:db :sqlite-dev, :migrator :sqlite-mig}]
                                                     :h2-dev-env     [{:db :h2-dev, :migrator :h2-mig}]}}}

             :uberjar {:auto-clean  false                   ; not sure about this one
                       :omit-source true
                       :aot         :all}
             :default [:base :system :provided]}

  :test-paths ["test/clj" "integtest/clj"]

  :test-selectors {:unit        (complement :integration)
                   :integration :integration
                   :cur         :cur                        ; one more selector for, give it freely to run only
                   ; the ones you need currently
                   :all         (constantly true)}

  :test2junit-output-dir "test-results"

  :jvm-opts ["-Dhornetq.data.dir=target/hornetq-data"
             "-Dcom.arjuna.ats.arjuna.objectstore.objectStoreDir=target/ObjectStore"]

  :repl-options {:timeout 120000}

  :main site.core

  :uberjar-name "site.jar"

  :aliases {"rel-jar" ["do" "clean," "cljsbuild" "once" "adv," "sass" "once," "uberjar"]
            "devrepl" ["pdo" "clean," "figwheel," "sass" "watch," #_("autoexpect" ":growl,") "repl" ":headless"]
            "unit"    ["do" "test" ":unit"]
            "integ"   ["do" "test" ":integration"]})

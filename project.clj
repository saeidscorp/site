(defproject site "0.1.0-SNAPSHOT"
  :description "My personal site/blog system written in Clojure."
  :url "https://github.com/saeidscorp/site"
  :license {:name "GNU General Public License v3.0"
            :url  "https://www.gnu.org/licenses/gpl-3.0.en.html"}

  :source-paths ["src/clj" "src/cljs" "src/cljc"]

  ; TODO: remove java bullshits
  ;:java-source-paths ["src/java"]

                 ;; clojure/clojurescript
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]

                 ;; webserver
                 ;    [http-kit "2.1.19"]
                 [org.immutant/web "2.1.5" :exclusions [ch.qos.logback/logback-classic]]

                 ;; core facilities
                 [org.clojure/core.cache "0.6.5"]
                 [org.clojure/core.async "0.2.395"]

                 ;; ring handler
                 [ring "1.5.0"]
                 [ring/ring-ssl "0.2.1"]

                 ;; ring utilities
                 [lib-noir "0.9.9"]
                 [ring-server "0.4.0"]
                 [ring/ring-anti-forgery "1.0.1"]
                 [ring-transit "0.1.6"]

                 ;; routing
                 [compojure "1.5.1"]
                 [bidi "2.0.13"]

                 ;; templating
                 [selmer "1.0.10"]

                 ;; error-reporting
                 [prone "1.1.2"]
                 [noir-exception "0.2.5"]

                 ;; logging
                 [com.taoensso/timbre "4.7.4"]
                 ;;   wrap logging libraries to log to slf4j...
                 [org.slf4j/slf4j-api "1.7.21"]
                 [org.slf4j/log4j-over-slf4j "1.7.21"]
                 ;;   ... and route them through our lovely timbre
                 [com.fzakaria/slf4j-timbre "0.3.2"]

                 ;; authentication
                 [buddy/buddy-hashers "1.0.0"]
                 [buddy/buddy-auth "1.2.0"]

                 ;; database
                 [korma "0.4.3"]
                 ;[com.h2database/h2 "1.4.192"]
                 [org.xerial/sqlite-jdbc "3.14.2.1"]

                 ;; configuration
                 [jarohen/nomad "0.7.2"]

                 ;; profiling
                 [clojure-miniprofiler "0.5.0" :exclusions [fipp]]
                 [fipp "0.6.6"]                             ;; WARNING

                 ;; components and systems
                 [org.danielsz/system "0.3.1"]

                 ;; client-side
                 [datascript "0.15.4"]
                 [cljs-ajax "0.5.8"] ;;
                 [reagent "0.6.0"]

                 ;; utilities
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [net.tanesha.recaptcha4j/recaptcha4j "0.0.8"]
                 [environ "1.1.0"]
                 [funcool/cuerdas "2.0.0"]
                 [com.draines/postal "2.0.0"]
                 [de.sveri/clojure-commons "0.2.0"] ; do not touch :_
                 [me.raynes/fs "1.4.6"]

                 ;; useful libraries
                 [im.chit/hara.event "2.4.7"]
                 [im.chit/hara.io.scheduler "2.4.7"]
                 [im.chit/hara.expression "2.4.7"]
                 [im.chit/hara.function "2.4.7"]
                 [im.chit/hara.time "2.4.7"]
                 [im.chit/hara.time.joda "2.2.17"]
                 [delimc "0.1.0"]

                 ;; localization
                 [com.taoensso/tower "3.0.2"]
                 [clojure-humanize "0.2.2"]

                 ;; typed
                 [org.clojure/core.typed "0.3.28"]]

  :plugins [[lein-immutant "2.1.0"]
            [de.sveri/closp-crud "0.1.3"]
            ;[lein-cljsbuild "1.0.5"]
            [lein-cljsbuild "1.1.3"]]

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

  :sass {:src   "dev-resources\\styles"
         :dst   "resources/public/assets/css/"
         :style :expanded}

  :profiles {:dev     {:repl-options {:init-ns site.user}

                       :source-paths ["env" "env/dev/script"]

                       :plugins      [[lein-ring "0.9.0"]
                                      [lein-expectations "0.0.8"]
                                      [lein-autoexpect "1.9.0"]
                                      [lein-figwheel "0.5.8"]
                                      [lein-immutant "2.1.0"]
                                      [lein-sassy "1.0.8"]
                                      [lein-pdo "0.1.1"]]

                       :dependencies [[org.bouncycastle/bcprov-jdk15on "1.55"]

                                      [figwheel-sidecar "0.5.8"]
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

                                      ;; testing
                                      [expectations "2.1.9"]

                                      ;; integration testing
                                      ;[org.seleniumhq.selenium/selenium-server "2.46.0"]
                                      [org.seleniumhq.selenium/selenium-server "3.0.1"]
                                      [org.seleniumhq.selenium/selenium-htmlunit-driver "2.52.0" :exclusions [org.apache.httpcomponents/httpclient]]
                                      [org.seleniumhq.selenium/selenium-remote-driver "2.53.1" :exclusions [org.apache.httpcomponents/httpclient]]
                                      [org.seleniumhq.selenium/selenium-java "3.0.1" :exclusions [org.apache.httpcomponents/httpclient]]

                                      [ring/ring-mock "0.3.0"]
                                      [ring/ring-devel "1.5.0"]

                                      ;; for vinyasa's (./lein) to work
                                      [leiningen "2.6.1"]

                                      [lein-sassy "1.0.8"]

                                      ; new version of joplin (backwards incompatible)
                                      [joplin.jdbc "0.3.9"]
                                      [joplin.core "0.3.9"]]}

             :uberjar {:dependencies [[org.clojure/tools.nrepl "0.2.12"]]
                       :auto-clean  false                   ; not sure about this one
                       :omit-source true
                       :aot         :all}}
             ;:default [:base :system :provided]}

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
            "integ"   ["do" "test" ":integration"]

            ;; joplin migration
            "migrate" ["run" "-m" "joplin.alias/migrate" "joplin.edn" "sqlite-dev-env" "sqlite-dev"]
            "rollback" ["run" "-m" "joplin.alias/rollback" "joplin.edn" "sqlite-dev-env" "sqlite-dev"]
            "reset" ["run" "-m" "joplin.alias/reset" "joplin.edn" "sqlite-dev-env" "sqlite-dev"]
            "seed"  ["run" "-m" "joplin.alias/seed" "joplin.edn" "sqlite-dev-env" "sqlite-dev"]})

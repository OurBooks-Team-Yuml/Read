(defproject read-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.pedestal/pedestal.service "0.5.8"]
                 [io.pedestal/pedestal.route "0.5.8"]
                 [io.pedestal/pedestal.jetty "0.5.8"]
                 [org.slf4j/slf4j-simple "1.7.28"]]
  :main ^:skip-aot read-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:aliases {"run-dev" ["trampoline" "run" "-m" "hello-world.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.8"]]}})

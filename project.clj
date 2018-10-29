(defproject persons "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                  [org.clojure/java.jdbc "0.7.8"]
                  [mysql/mysql-connector-java "5.1.25"]
                  [http-kit "2.3.0"]
                  [ring/ring-json "0.4.0"]
                  [compojure "1.6.1"]
                  [ring "1.7.0"]]
  :main ^:skip-aot persons.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

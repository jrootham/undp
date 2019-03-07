(defproject fixedstrings "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies 
  [
  		[org.clojure/clojure "1.10.0"]
  		[org.postgresql/postgresql "42.2.5"]
		  [org.clojure/java.jdbc "0.7.9"]
  ]
  :main ^:skip-aot fixedstrings.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

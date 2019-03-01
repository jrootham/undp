(defproject update-version "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies 
  	[
  		[org.clojure/clojure "1.8.0"]
  		[org.postgresql/postgresql "9.4-1206-jdbc41"]
		[org.clojure/java.jdbc "0.7.1"]
  		[clojure-csv "2.0.2"]
  	]
  :main ^:skip-aot update-version.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

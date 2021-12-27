(defproject server "0.1.0-SNAPSHOT"
	:description "FIXME: write description"
	:url "http://example.com/FIXME"
	:license {:name "Eclipse Public License"
	:url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies 
  	[
  [org.clojure/clojure "1.10.0"]
		[ring/ring-core "1.7.1"]
		[ring/ring-jetty-adapter "1.7.1"]
		[ring-cors "0.1.13"]
		[com.unbounce/encors "2.4.0"]
		[ring/ring-json "0.4.0"]
		[bananaoomarang/ring-debug-logging "1.1.0"]
		[clj-http "3.9.1"]
		[compojure "1.6.1"]
		[org.postgresql/postgresql "42.2.5"]
		[org.clojure/java.jdbc "0.7.9"]
		[org.clojure/data.json "0.2.6"]
  	]
  	:main ^:skip-aot server.core
  	:target-path "target/%s"
  	:profiles {:uberjar {:aot :all}})

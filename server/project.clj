(defproject server "0.1.0-SNAPSHOT"
	:description "FIXME: write description"
	:url "http://example.com/FIXME"
	:license {:name "Eclipse Public License"
	:url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies 
  	[
  		[org.clojure/clojure "1.8.0"]
		[ring/ring-core "1.6.2"]
		[ring/ring-jetty-adapter "1.6.2"]
		[ring-cors "0.1.11"]
		[ring/ring-json "0.4.0"]
		[postgresql "9.3-1102.jdbc41"]
		[org.clojure/java.jdbc "0.7.1"]
		[org.clojure/data.json "0.2.6"]
  	]
  	:main ^:skip-aot server.core
  	:target-path "target/%s"
  	:profiles {:uberjar {:aot :all}})

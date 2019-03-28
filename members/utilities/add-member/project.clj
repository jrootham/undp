(defproject add-member "0.1.0-SNAPSHOT"
	:description "FIXME: write description"
	:url "http://example.com/FIXME"
	:license {:name "Eclipse Public License"
		:url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies 
	[
		[org.clojure/clojure "1.10.0"]
  		[org.postgresql/postgresql "42.2.5"]
		[org.clojure/java.jdbc "0.7.9"]
  		[clojure-csv "2.0.2"]
		[crypto-random "1.2.0"]
		[simple-email "1.0.7"]
	]
	:main ^:skip-aot add-member.core
	:target-path "target/%s"
	:profiles {:uberjar {:aot :all}})

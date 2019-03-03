(ns server.core-test
	(:require [clojure.test :refer :all])
	(:require [server.core :refer :all])
	(:require [clj-http.client :as client])
)

(def expected-fixed "[{\"name\":\"bar\",\"string\":\"bar and foo\"}]")

(defn get-fixedstring [version language]
	(let 
		[
			headers {"Origin" "http://localhost:3333"}
			response (client/get "http://localhost:3333/fixedstrings?version=1&language=en" {:headers headers})
		]
		(get response :body)
	)
)

(deftest fixedstring-test
	(testing "fixedstring" (is (= expected-fixed (get-fixedstring 1 "en"))))
)

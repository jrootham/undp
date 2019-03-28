(ns add-member.core
	(:gen-class)
	(:require [clojure.java.jdbc :refer :all :as jdbc])
	(:use clojure.java.jdbc)
	(:require [clojure-csv.core :refer [parse-csv]])
	(:require [crypto.random])
)

(def NONCE-SIZE 16)

(defn add-a-name [db row]
	(let
		[
			user-name (nth row 0)
			email (nth row 1)
			nonce (crypto.random/url-part NONCE-SIZE)
		]
		(insert! db :members {:user_name user-name :email email :nonce nonce})
	)
)

(defn add-names [csv-string  db-url]
	(with-db-connection [db {:connection-uri db-url}]
		(doall (map (partial add-a-name db) (parse-csv csv-string)))
	)
)

(defn -main
	"Update the member database with new names from a csv file"
	[& args]
	(if (= 4 (count args))
		(let
			[
				csv-name (nth args 0)
				db-name (nth args 1)
				db-user (nth args 2)
				db-password (nth args 3)

				csv-string (slurp csv-name)

				db-url (str "jdbc:postgresql:" db-name "?user=" db-user "&password=" db-password)
			]
			(add-names csv-string db-url)
		)
		(println "Usage: csv-name db-name db-user db-password")
	)
)

(ns update-version.core
	(:gen-class)
	(:require [clojure.java.jdbc :refer :all :as jdbc])
	(:use clojure.java.jdbc)
	(:import (java.sql SQLException))
	(:require [clojure-csv.core :refer [parse-csv]])
)

(def SUCCESS 0)
(def BADNAME 1)
(def DUPLICATENAME 2)
(def SQLERROR 3)
(def ERROR 4)
(def ARGUMENTERROR 5)

(defn valid-names [parsed-csv]
	(= 0 (count (filter (fn [row] (= "version" (nth row 0))) parsed-csv)))
)

(defn make-version [db]
	(let
		[
			query-string "SELECT COUNT(*) AS count FROM fixed_string;"
			result (query db [query-string])
			count-value (get (first result) :count)
		]
		(if (= 0 count-value)
			1
			(let
				[
					query-string "SELECT MAX(version) AS max FROM fixed_string;"
					result (query db [query-string])
					max-value (get (first result) :max)
				]
				(+ 1 max-value)
			)
		)
	)
)

(defn fill-row [db version row]
	(let
		[
			string-name (first row)
			string (second row)

			query-string "SELECT COUNT(*) AS count FROM fixed_string WHERE version = ? AND name = ?;"
			result (query db [query-string version string-name])
			count-value (get (first result) :count)
		]
		(if (= 0 count-value)
			(try 
				(let
				[
					row-list (insert! db :fixed_string {:name string-name :version version})
					string_id (get (first row-list) :id)
				]
					(insert! db :fixed_translation {:fixed_string_id string_id :language_id 1 :string string})
				)
		    	(catch SQLException e 
		    		(println (.getNextException e))
		    		(System/exit SQLERROR)
		    	)
		    	(catch Exception e 
		    		(println "Exception" (.getMessage e))
		    		(System/exit ERROR)
		    	)
		    )
			(do
				(println "Duplicate name " string-name)
				(System/exit DUPLICATENAME)
			)
		)
	)
)

(defn fill-strings [db version parsed-csv]
	(let [map-row (partial fill-row db version)]
		(doall (map map-row parsed-csv))
	)
)

(defn update-version [csv-string  db-url]
	(with-db-connection [db {:connection-uri db-url}]
		(let [parsed-csv (parse-csv csv-string)]
			(if (valid-names parsed-csv)
				(fill-strings db (make-version db) parsed-csv)
				(do
					(println "Invalid name: version")
					(System/exit BADNAME)
				)
			)
		)
	)
)

(defn -main
	"Update the database with a new version from a csv file"
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
			(update-version csv-string db-url)
			(System/exit SUCCESS)
		)
		(do 
			(println "Usage: csv-name db-name db-user db-password")
			(System/exit ARGUMENTERROR)
		)
	)
)

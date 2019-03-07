(ns fixedstrings.core
	(:gen-class)
	(:require [clojure.java.io :refer[writer]])
	(:require [clojure.java.jdbc :refer :all :as jdbc])
	(:use clojure.java.jdbc)
	(:require [clojure.string :as str])
)

(defn write-line [out-file row]
	(let
		[
			index-value (get row :index)
			string-name (get row :name)
		]
		(.write out-file (str string-name "=" (str index-value) "\n"))
	)
)

(defn write-definitions [db out-file version]
	(let
		[
			query-string "SELECT index,name FROM fixed_string WHERE version=? ORDER BY id;"
			row-list (query db [query-string version])
			write-row (partial write-line out-file)
		]

		(doall (map write-row row-list))
	)
)

(defn group-pair [out-elm out-clojure element]
	(let
		[
			constant-name (first element)
			value (second element)
		]
		(.write out-elm (str constant-name "=" value "\n"))
		(.write out-clojure (str "(def " constant-name " " value ")\n"))
	)
)

(defn write-group [out-elm out-clojure]
	(let
		[
			group-element (partial group-pair out-elm out-clojure)
			group-list (list (list "fixedString" 1))
		]

		(doall (map group-element group-list))
	)
)

(defn current-version [db]
	(let
		[
			query-string "SELECT MAX(version) AS version FROM fixed_string;"
			result (query db [query-string])
		]
		(get (first result) :version)
	)
)

(defn write-constants [elm-path clojure-path db-url]
	(with-db-connection [db {:connection-uri db-url}]
		(with-open [out-file (writer (str elm-path "/" "FixedStringsConstants.elm"))]
			(let [version (current-version db)]
	  			(.write out-file "module FixedStringsConstants exposing(..)\n")
	  			(.write out-file (str "version=" version "\n"))  			
	  			(write-definitions db out-file version)
			)
  		)
	)

	(with-open [out-elm (writer (str elm-path "/" "Group.elm"))]
  		(.write out-elm "module Group exposing(...)\n")
		
		(with-open [out-clojure (writer (str clojure-path "/" "group.clj"))]
	  		(.write out-clojure "(ns server.group)\n")
			(write-group out-elm out-clojure)
		)
	)
)

(defn -main
	"Write Elm and Clojure constant definitions"
	[& args]

	(if (= 5 (count args))
		(let
			[
				elm-path (nth args 0)
				clojure-path (nth args 1)
				db-name (nth args 2)
				db-user (nth args 3)
				db-password (nth args 4)
				db-url (str "jdbc:postgresql:" db-name "?user=" db-user "&password=" db-password)
			]
			(write-constants elm-path clojure-path db-url)
		)
		(println "Usage: elm-path clojure-path db-name db-user db-password")
	)
)

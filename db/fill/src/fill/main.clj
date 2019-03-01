(ns fill.main
  (:gen-class)
	(:require [clojure.java.jdbc :refer :all :as jdbc])
	(:use clojure.java.jdbc)
	(:require [clojure.java.io :as io])
	(:require [clojure.string :as str])
	(:require [clojure-csv.core :as csv])
	(:require [bcrypt-clj.auth :as auth])
)

(defn make-fill-row [db]
	(fn [row]
		(let 
			[
				member-name (nth row 0)
				password (auth/crypt-password (nth row 1))
				email (nth row 2)
				riding (Integer/parseInt (nth row 3))
			]
			(insert! db :members {:name member-name, :password password, :email email :riding riding})
		)
	)
)

(defn update-data [db-uri data]
	(with-db-connection [db {:connection-uri db-uri}]
		(let [fill-row (make-fill-row db)] 
			(doseq [row (csv/parse-csv data)] (fill-row row))
		)
	)
)

(defn get-uri [user db-name db-password]
	(str "jdbc:postgresql://localhost:5432/" db-name "?user=" user "&password=" db-password)
)

(defn -main
  "Fill members database"
  [& args]
  (if (== 4 (count args))
	  (let 
	  	[
	  		user (nth args 0)
	  		db-name (nth args 1)
	  		db-password (nth args 2)
	  		data (slurp (nth args 3))
	   	]
	   	(update-data (get-uri user db-name db-password) data)
	  )
	  (println "Usage: lein run user db_name db_password data_file")
	)
)

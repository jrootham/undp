(ns server.core
	(:gen-class)
	(:require [com.unbounce.encors :refer [wrap-cors]])
	(:use ring.adapter.jetty)
	(:require [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
         [ring.util.response :refer [response]])
	(:require [ring.middleware.params :refer [wrap-params]])
	(:require [compojure.core :refer :all] [compojure.route :as route])
	(:require [clojure.java.jdbc :refer :all :as jdbc])
	(:use clojure.java.jdbc)
	(:require [clojure.string :as str])
	(:require [ring-debug-logging.core :refer [wrap-with-logger]])
	(:require [clj-http.client :as client])
	(:require [bcrypt-clj.auth :refer [crypt-password]])
)

(defn return-error [message]
	{:status 400 :body message}
)

(defn reset [db user-name password nonce]
	(let 
		[
			query-string (str "SELECT id FROM members WHERE user_name=? AND nonce=?;")
			result (query db [query-string user-name nonce])
		]
		(if (= 1 (count result))
			(let 
				[
					member-id (get :id (first result))
					values {:password (crypt-password password) :nonce nil}
				]
				(update! db :members values ["id=?" member-id])
			)
			{:status 404 :body "User not found"}
		)
	)
)

(defn shutdown [saved-phrase given-phrase]
	(if (== 0 (compare saved-phrase given-phrase))
		(do 
			(println "Server shutting down")
			(System/exit 0)
		)
		(return-error "Bad body")
	)
)

(defroutes server-routes
	(POST "/reset" 
		[:as {db :connection {user-name "user-name" password "password" nonce "nonce"} :body}] 
		(reset db user-name password nonce))
	(POST "/shutdown" 
		[:as {saved-phrase :shutdown {given-phrase "shutdown"} :body}] 
		(shutdown saved-phrase given-phrase))
	(route/not-found {:status 404})
)

(defn cors [handler]
	(let [cors-policy
		    { 
		    	:allowed-origins :match-origin
				:allowed-methods #{:post :get}
				:request-headers #{"Accept" "Content-Type" "Origin"}
				:exposed-headers nil
				:allow-credentials? true
				:origin-varies? false
				:max-age nil
				:require-origin? true
				:ignore-failures? false
		    }
     	]

     	(wrap-cors handler cors-policy)
     )
)

(defn make-wrap-db [db-url]
	(fn [handler]  
		(fn [req]   
			(with-db-connection [db {:connection-uri db-url}]
				(handler (assoc req :connection db))
			)
		)
	)
)

(defn make-insert-shutdown [shutdown]
	(fn [handler]  
		(fn [req]   
			(handler (assoc req :shutdown shutdown))
		)
	)
)

(defn make-handler [db-url shutdown] 
	(let 
		[
			wrap-db (make-wrap-db db-url)
			insert-shutdown (make-insert-shutdown shutdown)
		] 
		(-> server-routes
			(wrap-db)
			(wrap-json-body)
			(wrap-json-response)
			(wrap-params)
			(insert-shutdown)
			(cors)
			(wrap-with-logger)
		)
	)
)

(defn -main
  	"NDP resolutions server"
  	[& args]
  	(if (== 5 (count args))
		(let 
			[
				port-string (nth args 0)
				db-name (nth args 1)
				db-user (nth args 2)
				db-password (nth args 3)
				shutdown (nth args 4)
			]
			(try
				(let 
					[
						port (Integer/parseInt port-string)
						url (str "jdbc:postgresql:" db-name "?user=" db-user "&password=" db-password)
					]
					(run-jetty (make-handler url shutdown) {:port port})
				)
				(catch NumberFormatException exception 
					(println (str port-string " is not an int"))
				)
			)
		)
	  	(println "Usage: port db-name db-user db-password shutdown-phrase")
	)
)

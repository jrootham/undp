(ns gen-html.core
	(:gen-class)
	(:require [clojure.java.io :refer[writer]])
	(:require [clojure.string :as str])
)

(defn generate [destination target title js-name elm-name]
	(with-open [out-file (writer destination)]
		(.write out-file "<!DOCTYPE html>\n")
		(.write out-file "<html>\n")
		(.write out-file "<head>\n")
		(.write out-file "<meta charset=\"UTF-8\">\n")
		(.write out-file "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n")
		(.write out-file (str "<title>" title "</title>\n"))
		(.write out-file (str "<script src=\"" js-name "\"></script>\n"))
		(.write out-file "</head>\n")
		(.write out-file "<body>\n")
		(.write out-file "<div id=\"elm\"></div>\n")
		(.write out-file "<script>\n")
		(.write out-file (str "var app = Elm." elm-name ".init(\n"))
		(.write out-file (str "{node: document.getElementById('elm'), flags: \"" target "\"});\n"))
		(.write out-file "</script>\n")
		(.write out-file "</body>\n")
		(.write out-file "</html>\n")
	)
)

(defn -main
	"Generate html to call elm"
	[& args]
	(if (= 2 (count args))
		(let 
	  		[
	  			destination (first args)
	  			target (second args)
	  		]
	  		(do
	  			(generate (str destination "/" "app.html") target "Prioritzation" "app.js" "App") 
	  		)
	  	)
	  	(println "Usage: lein run destination target")
	)
)
	
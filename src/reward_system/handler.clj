(ns reward_system.handler
  (:require [reward_system.core :as core]
            [reward_system.views.layout  :as layout])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

; (defn ranking []
;   (layout/common
;     [:h1 "Ranking:"]
;     [:h2 (print-str (core/test)) ]
;   ))

(defroutes app-routes
  (GET "/"         [] "Hello World")
  ; (GET "/ranking"  [] (ranking))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

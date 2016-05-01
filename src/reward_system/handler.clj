(ns reward_system.handler
  (:use ring.util.response
        ring.middleware.params)
  (:require [reward_system.core :as core]
            [reward_system.views.layout  :as layout]
            [reward_system.views.ranking :as view_ranking]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.anti-forgery :refer :all]))

(defn home []
  (apply layout/common (view_ranking/form_invitation)))

(defn show_ranking []
  "Shows ranking."
  (if (nil? (core/ranking))
    (layout/common
      [:h1 "Ranking is empty"] )
    (apply layout/common (view_ranking/build_table core/ranking))))

(defn reset
  "Resets server to empty graph."
  []
  (core/reset_all)
  (redirect "/"))

(defn invite
  "Adds node into graph."
  [params]
  (let [customer (get params :customer)
        friend (get params :friend)]
    (core/add_node customer friend)
    (layout/common
      [:h1 "Good one!"]
      [:h4 "Your invitation has been sent."]
      [:a.btn.btn-primary {:href "/"} "Click here to invite more friends"] )
  ))

(defroutes app-routes
  (GET "/"        [] (home))
  (GET "/ranking" [] (show_ranking))
  (GET "/reset"   [] (reset))
  (POST "/invite" {params :params} (invite params))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

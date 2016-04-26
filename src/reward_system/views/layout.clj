(ns reward_system.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to NuBank"]
     (include-css "/css/application.css")]
    [:body body]))

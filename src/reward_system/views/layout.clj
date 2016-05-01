(ns reward_system.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(defn common [& block]
  (html5
    [:head
      [:title "Bootstrapped Example"]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      (include-css "/css/bootstrap.css")
      (include-css "/css/jumbotron-narrow.css")
      (include-js  "/js/jquery.min.js")
      (include-js  "/js/bootstrap.min.js")
      (include-js  "/js/application.js")
    ]
    [:body
      [:div.container
        [:div.header.clearfix
          [:nav
            [:ul.nav.nav-pills.pull-right
              [:li {:role "presentation" :class "active"}
                [:a {:href "/"} "Home"]
              ]
              [:li {:role "presentation"}
                [:a {:href "/ranking"} "Ranking"]
              ]
              [:li {:role "presentation"}
                [:a {:href "/reset"} "Reset Invitations"]
              ]
            ]
          ]
          [:h3.text-muted "Reward System"]
        ]
        block
      ]
    ]
  ))

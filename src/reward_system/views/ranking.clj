(ns reward_system.views.ranking
  (:use ring.util.anti-forgery))

(defn build_table
  [ranking]
  [[:div {:class "panel panel-default"}
    [:div.panel-heading "Ranking"]
    [:table.table
      [:thead [:tr [:th "Customer"][:th "Score"]]]
      [:tbody (for [entry (ranking) ]
                [:tr [:td (first entry)][:td (second entry)]])
      ]
    ]
  ]])

(defn form_invitation
  []
  [
    [:h3 "Hello there!"]
    [:p "Invite your friends and get rewarded"]
    [:form.form-inline {:method "post" :action "/invite"}
    (anti-forgery-field)
    [:div.form-group
      [:label {:for "customer"}]
      [:input {:type "number" :min "0" :placeholder "customer" :class "form-control" :id "customer" :name "customer" :required true}]
    ]
    [:div.form-group
      [:label {:for "friend"}]
      [:input {:type "number" :min "0" :placeholder "friend" :class "form-control" :id "friend" :name "friend" :required true}]
    ]
    [:button {:type "submit" :class "btn btn-primary"} "Invite"]
  ]])

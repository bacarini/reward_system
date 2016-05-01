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
    [:div
      [:h5 "Invite your friends and get rewarded"]
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
      ]]
    [:div.upload_file
      [:h5 "Or you can upload a file with multi invitations ;)"]
      [:p "Just follow the example bellow:"]
      [:pre
        "1 2" \newline
        "1 3" \newline
        "3 4" \newline
        "2 4" \newline
        "4 5" \newline]
      [:p "File extension:" [:code ".txt"]]
      [:form.form-inline.upload_file {:action "/upload_file" :method "post" :enctype "multipart/form-data"}
        (anti-forgery-field)
        [:div.upload_file [:input {:name "file" :type "file" :size "20" :required true}]]
        [:input {:type "submit" :name "submit" :value "Send" :class "btn btn-primary"}]
      ]]
  ])

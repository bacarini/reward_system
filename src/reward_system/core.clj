(ns reward_system.core
  (:require
    [clojure.string       :as str]
    [clojure.set          :as s]
    [reward_system.matrix :as matrix]))

(def state
 ^{ :doc "Creates a new atom with graph and sorted-set that
     represents all customers that are already invited" }
  (atom {
    :graph nil
    :invited (sorted-set)
  }))

(defn reset_invited
  "Resets the invited list."
  []
  (swap! state assoc-in [:invited] (sorted-set)))

(defn reset_graph
  "Resets the graph."
  []
  (swap! state assoc-in [:graph]   nil))

(defn reset_all
  "Resets the atom."
  []
  (reset_invited)
  (reset_graph))

(defn select_predecessor
  "Selects the predecessor of specific node.

  Returns a node."
  [search]
  (last (filter (fn [node]
          (= search (get node :customer)))
          (@state :graph))))

(defn setting_value_to_matrix
  "It sets recursively value into matrix.
  Once given the node, this method finds all the predecessors and set value for them as well.

  Returns a matrix (a vector of vectors)"
  ([matrix node]
    (setting_value_to_matrix matrix node 0))
  ([matrix node score]
  (if-not (nil? (get node :predecessor))
      (if-not (and (= score 0) (contains? (@state :invited) (get node :customer)))
        (do
            (swap! state update-in [:invited] conj (get node :customer) )
            (setting_value_to_matrix (matrix/add_score matrix
                                        (- (Integer/parseInt (get node :customer)) 1)
                                        (- (Integer/parseInt (get node :friend)) 1)
                                        (- score 1))
                                     (select_predecessor (get node :predecessor))
                                     (+ 1 score)))
        matrix)
    (matrix/add_score matrix (- (Integer/parseInt (get node :customer)) 1)
                             (- (Integer/parseInt (get node :friend)) 1)
                             (- score 1)))))

(defn load_matrix
  "Loads matrix according to a given graph.

  Returns a matrix (a vector of vectors)"
  ([graph matrix]
  (if (= 1 (count graph))
    (setting_value_to_matrix matrix (first graph))
    (recur (rest graph) (setting_value_to_matrix matrix (first graph))))))

(defn find_predecessor
  "Finds the predecessor of a given value (:friend).

  Returns the predecessor (:customer)"
  [search]
  (for [ node (into [] (get @state :graph)) ]
    (if (= (get-in node [:friend]) search)
      (get-in node [:customer]))))

(defn add_node
  "It add node into a graph.

  Returns a node
  Ex. {:customer '2' :friend '3' :predecessor '1'}"
  [customer friend]
  (let [ node { :customer customer
                :friend friend
                :predecessor (last (remove nil? (find_predecessor customer)))
              }]
      (swap! state update-in [:graph] conj node)))

(defn split_line
  "Given a string with 2 ids, customer and friend in this context, splits into a list.

  Returns a list of ids
  Ex.('1' '3')"
  [line_str]
    (let [ids (re-seq #"\S+" line_str) ]
      (assert (= 2 (count ids)))
      ids))

(defn read_file
  "Reads file and returns a list of invitations.

  Ex. (('1' '3') ('1' '2') ('3' '4') ('2' '4') ('4' '5') ('4' '6'))"
  [file_name]
    (map #(split_line %) (str/split-lines (slurp file_name))))

(defn get_values_from_customer
  "Finds the score to a given customer."
  [customer matrix]
    (hash-map customer (matrix/sum_scores matrix (- (Integer/parseInt customer) 1))))

(defn all_involved
  "Returns everyone involved in the process of invitation,
  no matter if they are friends or customers"
  [graph]
  (s/union
    (set (into [] (map (fn [node] (get node :customer)) graph)))
    (set (into [] (map (fn [node] (get node :friend)) graph)))))

(defn ranking
  "Returns the customer ranking of invitations process."
  []
  (reset_invited)
  (if-not (nil? (@state :graph))
    (let [matrix (load_matrix (reverse (@state :graph))
                                 (matrix/create 6))]
        (sort-by val > (into {} (map #(get_values_from_customer % matrix) (all_involved (@state :graph)))))
        )))

(defn load_graph
  "Loads a graph from a list of given invitations.

  Does not return any value."
  [invitations]
  (reset_graph)
  (doseq [[customer friend] invitations]
    (add_node customer friend)))

(defn -main []
  (reset_all)
  (let [invitations (read_file "input.txt")
        count_customer (count (set (flatten invitations)))
      ]
    (load_graph invitations)
    (println (ranking))))

(ns reward_system.core
  (:require
    [clojure.string       :as str]
    [clojure.set          :as s]
    [reward_system.matrix :as matrix]))

(def state
  (atom {
    :graph nil
    :invited (sorted-set)
  }))

(defn select_predecessor
  [search]
  (last (filter (fn [node]
          (= search (get node :customer)))
          (@state :graph))))

(defn setting_value_to_matrix
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
  ([graph matrix]
  (if (= 1 (count graph))
    (setting_value_to_matrix matrix (first graph))
    (recur (rest graph) (setting_value_to_matrix matrix (first graph))))))

(defn find_predecessor
  [search]
  (for [ node (into [] (get @state :graph)) ]
    (if (= (get-in node [:friend]) search)
      (get-in node [:customer]))))

(defn build_hash
  [customer friend]
  (let [ node { :customer customer
                :friend friend
                :predecessor (last (remove nil? (find_predecessor customer)))
              }] node ))

(defn load_graph
  [invitations]
    (doseq [[customer friend] invitations]
      (swap! state update-in [:graph] conj (build_hash customer friend))))

(defn split_line
  [line_str]
    (let [ids (re-seq #"\S+" line_str) ]
      (assert (= 2 (count ids)))
      ids))

(defn read_file
  [file_name]
    (map #(split_line %) (str/split-lines (slurp file_name))))

(defn max_customer
  [invitations]
  (apply max (map #(Integer/parseInt %) (set (flatten invitations)))))

(defn get_values_from_customer
  [customer matrix]
    (hash-map customer (matrix/sum_scores matrix (- (Integer/parseInt customer) 1))))

(defn all_customers
  [graph]
  (s/union
    (set (into [] (map (fn [node] (get node :customer)) graph)))
    (set (into [] (map (fn [node] (get node :friend)) graph)))))

(defn ranking
  [matrix]
    (sort-by val >
      (into {} (map #(get_values_from_customer % matrix) (all_customers (@state :graph))))))

(defn reset
  []
  (swap! state assoc-in [:graph]   nil)
  (swap! state assoc-in [:invited] (sorted-set)))

(defn -main []
    (reset)
    (let [invitations (read_file "input.txt")
          max_customer (max_customer invitations)
        ]
      (load_graph invitations)
      (println (ranking
                  (load_matrix (reverse (@state :graph))
                     (matrix/create max_customer max_customer))))))

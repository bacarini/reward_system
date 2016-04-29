(ns reward_system.core
  (:require
    [clojure.string       :as str]
    [reward_system.matrix :as matrix]))

(def graph
  (atom { :graph nil }))

(defn load_matrix [graph matrix]
  (if (= 1 (count graph))
    (matrix/add_score matrix  (- (Integer/parseInt (get (first graph) :customer)) 1)
                              (- (Integer/parseInt (get (first graph) :friend)) 1)
                              (+ 1 1))
    (recur (rest graph) (matrix/add_score matrix (- (Integer/parseInt (get (first graph) :customer)) 1)
                                                 (- (Integer/parseInt (get (first graph) :friend)) 1)
                                                 (+ 1 1)))))

(defn find_predecessor
  [search]
  (for [ node (into [] (get @graph :graph)) ]
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
      (swap! graph update-in [:graph] conj (build_hash customer friend))))

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

(defn -main []
    (let [invitations (read_file "input.txt")
          max_customer (max_customer invitations)
        ]
      (load_graph invitations)
      (load_matrix (reverse (@graph :graph))
                   (matrix/create max_customer max_customer))))

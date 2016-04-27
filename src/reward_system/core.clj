(ns reward_system.core
  (:require
    [clojure.string       :as str]
    [reward_system.matrix :as matrix]))

(def graph
  (atom { :graph nil }))

(defn find_predecessor
  [search]
  (for [ node (into [] (get @graph :graph)) ]
    (if (= (get-in node [:friend]) search)
      (get-in node [:customer]))))

(defn build_hash
  [ids]
  (let [ node { :customer (first ids)
                :friend (second ids)
                :predecessor (last (remove nil? (find_predecessor (first ids))))
              }] node ))

(defn all_customers
  [array_str]
  (let [array (flatten
                (into []
                  (map (fn [line] (re-seq #"\S+" line)) array_str)))]
    (map #(Integer/parseInt %) array)))

(defn load_matrix
  [graph max_customer]
  (println \newline "graph nodes:" graph)
  (def m (matrix/create max_customer max_customer))
  (let [result-array
          (into [] (for [ii (range max_customer)]
            (into [] (for [jj (range max_customer)]
              (def m (matrix/add_score m ii jj (+ ii jj)))))))
  ] m ))

(defn load_graph
  [file-str]
  (let [file_lines (str/split-lines file-str) ]
    (def max_customer (apply max (set (all_customers file_lines))))
    (doseq [line_str file_lines]
      (let [ids      (re-seq #"\S+" line_str) ]
        (assert (= 2 (count ids)))
        (swap! graph update-in [:graph] conj (build_hash ids))))))

(defn -main []
  (let [text  (slurp "input.txt") ]
    (load_graph text)
    (let [matrix (load_matrix (@graph :graph) max_customer)]
      (println matrix))))



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

(defn load-graph
  [file-str]
  (let [file-lines (str/split-lines file-str) ]
    (doseq [line-str file-lines]
      (let [ids      (re-seq #"\S+" line-str) ]
        (assert (= 2 (count ids)))
        (swap! graph update-in [:graph] conj (build_hash ids))))))

(defn -main []
  (let [text  (slurp "input.txt") ]
    (load-graph text)
    (println \newline "graph nodes:" (@graph :graph))))

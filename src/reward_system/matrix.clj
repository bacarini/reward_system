(ns reward_system.matrix)

(defn create
    "Create a vector of vectors (matrix)."
    [rows
     columns]
 (into []
    (for [ index (range rows) ]
      (into [] (repeat columns 0)))))

(defn ** [x n]
  (reduce * (repeat n x)))

(defn add_score
  "Add a score into a Matrix."
  [ matrix i j new_valeu]
  (assoc-in matrix [i j] (** 0.5 new_valeu)))

(defn row
  "Returns the row of matrix."
  [ matrix i ]
  (get-in matrix [i]))

(defn sum_scores
  "Sum the values of the row of matrix."
  [ matrix i ]
  (reduce + (row matrix i)))

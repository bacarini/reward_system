(ns reward_system.core
  (:require
    [reward_system.matrix :as matrix]))

(defn test []
  (matrix/create 2 3))

(defn -main []
  (matrix/create 2 2))

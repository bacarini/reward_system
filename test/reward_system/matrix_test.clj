(ns reward_system.matrix_test
  (:use reward_system.matrix)
  (:use clojure.test))

(deftest matrix
  (testing "create"
    (let [m (create 2 2)]
      (is (= [[0 0] [0 0]] m))))

  (testing "**"
    (is (= 1 (** 0.5 0))))

  (testing "add_score"
    (let [m (create 2 2)
          new_m (add_score m 1 1 1)
      ]
      (is (= [[0 0] [0 0.5]] new_m))))

  (testing "row"
    (let [m (create 2 2)
          new_m (add_score m 1 1 0)
      ]
      (is (= [0 0] (row m 0)))
      (is (= [0 1] (row new_m 1)))))

  (testing "sum_scores"
    (let [m (create 2 2)
          new_m (add_score m 1 1 0)
      ]
      (is (= 0 (sum_scores m 0)))
      (is (= 1 (sum_scores new_m 1)))))
)

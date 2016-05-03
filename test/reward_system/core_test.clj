(ns reward_system.core_test
  (:use reward_system.core)
  (:use reward_system.matrix)
  (:use clojure.test))


(deftest core
  (testing "state"
    (is (= {:graph nil, :invited #{}} @state)))

  (testing "reset_invited"
    (swap! state update-in [:invited] conj "1")
    (is (= {:graph nil, :invited #{"1"} } @state))
    (is (= {:graph nil, :invited #{}} (reset_invited))))

  (testing "reset_graph"
    (swap! state update-in [:graph] conj {:customer "1"})
    (is (= {:graph '({:customer "1"}), :invited #{}} @state))
    (is (= {:graph nil, :invited #{}} (reset_graph))))

  (testing "reset_all"
    (swap! state update-in [:graph] conj {:customer "1"})
    (swap! state update-in [:invited] conj "1")
    (is (= {:graph '({:customer "1"}), :invited #{"1"}} @state))
    (is (= {:graph nil, :invited #{}} (reset_all))))

  (testing "select_predecessor"
    (swap! state update-in [:graph] conj {:customer "1" :friend "2"})
    (swap! state update-in [:graph] conj {:customer "2" :friend "3"})
    (testing "when there is a predecessor"
      (is (= {:customer "2", :friend "3"} (select_predecessor "2"))))
    (testing "when there is not a predecessor"
      (is (= nil (select_predecessor "3")))))

  (testing "find_predecessor"
    (reset_all)
    (swap! state update-in [:graph] conj {:customer "1" :friend "2"})
    (swap! state update-in [:graph] conj {:customer "2" :friend "3"})
    (testing "when there is a predecessor"
      (is (= "1" (find_predecessor "2"))))
    (testing "when there is not a predecessor"
      (is (= nil (find_predecessor "1")))))

  (testing "add_node"
    (reset_all)
    (testing "when insert a new node and there is not a predecessor"
      (add_node "1" "2")
      (is (= (@state :graph) '({:customer "1", :friend "2", :predecessor nil}))))
    (testing "when insert a new node and there is a predecessor"
      (add_node "2" "3")
      (is (= (@state :graph) '({:customer "2", :friend "3", :predecessor "1"}
                               {:customer "1", :friend "2", :predecessor nil}))))
    (testing "when insert a new node, but predecessor is the same as friend"
      (add_node "2" "1")
      (is (= (@state :graph) '({:customer "2", :friend "1", :predecessor nil}
                               {:customer "2", :friend "3", :predecessor "1"}
                               {:customer "1", :friend "2", :predecessor nil})))))
  (testing "split_line"
    (testing "when there are two ids"
      (is (= '("1" "3") (split_line "1 3"))))
    (testing "when there are not two ids"
      (is (thrown? AssertionError (split_line "1 3 3")))))

  (testing "get_values_from_customer"
    (is (= {"1" 6} (get_values_from_customer "1" [[1 5][0 0]]))))

  (testing "all_involved"
    (reset_all)
    (swap! state update-in [:graph] conj {:customer "1" :friend "2"})
    (swap! state update-in [:graph] conj {:customer "4" :friend "1"})
    (is (= #{"1" "2" "4"} (all_involved (@state :graph)))))

  (testing "count_customers"
    (reset_all)
    (testing "when there is not a node into the graph"
      (is (= 0 (count_customers (@state :graph)))))
    (testing "when there is a node into the graph"
      (swap! state update-in [:graph] conj {:customer "1" :friend "2"})
      (swap! state update-in [:graph] conj {:customer "4" :friend "1"})
      (is (= 4 (count_customers (@state :graph))))))

  (testing "load_graph"
    (testing "when there is a given list"
      (load_graph '(("10" "12") ("20" "31")))
      (is (= (@state :graph) '({:customer "20", :friend "31", :predecessor nil}
                               {:customer "10", :friend "12", :predecessor nil}))))
    (testing "when there is not a given list"
      (load_graph '())
      (is (= nil (@state :graph)))))

  (testing "load_matrix"
    (reset_all)
    (testing "when graph as predecessors"
      (swap! state update-in [:graph] conj {:customer "1" :friend "2" :predecessor nil})
      (swap! state update-in [:graph] conj {:customer "2" :friend "3" :predecessor "1"})
      (swap! state update-in [:graph] conj {:customer "3" :friend "4" :predecessor "2"})
      (is (= [[0 1.5 0 0][0 0 1 0][0 0 0 0][0 0 0 0]] (load_matrix (reverse (@state :graph)) (create 4)))))
    (reset_all)
    (testing "when graph as not a predecessor"
      (swap! state update-in [:graph] conj {:customer "1" :friend "2" :predecessor nil})
      (swap! state update-in [:graph] conj {:customer "2" :friend "1" :predecessor nil})
      (is (= [[0 0][0 0]] (load_matrix (reverse (@state :graph)) (create 2))))))

  (testing "setting_value_to_matrix"
    (testing "when there is no predecessor"
      (reset_all)
      (let [result (setting_value_to_matrix (create 2) {:customer "1" :friend "2" :predecessor nil})]
        (is (= [[0 0][0 0]] result))
        (is (= {:graph nil, :invited #{}} @state))))
    (testing "when there is a predecessor"
      (reset_all)
      (swap! state update-in [:graph] conj {:customer "1" :friend "2" :predecessor nil})
      (swap! state update-in [:graph] conj {:customer "2" :friend "3" :predecessor "1"})
      (let [result (setting_value_to_matrix (create 3) (first (@state :graph)))]
        (is (= [[0 1 0][0 0 0][0 0 0]] result))
        (is (= #{"2"} (@state :invited))))))

  (testing "ranking"
    (testing "when there is no data into the graph"
      (reset_all)
      (is (= nil (ranking))))
    (testing "when there is data into the graph"
      (swap! state update-in [:graph] conj {:customer "1" :friend "2" :predecessor nil})
      (swap! state update-in [:graph] conj {:customer "2" :friend "3" :predecessor "1"})
      (is (= '(["1" 1] ["3" 0] ["2" 0]) (ranking)))))
)

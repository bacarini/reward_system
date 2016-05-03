(ns reward_system.core_test
  (:use reward_system.core)
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
)

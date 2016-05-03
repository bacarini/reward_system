(ns reward_system.handler_test
  (:use reward_system.core
        ring.middleware.anti-forgery)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [reward_system.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Reward System"))
      (is (re-find #"invite" (:body response)))))

  (testing "ranking"
    (testing "when ranking is empty"
      (reset_all)
      (let [response (app (mock/request :get "/ranking"))]
        (is (= (:status response) 200))
        (is (re-find #"Ranking is empty" (:body response)))))
    (testing "when ranking is not empty"
        (add_node "1" "2")
        (let [response (app (mock/request :get "/ranking"))]
          (is (= (:status response) 200))
          (is (re-find #"Customer" (:body response)))
          (is (re-find #"Score" (:body response)))
          (not (re-find #"Ranking is empty" (:body response))))))

  (testing "reset"
    (reset_all)
    (add_node "1" "2")
    (is (= '({:customer "1", :friend "2", :predecessor nil}) (@state :graph)))
    (let [response (app (mock/request :get "/reset"))]
      (is (= (:status response) 302)))
      (is (= nil (@state :graph))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(ns reward_system.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [reward_system.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Reward System"))
      (is (re-find #"invite" (:body response)))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

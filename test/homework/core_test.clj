(ns homework.core-test
  (:require [homework.core :as core]
            [clojure.test :refer [deftest is testing]]))

(deftest test-diff
  (testing "Test no difference"
    (is (= (core/diff [{:id "a"}] [{:id "a"}]) [])))
  (testing "Test missing data"
    (is (= (core/diff [{:accounts/id "a"} {:accounts/id "b"}] [{:accounts/id "a"}])
           [{:accounts/id "b" :status :missing}])))
  (testing "Test corrupted data"
    (is (= (core/diff [{:accounts/id "a" :item "a"}] [{:accounts/id "a" :item "b"}])
           [{:accounts/id "a" :status :corrupt}])))
  (testing "Test new data"
    (is (= (core/diff [] [{:accounts/id "a"}])
           [{:accounts/id "a" :status :new}]))))

(ns skin.test
  (:require
    [skin.core :refer [render-block inspect] :refer-macros [<style>]])
  (:use 
    [clojure.test :only [deftest is testing run-tests]]))


(deftest var-binding
  (is (= true false)))


(run-tests)
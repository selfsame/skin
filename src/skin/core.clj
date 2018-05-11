(ns skin.core
  (:require [clojure.walk :as walk]))

(defn- datatype? [v] (or (sequential? v) (set? v) (map? v)))

(defn- rulestring [v] 
  (cond 
    (keyword? v) (name v) 
    (vector? v) (apply str (interpose " " (map rulestring v)))
    :else (str v)))

(defn- find-blocks [form]
  (map 
    (fn [[rule styles]]
      (list 'skin.core/render-block 
          (apply str (interpose ", " (map rulestring rule)))
          (first styles)))
    (partition 2 (partition-by map? form))))

(defn- block-walk [form]
  (cond (and (list? form) (= 'css (first form))) 
        `(~'str ~@(find-blocks (rest form)))
        (symbol? form)   form
        (datatype? form) (clojure.walk/walk block-walk identity form)
        :else form))

(defmacro <style> [kw & forms]
  (let [blocks (block-walk forms)]
   `(~'skin.core/register ~kw
      (~'fn [] 
        [:style {
          :id ~(str kw)
          :dangerouslySetInnerHTML {:__html (~'str ~@blocks)}} ]))))
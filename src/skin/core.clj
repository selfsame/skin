(ns skin.core
  (:require [clojure.walk :as walk]))

(defn- datatype? [v] (or (sequential? v) (set? v) (map? v)))

(defn- css-value [v]
  (cond (symbol? v) v
        (list? v) v
        (keyword? v) (apply str (rest (str v)))
        (number? v)  (str v)
        (vector? v) `(~'str ~@(interpose " " (map css-value v)))
        :else (str v)))

(defn- clj->css [col]
  (into {} (map  
    (fn [[k v]]
      [k (css-value v)])
    col)))

(defn- find-blocks [form]
  (map #(list 'skin.core/render-block 
          (apply str (interpose ", " (first %))) 
          (clj->css (first (last %))))
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
          :id ~(apply str (rest (str kw)))
          :dangerouslySetInnerHTML {:__html (~'str ~@blocks)}} ]))))
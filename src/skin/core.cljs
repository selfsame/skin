(ns skin.core
  (:require
    [reagent.core :as r]))

(defonce sheet-map (r/atom {}))

(defn- vectorize [v] (if (sequential? v) (vec v) [v]))

(defn- vec-wrap [v] 
  (let [v (vectorize v)]
    (if (vector? (first v)) v [v])))

(defn register 
  ([c] (do (swap! sheet-map assoc c (vec-wrap c))) true)
  ([k c] (do (swap! sheet-map assoc k (vec-wrap c)) true)))

(defn unregister 
  ([c] (do (swap! sheet-map dissoc c (vec-wrap c)) true))
  ([k c] (do (swap! sheet-map dissoc k (vec-wrap c)) true)))


(defn- render-value [v]
  (cond (keyword? v) (name v)
        (vector? v)  (apply str (interpose " " (map render-value v))) 
        :else        (str v)))

(defn render-block [rule m]
  (str "\n" rule " {" 
    (apply str (mapcat (fn [[k v]] 
      (interleave ["" (clj->js k) (render-value v)] 
                  ["\n  " ":" ";"])) m)) "}"))


(defn main [] (vec (cons :div (apply concat (vals @sheet-map)))))

(defn mount 
  ([]
    (mount (.-head js/document)))
  ([target] 
    (r/render-component [main] target)))

(defn inspect [k] 
  (when-let [f (k @sheet-map)]
    (println (str "\"" (get-in (((f 0) 0)) 
      [1 :dangerouslySetInnerHTML :__html]) "\"")) '*))

(defn- unitfn [u] (fn [s] (str s u)))
(def px (unitfn "px"))
(def em (unitfn "em"))


;TODO
'[parsing
  ((x) rule should handle :kw or [:rule1 "rule2"] collections)
  ((x) unified css parsing path)
  ((/) css macro should have a clearer spec
    :kw, symbol, string
    [:foo bar] "foo, bar"
    [[:foo bar] [baz :quz]] "foo bar, baz quz")
  (( ) verify alternate css options (like garden))]


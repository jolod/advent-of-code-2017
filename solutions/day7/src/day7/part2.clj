(ns day7.part2
  (:require [clojure.java.io :as io]))

(defn line->disc [line]
  (let [[parent & children] (re-seq #"[^\W\d]+" line)
        weight (Long. (re-find #"\d+" line))]
    [parent {:children children :weight weight}]))

(defn find-root [tower]
  (let [child->parent (into {}
                            (for [[parent {:keys [children]}] tower
                                  child children]
                              [child parent]))]
    (->> child->parent
         ffirst
         (iterate child->parent)
         (take-while some?)
         last)))

(defn subtower-weight [tower disc-name]
  (let [{:keys [children weight]} (get tower disc-name)]
    (apply + weight (map #(subtower-weight tower %) children))))

(defn find-weight-offset [tower disc-name]
  (when-let [children (seq (get-in tower [disc-name :children]))]
    (let [child-weights (map (partial subtower-weight tower) children)
          [odd-one the-rest & nothing] (->> child-weights frequencies (sort-by val) (map key))]
      (assert (not (seq nothing)))
      (- odd-one the-rest))))

(defn find-odd-branch [tower disc-name]
  (when-let [children (seq (get-in tower [disc-name :children]))]
    (let [branch-by-weight (group-by (partial subtower-weight tower) children)]
      (case (count branch-by-weight)
        1 nil
        2 (->> branch-by-weight vals (sort-by count) ffirst)
        (assert false)))))

(defn find-odd-disc [tower disc-name]
  (->> disc-name
       (iterate (partial find-odd-branch tower))
       (take-while some?)
       last))

(defn -main [& [debug]]
  (let [input (line-seq (java.io.BufferedReader. *in*))
        tower (into {} (map line->disc input))
        root (find-root tower)
        weight-offset (find-weight-offset tower root)
        disc-name (find-odd-disc tower root)
        disc-weight (get-in tower [disc-name :weight])]
    (when debug
      (println "Weight offset:" weight-offset)
      (println "Bad disc:" disc-name)
      (println "Current disc weight:" disc-weight))
    (println (- disc-weight weight-offset))))

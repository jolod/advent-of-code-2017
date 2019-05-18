(ns day7.part1
  (:require [clojure.java.io :as io]))

(defn -main []
  (let [input (line-seq (java.io.BufferedReader. *in*))
        input' (for [line input
                     :let [[parent & children] (re-seq #"[^\W\d]+" line)]]
                 [parent children])
        child->parent (into {}
                            (for [[parent children] input'
                                  child children]
                              [child parent]))
        node (ffirst input')
        root (->> node
                  (iterate child->parent)
                  (take-while some?)
                  last)]
    (println root)))

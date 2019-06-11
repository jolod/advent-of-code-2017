(ns day2.part2
  (:require [clojure.string :as str]))

(defn enumerate [xs]
  (map-indexed vector xs))

(defn -main []
  (println
    (apply +
      (for [line (line-seq (java.io.BufferedReader. *in*))
            :let [numbers (enumerate (map #(Long. %) (str/split line #"\s+")))]
            [i a] numbers
            [j b] numbers
            :when (and (not= i j) (zero? (rem a b)))]
        (/ a b)))))

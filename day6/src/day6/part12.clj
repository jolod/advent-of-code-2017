(ns day6.part12
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn enumerate [xs]
  (map vector (range) xs))

(defn max-index [v]
  (first
   (reduce
    (fn [[max-index max-value :as champion] [index value :as contender]]
      (if (> value max-value)
        contender
        champion))
    (enumerate v))))

(defn take-while-unique [coll]
  (->> coll
       (reductions
        (fn [[seen _] value]
          (when-not (seen value)
            [(conj seen value) value]))
        [#{} nil])
       rest
       (take-while some?)
       (map second)))

(defn distribute [banks i n]
  (if (<= n 0)
    banks
    (recur
     (update banks (rem i (count banks)) inc)
     (inc i)
     (dec n))))

(defn redistribute [banks]
  (let [banks (vec banks)
        i (max-index banks)]
    (distribute
     (assoc banks i 0)
     (inc i)
     (banks i))))

(defn reallocation-routine [banks]
  (iterate redistribute banks))

(def banks' [5	1	10	0	1	7	13	14	3	12	8	10	7	12	0	6])

(defn -main []
  (let [input (line-seq (java.io.BufferedReader. *in*))
        banks (map #(Long. %) (str/split (first input) #"\s+"))
        _ (assert (= banks banks'))
        process (reallocation-routine banks)
        n (count (take-while-unique (reallocation-routine banks)))
        process (drop n process)]
    (println n)
    (println (count (take-while-unique process)))))

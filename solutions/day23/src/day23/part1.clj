(ns day23.part1
  (:require [clojure.string :as str])
  (:import [java.io BufferedReader StringReader]))

(defn new-computer [instructions]
  {:n 0
   :instructions (vec instructions)
   :registers (into {}
                    (for [register (filter string? (apply concat instructions))]
                      [(str register) 0]))})

(defn step [computer]
  (if-let [instruction (get-in computer [:instructions (:n computer)])]
    (let [computer (update computer :n inc)

          jump #(update computer :n + (dec %))
          do-nothing (constantly computer)
          value (fn [a]
                  (condp = (type a)
                    Long a
                    String (get-in computer [:registers a] 0)))
          update-register (fn [register f & args]
                             (apply update-in computer [:registers register] (fnil f 0) args))

          write (fn [_ x] x)

          [op a b] instruction
          computer' (case op
                      :set (update-register a write (value b))
                      :sub (update-register a - (value b))
                      :mul (update-register a * (value b))
                      :jnz (if (not (zero? (value a)))
                             (jump (value b))
                             (do-nothing)))]
      [computer' op])))

(defn maybe-long [x]
  (try
    (Long. x)
    (catch Exception e
      nil)))

(defn -main []
  (let [lines (line-seq (BufferedReader. *in*))
        instructions (for [line lines]
                       (let [[op a b] (str/split line #" ")]
                         [(keyword op)
                          (or (maybe-long a) a)
                          (or (maybe-long b) b)]))
        computer (new-computer instructions)
        computers (iterate
                   (comp step first)
                   [computer nil])]
    (->> computers
         (take-while some?)
         (map second)
         frequencies
         :mul
         println)))

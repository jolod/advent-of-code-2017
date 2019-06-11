(ns day18.part1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn step [computer]
  (if-let [instruction (get-in computer [:instructions (:n computer)])]
    (let [computer (update computer :n inc)
          computer (assoc computer :effect instruction)

          jump #(update computer :n + (dec %))
          do-nothing #(assoc computer :effect nil)
          value (fn [a]
                  (condp = (type a)
                    Long a
                    String (get-in computer [:registers a] 0)))
          update-register' (fn [computer register f & args]
                             (apply update-in computer [:registers register] (fnil f 0) args))
          update-register (partial update-register' computer)
          set-frequency #(assoc computer :frequency %)
          current-frequency (get computer :frequency)

          write (fn [_ x] x)

          [op a b] instruction]
      (case op
        :set (update-register a write (value b))
        :add (update-register a + (value b))
        :mul (update-register a * (value b))
        :mod (update-register a mod (value b))
        :snd (set-frequency (value a))
        :rcv (if (zero? (value a))
               (do-nothing)
               (update-register a write current-frequency))
        :jgz (if (pos? (value a))
               (jump (value b))
               (do-nothing))))))

(defn parse-line [line]
  (let [literal-or-register #(or (try
                                   (Long. %)
                                   (catch Exception e))
                                 %)
        [op a b] (str/split line #"\s+")]
    [(keyword op) (literal-or-register a) (literal-or-register b)]))

(defn -main []
  (let [input (line-seq (java.io.BufferedReader. *in*))
        instructions (map parse-line input)
        computer {:frequency 0
                  :n 0
                  :instructions (vec instructions)
                  :registers {}}
        after-rcv (->> computer
                       (iterate step)
                       (drop-while #(not= :rcv (first (:effect %))))
                       first)
        register (-> after-rcv :effect second)
        value (get-in after-rcv [:registers register])]
    (println value)))

(ns day18.part2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn step [computer]
  (if-let [instruction (get-in computer [:instructions (:n computer)])]
    (let [computer (update computer :n inc)
          queue (get computer :queue)

          continue #(vector :continue %)
          snd #(vector :send computer %)
          wait (vector :wait)

          jump #(continue (update computer :n + (dec %)))
          do-nothing #(continue computer)
          value (fn [a]
                  (condp = (type a)
                    Long a
                    String (get-in computer [:registers a] 0)))
          update-register' (fn [computer register f & args]
                             (continue (apply update-in computer [:registers register] (fnil f 0) args)))
          update-register (partial update-register' computer)

          write (fn [_ x] x)

          [op a b] instruction]
      (case op
        :set (update-register a write (value b))
        :add (update-register a + (value b))
        :mul (update-register a * (value b))
        :mod (update-register a mod (value b))
        :jgz (if (pos? (value a))
               (jump (value b))
               (do-nothing))
        :snd (snd (value a))
        :rcv (if-let [[x & xs] (seq queue)]
               (-> computer
                   (assoc :queue (vec xs))
                   (update-register' a write x))
               wait)))
    [:stop]))

(defn boot [system instructions]
  (let [id (count system)
        computer {:n 0
                  :instructions (vec instructions)
                  :queue []
                  :id id
                  :registers {"p" id}}]
    (assoc system id computer)))

(def empty-queue
  clojure.lang.PersistentQueue/EMPTY)

(defn run [system]
  (let [schedule {:running (keys system)
                  :waiting []}
        next-id #(mod (inc %) (count system))]
    (iterate
     (fn [[[system running waiting] _]]
       (if-let [id (first running)]
         (let [ret (step (get system id))
               inter #(vector [%1 %2 %3] ret)]
           (case (first ret)
             :stop (inter system
                          (pop running)
                          waiting)
             :continue (inter (-> system (assoc id (second ret)))
                              (conj (pop running) id)
                              waiting)
             :wait (inter system
                          (pop running)
                          (conj waiting id))
             :send (let [[_ computer value] ret]
                     (inter (-> system
                                (assoc id computer)
                                (update-in [(next-id id) :queue] conj value))
                            (-> empty-queue
                                (into waiting)
                                (into (pop running))
                                (conj id))
                            []))))
         (if (empty? waiting)
           [[system running waiting] [:done]]
           [[system running waiting] [:deadlock]])))
     [[system
       (into empty-queue (sort (keys system)))
       []]
      nil])))

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
        system (-> {} (boot instructions) (boot instructions))]
    (->> system
         run
         rest
         (map second)
         (take-while #(not (#{:done :deadlock} (first %))))
         (filter #(and (= :send (first %))
                       (= 1 (:id (second %)))))
         count
         prn)))

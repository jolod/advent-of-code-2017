(ns day25.part1
  (:require [clojure.string :as str])
  (:import [java.io BufferedReader StringReader]))

(defn init-machine [rules state]
  {:tape {}
   :cursor 0
   :state state
   :rules rules})

(defn step [machine]
  (let [state (:state machine)
        cursor (:cursor machine)
        value (-> machine (get :tape) (get cursor 0))
        rule (-> machine (get :rules) (get state) (get value))
        _ (assert rule {:rule rule :state state :value value machine :machine})]
    (-> machine
        (assoc-in [:tape cursor] (:value rule))
        (update :cursor + (:cursor-delta rule))
        (assoc :state (:state rule)))))

(defn checksum [machine]
  (apply + (vals (:tape machine))))

(defn parse-header [header]
  (if-let [[_ state steps] (re-find
                            #"Begin in state (\w+).\nPerform a diagnostic checksum after (\d+) steps."
                            header)]
    [state (Long. steps)]
    (-> "Failed to parse header"
        Exception.
        throw)))

(defn parse-rule [rule]
  (if-let [[_ state zero-write zero-move zero-state one-write one-move one-state] (re-find
                                                                                   #"In state (\w+):
  If the current value is 0:
    - Write the value ([01]).
    - Move one slot to the (left|right).
    - Continue with state (\w+).
  If the current value is 1:
    - Write the value ([01]).
    - Move one slot to the (left|right).
    - Continue with state (\w+)."
                                                                                   rule)]
    (let [action (fn [write move state]
                   {:value (Long. write)
                    :cursor-delta (case move
                                    "left" -1
                                    "right" 1)
                    :state state})]
      [state [(action zero-write zero-move zero-state) (action one-write one-move one-state)]])
    (-> "Failed to parse rule"
        Exception.
        throw)))

(defn -main []
  (let [input (str/join "\n" (line-seq (BufferedReader. *in*)))
        [header & rules] (str/split input #"\n\n")
        [init-state steps]  (parse-header header)
        rules (into {} (map parse-rule rules))
        machines (iterate step (init-machine rules init-state))]
    (println (checksum (nth machines steps)))))

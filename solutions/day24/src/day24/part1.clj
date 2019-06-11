(ns day24.part1
  (:require [clojure.string :as str])
  (:import [java.io BufferedReader StringReader]))

(defn parse-line [line]
  (map #(Long. %) (str/split line #"/")))

(defn empty-bridge [connector]
  {:components []
   :connector connector
   :strength 0})

(defn add-component [bridge a b]
  (assert (= a (:connector bridge)))
  (-> bridge
      (update :components conj [a b])
      (update :strength + a b)
      (assoc :connector b)))

(defn connect [bridge a b]
  (condp = (:connector bridge)
    a (add-component bridge a b)
    b (add-component bridge b a)
    nil))

(defn add-to-index [index component]
  (let [[a b] (sort component)]
    (-> index
        (update a (fnil conj #{}) b)
        (update b (fnil conj #{}) a))))

(defn remove-from-index [index component]
  (let [[a b] (sort component)]
    (-> index
        (update a disj b)
        (update b disj a))))

(defn max-bridge [bridge index]
  (let [connector (:connector bridge)
        candidates (get index connector)]
    (apply max-key
           :strength
           (cons bridge
                 (for [candidate candidates
                       :let [new-bridge (connect bridge connector candidate)]
                       :when new-bridge]
                   (max-bridge new-bridge (remove-from-index index [connector candidate])))))))

(defn -main [& [debug]]
  (let [lines (line-seq (BufferedReader. *in*))
        components (map (comp sort parse-line) lines)
        index (reduce add-to-index {} components)
        bridge (max-bridge (empty-bridge 0) index)]
    (when debug
      (println bridge))
    (println (:strength bridge))))

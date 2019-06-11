(ns day24.part2
  (:require [clojure.string :as str])
  (:import [java.io BufferedReader StringReader]))

(defn parse-line [line]
  (map #(Long. %) (str/split line #"/")))

(defn empty-bridge [connector]
  {:components []
   :connector connector
   :strength 0})

(defn strength [bridge]
  ; This is just for demo purposes. 
  (apply + (apply concat (:components bridge))))

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

; (defn max-by [f & coll]
;   (last (sort-by f coll)))

(defn max-by [f & coll]
  (when-let [[x & xs] (seq coll)]
    (first
     (reduce
      (fn [[max key :as old] y]
        (let [k (f y)]
          (if (> (compare k key) 0)
            [y k]
            old)))
      [x (f x)]
      xs))))

(defn max-bridge [bridge index]
  (let [connector (:connector bridge)
        candidates (get index connector)]
    (apply max-by
           (juxt (comp count :components) strength)
           (cons bridge
                 (for [candidate candidates
                       :let [new-bridge (connect bridge connector candidate)]
                       :when new-bridge]
                   (max-bridge new-bridge (remove-from-index index [connector candidate])))))))

(defn -main [& [debug]]
  (let [lines (line-seq (BufferedReader. *in*))
        components (map (comp sort parse-line) lines)
        index (reduce add-to-index {} components)
        t0 (System/currentTimeMillis)
        bridge (max-bridge (empty-bridge 0) index)]
    (when debug
      (println (/ (- (System/currentTimeMillis) t0) 1000.0))
      (println bridge))
    (println (:strength bridge))))

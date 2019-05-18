(ns day21.part12
  (:require [clojure.string :as str])
  (:import [java.io BufferedReader]))

(defn flip-ud [square]
  (reverse square))

(defn flip-lr [square]
  (map reverse square))

(defn transpose [square]
  (apply map vector square))

(defn rotate-ccw [square]
  (-> square transpose flip-ud))

(defn expand [square]
  (reduce
   (fn [all square]
     (conj all square (flip-ud square) (flip-lr square)))
   #{}
   (take 4 (iterate rotate-ccw square))))

(defn parse-square [s]
  (map #(map {\# true, \. false} %)
       (str/split s #"/")))

(defn parse-rule [line]
  (let [[from to] (map parse-square (str/split line #" => "))]
    (into {}
          (for [from (expand from)]
            [from to]))))

(defn divide [square factor]
  (->> square
       (map #(partition factor %))
       (partition factor)
       (map #(apply map vector %))
       (apply concat)
       (partition (/ (count square) factor))))

(defn join [square]
  (let [square' (map #(->> %
                           transpose
                           (apply concat)
                           (apply concat)) square)
        factor (count (ffirst square))]
    (partition (* (count square) factor) (apply concat square'))))

(defn apply-rule [divided-square rulebook]
  (for [row divided-square]
    (map rulebook row)))

(defn step [square rulebook]
  (let [factor (if (even? (count square))
                 2
                 3)]
    (-> square
        (divide factor)
        (apply-rule rulebook)
        join)))

; (defn render [square]
;   (doseq [row square]
;     (doseq [col row]
;       (print ({true "#" false "."} col)))
;     (println)))

(defn -main [& [debug]]
  (let [lines (line-seq (BufferedReader. *in*))
        rulebook (reduce merge {} (map parse-rule lines))
        init-square (parse-square ".#./..#/###")
        iter (atom 0)
        squares (iterate #(do (when debug
                                (binding [*out* *err*]
                                  (println (swap! iter inc))))
                              (step % rulebook))
                         init-square)
        on-count #(count (filter identity (flatten %)))]
    (println (on-count (nth squares 5)))
    (println (on-count (nth squares 18)))))

(ns day20.part2
  (:require [clojure.set :as set])
  (:require [clojure.math.numeric-tower :refer [round]])
  (:import [java.io BufferedReader]))

(defn pow2 [x]
  (* x x))

(defn real-roots2 [a b c]
  (if (zero? a)
    (if (zero? b)
      (if (zero? c)
        nil
        [])
      [(- (/ c b))])
    (let [p (/ b a)
          q (/ c a)
          p-half (/ p 2)
          discriminant (- (pow2 p-half) q)]
      (cond
        (pos? discriminant)
        [(- (- p-half) (Math/sqrt discriminant))
         (+ (- p-half) (Math/sqrt discriminant))]

        (zero? discriminant)
        [(- p-half)]

        (neg? discriminant)
        []))))

(defn poly [p t]
  (apply + (map * (reverse p) (iterate #(* % t) 1))))

(defn nat-roots2 [a b c]
  (when-let [roots (real-roots2 a b c)]
    (->> roots
         (filter (complement neg?))
         (map round)
         (filter #(zero? (poly [a b c] %)))
         vec)))

(defn position-poly [a v p]
  [(/ a 2) (+ v (/ a 2)) p])

(defn poly-minus [p1 p2]
  (map - p1 p2))

(defn poly-zero? [p]
  (apply (every-pred zero?) p))

(defn collision1d [p1 p2]
  (let [diff (poly-minus
              (apply position-poly p1)
              (apply position-poly p2))]
    (when-not (poly-zero? diff)
      (apply nat-roots2 diff))))

(defn collision3d [p1 p2]
  (let [nss (seq
             (for [k [:x :y :z]
                   :let [ns (collision1d (k p1) (k p2))]
                   :when (some? ns)]
               (set ns)))]
    (assert (seq nss) {:msg "Particles have exactly the same trajectory"
                       :p1 p1
                       :p2 p2})
    (when-let [ns (seq (apply set/intersection nss))]
      (apply min ns))))

(defn singleton-set [x]
  (set [x]))

(defn all-pairs-half
  "Returns all combinations of values in xs. If x and x' are elements in xs then only one of [x x'] and [x' x] will be included.
  Also, the ''diagonal'' is not included, i.e. [x x] is not included."
  [xs]
  (for [[p & ps] (take-while some? (iterate next (seq xs)))
        p' ps]
    [p p']))

(defn -main []
  (let [lines (line-seq (BufferedReader. *in*))
        particles (set
                   (for [line lines]
                     (let [[px py pz vx vy vz ax ay az] (map #(Long. %) (re-seq #"[-\d]+" line))]
                       {:x [ax vx px]
                        :y [ay vy py]
                        :z [az vz pz]})))
        collisions (for [particle-pair (all-pairs-half particles)
                         :let [n (apply collision3d particle-pair)]
                         :when n]
                     [n particle-pair])
        collisions (->> collisions
                        (map (fn [[n ps]] {n (singleton-set ps)}))
                        (apply merge-with set/union)
                        (sort-by key)
                        (map val))
        surviving-particles (reduce
                             (fn [particles pairs]
                               (apply disj particles
                                      (for [[p1 p2] pairs
                                            :when (and (particles p1) (particles p2))
                                            p [p1 p2]]
                                        p)))
                             particles
                             collisions)]
    (println (count surviving-particles))))

(ns day11.part12
  (:require [clojure.string :as str]))

(defn sign
  "Sign is defined as positive for zero. Useful below."
  [x]
  (if (neg? x)
    -1
    1))

; Due to symmetry, there's really only three interesting cases: ppp, ppn, and
; npn, where p stands for positive or zero and n for negative.
;
; First I invert everything where straight is negative. Then I want to get left
; positive, which is easy if right is positive; since I just need to swap left
; and right.
;
; For case ppp it's simple. A left and a right is equivalent to one straight.
;
; For case ppn is gets slightly complicated. If the negative (right) part has a
; smaller magnitude than the straight part, then the negative part doesn't
; affect the distance. If it is larger in magnitude then it adds to the distance
; by how much "longer" it is than the straight part. Consider [3 0 -3], which is
; clearly 6 steps away from the origin. For case npn we essentially have ppp
; except a negative left and a negative right should subtract from straight. We
; are transported to case ppn or pnn, depending on how much negative left and
; right are. ppn we handle directly, while pnn will be turned into npp which
; will turn into ppn which we again handle.
;
; So in the end we only have two base cases, ppp and ppn, but we need to do a
; non-trivial computation for case npn on the way.
(defn hex-dist' [left straight right]
  (let [invert (fn [] (hex-dist' (- left) (- straight) (- right)))
        mirror (fn [] (hex-dist' right straight left))]
    (case (map sign [left straight right])
      [ 1  1  1] (let [[a b] (sort [left right])
                       straight (+ straight a)
                       sideways (- b a)]
                   (+ straight sideways))
      [ 1  1 -1] (+ left straight (max 0 (- (- right) straight)))
      [-1  1  1] (mirror)
      [-1  1 -1] (let [[a b] (sort (map - [left right]))]
                   (recur 0 (- straight a) (- a b)))
      [-1 -1 -1] (invert)
      [-1 -1  1] (invert)
      [ 1 -1 -1] (invert)
      [ 1 -1  1] (invert))))

; It does not matter in which order you make the moves when calculating the
; distance, and since
;   * :south cancels a :north,
;   * :south-east cancels a :north-west, and
;   * :south-west cancels a :north-east,
; what we really need is only three (potentially negative) variables:
; :north, north-west, and north-east. Or, a bit more neutral: straight, left,
; and right.
(defn hex-dist [freqs]
  (let [straight (- (:north freqs 0) (:south freqs 0))
        left (- (:north-west freqs 0) (:south-east freqs 0))
        right (- (:north-east freqs 0) (:south-west freqs 0))]
    (hex-dist' left straight right)))

(defn -main []
  (let [input (.readLine (java.io.BufferedReader. *in*))
        moves (for [s (str/split input #",")]
                (case s
                  "n"  :north
                  "ne" :north-east
                  "nw" :north-west
                  "s"  :south
                  "sw" :south-west
                  "se" :south-east))
        freqs-seq (reductions #(update %1 %2 (fnil inc 0)) {} moves)
        distances (map hex-dist freqs-seq)]
    (println (last distances))
    (println (apply max distances))))

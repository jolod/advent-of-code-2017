(ns day3.part2)

(def delta
  {:right [0 +1]
   :upper [-1 0]
   :left  [0 -1]
   :lower [+1 0]})

(defn neighbors [[x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not (and (zero? dx) (zero? dy)))]
    [(+ x dx) (+ y dy)]))

(defn plus-tuple [[a b] [x y]]
  [(+ a x) (+ b y)])

(defn ring-coords [radius]
  (let [side-length (* 2 radius)
        zero [radius (- radius)]]
    (rest
      (reductions
        plus-tuple
        zero
        (concat
          (repeat side-length (:right delta))
          (repeat side-length (:upper delta))
          (repeat side-length (:left  delta))
          (repeat side-length (:lower delta)))))))

(defn sum-neighbors [m coord]
  (apply + (filter some? (map m (neighbors coord)))))

(defn -main [input]
  (println
    (let [input (Long. input)
          init {[0 0] 1}
          coords (mapcat ring-coords (rest (range)))]
      (->> coords
        (reductions
          (fn [[m _] coord]
            (let [value (sum-neighbors m coord)]
              [(assoc m coord value)
               value]))
          [init 1])
        (map second)
        (drop-while #(<= % input))
        first))))

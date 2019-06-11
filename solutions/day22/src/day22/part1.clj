(ns day22.part1
  (:import [java.io BufferedReader]))

(defn new-ant [[x y] direction]
  {:position [x y]
   :direction direction})

(defn turn-right [ant]
  (update ant :direction #(case %
                            :up :right
                            :right :down
                            :down :left
                            :left :up)))

(defn turn-left [ant]
  (update ant :direction #(case %
                            :up :left
                            :left :down
                            :down :right
                            :right :up)))

(defn move-ant [ant]
  (let [[dx dy] (case (:direction ant)
                  :right [ 1  0]
                  :left  [-1  0]
                  :up    [ 0 -1]
                  :down  [ 0  1])]
    (-> ant
        (update-in [:position 0] + dx)
        (update-in [:position 1] + dy))))

(defn flip [board position]
  (update board position (fnil inc 0)))

(defn init-board [infected]
  (reduce flip {} infected))

(defn infected? [board position]
  (odd? (get board position 0)))

(defn burst [system]
  (let [ant (:ant system)
        board (:board system)
        turn (if (infected? board (:position ant))
               turn-right
               turn-left)
        infected (if (infected? board (:position ant))
                   0
                   1)]
    (-> system
      (update :infections (fnil + 0) infected)
      (update :board flip (:position ant))
      (update :ant (comp move-ant turn)))))

(defn infected-count [board]
  (reduce-kv
    (fn [m k v]
      (assoc m k (int (java.lang.Math/ceil (/ v 2)))))
    {}
    board))

(defn enumerate [xs]
  (map-indexed vector xs))

(defn -main []
  (let [lines (line-seq (BufferedReader. *in*))
        ; lines ["..#"
        ;       "#.."
        ;       "..."]
        midway (int (/ (count lines) 2))
        infected (for [[y line] (enumerate lines)
                       [x cell] (enumerate (seq line))
                       :when (= \# cell)]
                   [(- x midway) (- y midway)])
        board (init-board infected)

        system {:board board
                :ant (new-ant [0 0] :up)}
        new-system (->> system
                     (iterate burst)
                     (drop 10000)
                     first)
        new-board (:board new-system)]
    (println
      (apply +
        (vals
          (merge-with -
            (infected-count new-board)
            (infected-count board)))))))

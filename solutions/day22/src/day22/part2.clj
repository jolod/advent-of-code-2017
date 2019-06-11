(ns day22.part2
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

(defn turn-around [ant]
  (update ant :direction #(case %
                            :up :down
                            :down :up
                            :right :left
                            :left :right)))

(defn turn-ant [ant state]
  (let [turn (case state
               nil turn-left
               :clean turn-left
               :weakened identity
               :infected turn-right
               :flagged turn-around)]
    (turn ant)))

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
  ; Clean nodes become weakened.
  ; Weakened nodes become infected.
  ; Infected nodes become flagged.
  ; Flagged nodes become clean.  
  (update board position #(case %
                            nil :weakened
                            :clean :weakened
                            :weakened :infected
                            :infected :flagged
                            :flagged :clean)))

(defn init-board [infected]
  (into {}
    (for [position infected]
      [position :infected])))

(defn infected? [board position]
  (= (get board position) :infected))

(defn bool->bit [b]
  (if b 1 0))

(defn burst [system]
  (let [ant (:ant system)
        board (:board system)
        position (:position ant)
        state (get-in system [:board position])
        count-infections (fn [system]
                           (update system :infections + (bool->bit (infected? (:board system) (:position ant)))))]
    (-> system
      (update :board flip (:position ant))
      count-infections
      (update :ant turn-ant state)
      (update :ant move-ant))))

(defn infected-count [board]
  (reduce-kv
    (fn [m k v]
      (assoc m k (int (java.lang.Math/ceil (/ v 2)))))
    {}
    board))

(defn new-system [board]
  {:board board
   :ant (new-ant [0 0] :up)
   :infections 0})

(defn enumerate [xs]
  (map-indexed vector xs))

(defn take-while-unique [coll]
  (->> coll
    (reductions
      (fn [[seen _] value]
        (when-not (seen value)
          [(conj seen value) value]))
        [#{} nil])
    rest
    (take-while some?)
    (map second)))

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
        
        system (new-system board)
        new-system (->> system
                     (iterate burst)
                     (drop 10000000)
                     first)
        ]
    (println
      (-
        (:infections new-system)
        (:infections system)))))

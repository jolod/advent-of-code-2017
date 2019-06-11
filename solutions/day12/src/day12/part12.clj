(ns day12.part12
  (:import [java.io BufferedReader]))

(defn reachable [connections start]
  (loop [q [start]
         visited #{}]
    (if-let [[head & tail] (seq q)]
      (if (visited head)
        (recur
          tail
          visited)
        (recur
          (into tail (get connections head))
          (conj visited head)))
      visited)))

(defn remove-one-group [connections]
  (apply dissoc connections (reachable connections (ffirst connections))))

(defn -main []
  (let [lines (line-seq (BufferedReader. *in*))
        connections (reduce
                      (fn [m [k v]]
                        (update m k (fnil conj #{}) v))
                      {}
                      (apply concat
                        (for [line lines
                              :let [[a & bs] (re-seq #"\d+" line)]
                              b bs]
                          [[a b] [b a]])))]
    (println (count (reachable connections "0")))
    (println (count (take-while #(pos? (count %)) (iterate remove-one-group connections))))))

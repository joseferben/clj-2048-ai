(ns clj.ai
  (:require
   [clj.game :as game]))

(def matrix [[15 14 13 12] [8 9 10 11] [7 6 5 4] [0 1 2 3]])

(defn- cluster-score [board matrix]
  (reduce +
          (for [x [0 1 2 3]
                y [0 1 2 3]]
            (* (nth (nth board x) y) (nth (nth matrix x) y)))))

(defn- neighbour-score
  [x y board]
  (reduce +
          [(Math/abs (- (nth (nth board x) y) (nth (nth board (max (dec x) 0)) y)))
          (Math/abs (- (nth (nth board x) y) (nth (nth board (min (inc x) 3)) y)))
          (Math/abs (- (nth (nth board x) y) (nth (nth board x) (max (dec y) 0))))
          (Math/abs (- (nth (nth board x) y) (nth (nth board x) (min (inc y) 3))))]))

(defn- hetero-score
  [board]
  (reduce +
          (for [x [0 1 2 3]
                y [0 1 2 3]]
            (neighbour-score x y board))))

(defn- score
  "heuristic score for a given board"
  ([board original]
   (if (= board original)
     0
     (score board)))
  ([board]
   (let [cl (cluster-score board matrix)
         ht (hetero-score board)]
     (- (* 1 cl) ht))))

(def m-score (memoize score))

(defn best-move-heuristic
  [board]
  (let [moveh (sort-by val > (into (sorted-map)
                    (map
                     (fn [x] {x (score (game/execute-move board x) board)}) game/moves)))]
    (get game/moves-map
         (first (keys moveh)))))

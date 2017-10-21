(ns clj.ai)

(def matrix [[6 5 4 3] [5 4 3 2] [4 3 2 1] [3 2 1 0]])

(defn cluster-score [board]
  (reduce +
          (for [x [0 1 2 3]
                y [0 1 2 3]]
            (* x y))))

(defn neighbour-score
  [x y board]
  (reduce +
          [(- (nth (nth board x) y) (nth (nth board (max (dec x) 0)) y))
          (- (nth (nth board x) y) (nth (nth board (min (inc x) 3)) y))
          (- (nth (nth board x) y) (nth (nth board x) (max (dec y) 0)))
          (- (nth (nth board x) y) (nth (nth board x) (min (inc y) 3)))]))

(defn hetero-score
  [board]
  (reduce +
          (for [x [0 1 2 3]
                y [0 1 2 3]]
            (neighbour-score x y board))))

(defn heuristic-score
  [board]
  (- (* 2 (cluster-score board)) (hetero-score board)))

(defn best-move
  [board]
  (prn "woooo" board)
  (rand-int 4))

(ns clj.ai
  (:require [clj.game :as game]))

(def matrix [[6 5 4 3] [5 4 3 2] [4 3 2 1] [3 2 1 0]])
(def moves-map {:up 0 :down 1 :left 2 :right 3})
(def moves '(:up :down :left :right))

(defn cluster-score [board matrix]
  (reduce +
          (for [x [0 1 2 3]
                y [0 1 2 3]]
            (* (nth (nth board x) y) (nth (nth matrix x) y)))))

(defn neighbour-score
  [x y board]
  (reduce +
          [(Math/abs (- (nth (nth board x) y) (nth (nth board (max (dec x) 0)) y)))
          (Math/abs (- (nth (nth board x) y) (nth (nth board (min (inc x) 3)) y)))
          (Math/abs (- (nth (nth board x) y) (nth (nth board x) (max (dec y) 0))))
          (Math/abs (- (nth (nth board x) y) (nth (nth board x) (min (inc y) 3))))]))

(defn hetero-score
  [board]
  (reduce +
          (for [x [0 1 2 3]
                y [0 1 2 3]]
            (neighbour-score x y board))))

(defn score
  [board original]
  (if (= board original)
    0
    (let [cl (cluster-score board matrix)
          ht (hetero-score board)]
      (prn "cluster score: " cl)
      (prn "hetero score: " ht)
      (- (* 10 cl) ht))))

(defn best-move
  [board]
  (prn "board: " board)
  (let [moveh (sort-by val > (into (sorted-map)
                    (map
                     (fn [x] {x (score (game/execute-move board x) board)}) moves)))]
    (prn moveh)
    (get moves-map
         (first (keys moveh)))))

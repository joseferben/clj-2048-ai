(ns clj.expectimax
  (:require
   [clj.ai :as ai]
   [clj.game :as game]))

(declare calculate-max)

(defn average
  [numbers]
    (/ (apply + numbers) (count numbers)))

(defn all-spawns
  [board kind]
  (->>
   (for [x [0 1 2 3]
         y [0 1 2 3]]
     (if (= (nth (nth board x) y) 0)
       (assoc-in (vec board) [x y] kind)))
   (filter (complement nil?))))

(defn all-moves
  [board]
  (filter #(not= % board) (map #(game/execute-move board %) ai/moves)))

(defn calculate-chance
  ([board depth limit original]
   (if (= board original)
     0
     (calculate-chance board depth limit)))
  ([board depth limit]
   (if (or (= depth limit)) ;(> (- (System/currentTimeMillis) timestamp) 300))
     (ai/m-score board)
     (average (concat
               (map #(* (calculate-max % (inc depth) limit) 0.9) (all-spawns board 2))
               (map #(* (calculate-max % (inc depth) limit) 0.1) (all-spawns board 4)))))))

(defn calculate-max
  ([board depth limit original]
  (if (= board original)
    0
    (calculate-max board depth limit)))
  ([board depth limit]
  (if (or (= depth limit) );(> (- (System/currentTimeMillis) timestamp) 300))
    (ai/m-score board)
    (apply max (concat (map #(calculate-chance % (inc depth) limit) (all-moves board)) '(0))))))


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
  ([board depth original]
   (if (= board original)
     0
     (calculate-chance board depth)))
  ([board depth]
   (if (= depth 0)
     (ai/score board)
     (average (concat
               (map #(* (calculate-max % (dec depth)) 0.9) (all-spawns board 2))
               (map #(* (calculate-max % (dec depth)) 0.1) (all-spawns board 4)))))))

(defn calculate-max
  ([board depth original]
  (if (= board original)
    0
    (calculate-max board depth)))
  ([board depth]
  (if (= depth 0)
    (ai/score board)
    (apply max (concat (map #(calculate-chance % (dec depth)) (all-moves board)) '(0))))))


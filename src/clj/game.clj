(ns clj.game)

(def moves {:up 0 :down 1 :left 2 :right 3})

(defn remove-zeroes
  [row]
  (vec (filter (complement zero?) row)))

(defn pad-zeroes
  [row]
  (loop [row row]
    (if (>= (count row) 4)
      row
      (recur (conj row 0)))))

(defn merge-pair
  [row a b original]
  (if (and (= (nth row a) (nth row b))
           (or (nil? original) (= (nth original 2) (nth row 2))))
      (-> row
          (assoc a (+ (nth row a) (nth row b))) 
          (assoc b 0))
      row))

(defn merge-row-left 
  [row]
  (-> row
      (remove-zeroes)
      (pad-zeroes)
      (merge-pair 0 1 nil)
      (merge-pair 2 3 nil)
      (merge-pair 1 2 row)
      (remove-zeroes)
      (pad-zeroes)))

(defn merge-row-right
  [row]
  (-> row
      (reverse)
      (merge-row-left)
      (#(vec (reverse %)))))

(defn merge-row
  [move]
  (if (= move :right)
    merge-row-right
    merge-row-left))

(defn merge-rows
  [board move]
  (map (merge-row move) board))

(defn transpose-move
  [move]
  (cond
    (= move :down) :right
    (= move :up) :left))

(defn transpose
  [board]
  (apply mapv vector board))

(defn execute-move
  [board move]
  (if (> 2 (get moves move))
   (transpose (merge-rows (transpose board) (transpose-move move)))
   (merge-rows board move)))

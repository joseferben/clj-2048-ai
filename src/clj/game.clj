(ns clj.game)

(def moves-map {:up 0 :down 1 :left 2 :right 3})
(def moves '(:up :down :left :right))

(defn- remove-zeroes
  [row]
  (vec (filter (complement zero?) row)))

(defn- pad-zeroes
  "right pads zeroes to length 4"
  [row]
  (loop [row row]
    (if (>= (count row) 4)
      row
      (recur (conj row 0)))))

(defn- merge-pair
  "merges two elemnts of a row to the left, considering the original row"
  [row a b original]
  (if (and (= (nth row a) (nth row b))
           (or (nil? original) (= (nth original 2) (nth row 2))))
      (-> row
          (assoc a (+ (nth row a) (nth row b))) 
          (assoc b 0))
      row))

(defn- merge-row-left 
  [row]
  (-> row
      (remove-zeroes)
      (pad-zeroes)
      (merge-pair 0 1 nil)
      (merge-pair 2 3 nil)
      (merge-pair 1 2 row)
      (remove-zeroes)
      (pad-zeroes)))

(def m-left (memoize merge-row-left))

(defn- merge-row-right
  [row]
  (-> row
      (reverse)
      (m-left)
      (#(vec (reverse %)))))

(def m-right (memoize merge-row-right))

(defn- merge-row
  [move]
  (if (= move :right)
    m-right
    m-left))

(defn- merge-rows
  [board move]
  (map (merge-row move) board))

(defn- transpose-move
  [move]
  (cond
    (= move :down) :right
    (= move :up) :left))

(defn- transpose
  [board]
  (apply mapv vector board))

(defn execute-move
  "returns board after execution of move"
  [board move]
  (if (> 2 (get moves-map move))
   (transpose (merge-rows (transpose board) (transpose-move move)))
   (merge-rows board move)))

(defn count-zeroes
  [board]
  (or (-> board
          (flatten)
          (frequencies)
          (get 0)) 0))

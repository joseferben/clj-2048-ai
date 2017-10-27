(ns clj.handler
  (:require [clj.ai :as ai]
            [clj.expectimax :as ex]
            [clj.game :as game]
            [clojure.core.memoize :as mem]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as cheshire]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.util.response :refer [response content-type]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(def moves '(:up :down :left :right))
(def moves-map {:up 0 :down 1 :left 2 :right 3})

(defn pretty-print
  [board]
  (prn (nth board 0))
  (prn (nth board 1))
  (prn (nth board 2))
  (prn (nth board 3)))

(defn count-zeroes
  [board]
  (or (-> board
          (flatten)
          (frequencies)
          (get 0)) 0))

(defn decide-depth
  [number]
  (cond
    (> number 12) 1
    (> number 7) 2
    (> number 4) 3
    (> number 1) 4
    (>= number 0) 6
    :else 2))
    
(defn get-depth
  [board]
  (-> board
      (count-zeroes)
      (decide-depth)))

(defn best-move
  [board]
  (pretty-print board)
  (let [moveh (sort-by val > (into (sorted-map)
                    (pmap
                     (fn [x] {x (ex/calculate-chance (game/execute-move board x) 0 (get-depth board) board)}) moves)))]
    (prn moveh)
    (get moves-map
         (first (keys moveh)))))

(def m-best-move (memoize best-move))

(defn json [form]
  (-> form
    cheshire/encode
    response
    (content-type "application/json; charset=utf-8")))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/" {data :body} (json (m-best-move data)))
  (route/not-found "Not Found"))

(def app
  (wrap-json-body app-routes api-defaults))

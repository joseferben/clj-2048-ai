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

(defn best-move
  "returns the best move given the current board"
  [board]
  (let [moveh (sort-by val > (into (sorted-map)
                    (pmap
                     (fn [x] {x (ex/calculate-chance
                                 (game/execute-move board x) 0
                                 (ex/get-depth board) board)})
                     game/moves)))]
    (get game/moves-map
         (first (keys moveh)))))

(defn json [form]
  (-> form
    cheshire/encode
    response
    (content-type "application/json; charset=utf-8")))

(defroutes app-routes
  (POST "/" {data :body} (json (best-move data)))
  (route/not-found "Not Found"))

(def app
  (wrap-json-body app-routes api-defaults))

(ns clj.handler
  (:require [clj.ai :as ai]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as cheshire]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.util.response :refer [response content-type]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defn json [form]
  (-> form
    cheshire/encode
    response
    (content-type "application/json; charset=utf-8")))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/" {data :body} (json (ai/best-move data)))
  (route/not-found "Not Found"))

(def app
  (wrap-json-body app-routes api-defaults))

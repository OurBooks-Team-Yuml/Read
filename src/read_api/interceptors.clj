(ns read-api.interceptors
  (:require [monger.core :as mg]))

;(defonce mongo-uri (System/getenv "DATABASE_URI"))

;(defonce mongo-db (mg/connect-via-uri mongo-uri))

;(defonce database (atom mongo-db))

(def db-interceptor
  {:name :database-interceptor
   :enter #(assoc % ::db "d")})

(defn response [status body & {:as headers}]
  {:status status :body "test"})

(def ok       (partial response 200))
(def created  (partial response 201))
(def accepted (partial response 202))

(def echo
  {:name :echo
   :enter
   (fn [context]
     (let [request (:request context)
           response (ok context)]
       (assoc context :response response)))})

(def hello-world
  {:name :hello-world
   :leave
   (fn [context]
     (assoc context :response {:status 200 :body "Hello world"})
     context)})

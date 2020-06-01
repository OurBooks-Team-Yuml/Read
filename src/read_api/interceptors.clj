(ns read-api.interceptors
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(defonce mongo-uri (System/getenv "DATABASE_URI"))

(def db-interceptor
  {:name :database-interceptor
   :enter
   (fn [context]
     (let [{:keys [conn db]} (mg/connect-via-uri mongo-uri)]
       (assoc context :db db)))})

(def read-book
  {:name :read-book
   :enter
   (fn [context]
     (mc/insert (-> context :db) "readbooks" {:book-id (-> context :request :path-params :book-id)})
     context)})

(defn response [status body & {:as headers}]
  {:status status :body (-> body :request :path-params :book-id)})

(def ok       (partial response 200))
(def created  (partial response 201))
(def accepted (partial response 202))

(def echo
  {:name :echo
   :leave
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

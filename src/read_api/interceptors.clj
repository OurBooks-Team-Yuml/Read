(ns read-api.interceptors
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(defonce mongo-uri (System/getenv "DATABASE_URI"))
(defonce collection "readbooks")

(def db-interceptor
  {:name :database-interceptor
   :enter
   (fn [context]
     (let [{:keys [conn db]} (mg/connect-via-uri mongo-uri)]
       (assoc context :db db)))})

(def get-book-id-from-path [context]
  (-> context :request :path-params :book-id))

(def find-book-by-id
  {:name :is-in-db
   :enter
   (fn [context]
     (let [book (mc/find-maps (-> context :db) collection {:book-id (get-book-id-from-path context)})]
       (assoc context :book book)))})

(def book-found?
  {:name :book-found?
   :leave
   (fn [context]
     (if-let [[op & args] (:book context)]
       context))})

(def read-book
  {:name :read-book
   :leave
   (fn [context]
     (mc/insert (-> context :db) collection {:book-id (get-book-id-from-path context)})
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

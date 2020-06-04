(ns read-api.interceptors
  (:require [io.pedestal.http :refer [json-response]])
  (use [read-api.database]))

(defn get-book-id-from-path [context]
  (-> context :request :path-params :book-id))

(defn response [status body & {:as headers}]
  {:status status :body body :headers {"Content-Type" "application/json"}})

(def ok          (partial response 200))
(def created     (partial response 201))
(def no-body     (partial response 204))
(def bad-request (partial response 400))
(def not-found   (partial response 404))

(def db-interceptor
  {:name :database-interceptor
   :enter
   (fn [context]
     (let [{:keys [conn db]} database]
       (assoc context :db db)))})

(def find-book-by-id
  {:name :is-in-db
   :enter
   (fn [context]
     (let [book (find-by-id (-> context :db) (get-book-id-from-path context))]
       (assoc context :book book)))})

(defn add-record [context]
  (add (-> context :db) {:book-id (get-book-id-from-path context)})
  context)

(defn remove-record [context]
  (remove-with-id (-> context :db) (-> context :book first :_id))
  context)

(def read-book
  {:name :read-book
   :enter
   (fn [context]
     (if-let [book (-> context :book empty? not)]
       context
       (add-record context)))
   :leave
   (fn [context]
     (if-let [book (-> context :book empty? not)]
       (assoc context :response
              (bad-request {:errors {:book-id ["You cannot mark the book second time."]}}))
       (assoc context :response (created {:book-id (get-book-id-from-path context)}))))})

(def unread-book
  {:name :unread-book
   :enter
   (fn [context]
     (if-let [book (-> context :book empty?)]
       context
       (remove-record context)))
   :leave
   (fn [context]
     (if-let [book (-> context :book empty?)]
       (assoc context :response (not-found ""))
       (assoc context :response (no-body ""))))})

(def echo
  {:name :echo
   :leave
   (fn [context]
     (let [request (:request context)
           response (ok "")]
       (if-let [resp (-> context :response)]
         context
         (assoc context :response response))))})

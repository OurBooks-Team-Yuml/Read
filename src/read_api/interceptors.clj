(ns read-api.interceptors
  (use [read-api.database]))

(defn get-book-id-from-path [context]
  (-> context :request :path-params :book-id))

(defn response [status body & {:as headers}]
  {:status status :body (get-book-id-from-path body)})

(def ok       (partial response 200))
(def created  (partial response 201))
(def not-found (partial response 404))

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

(def read-book
  {:name :read-book
   :enter
   (fn [context]
     (if-let [book (-> context :book empty? not)]
       context
       (add-record context)))})

(def echo
  {:name :echo
   :leave
   (fn [context]
     (let [request (:request context)
           response (ok context)]
       (assoc context :response response)))})

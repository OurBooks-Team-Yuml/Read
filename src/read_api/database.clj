(ns read-api.database
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(defonce mongo-uri (System/getenv "DATABASE_URI"))
(defonce coll "readbooks")
(defonce database (mg/connect-via-uri mongo-uri))

(defn find-by-id [db id]
  (mc/find-maps db coll {:book-id id}))

(defn add [db data]
  (mc/insert db coll data))

(defn remove-with-id [db oid]
  (mc/remove-by-id db coll oid))

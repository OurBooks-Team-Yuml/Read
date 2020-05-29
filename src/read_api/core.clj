(ns read-api.core
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [read-api.routes :as routes]))

(defn create-server []
  (http/create-server
    {::http/routes routes/routes
     ::http/type   :jetty
     ::http/port   8890}))

(defn -main
  [& args]
  (println "Creating server...")
  (http/start (create-server)))

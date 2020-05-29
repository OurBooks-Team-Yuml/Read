(ns read-api.server
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [read-api.service :as service]))


(defonce runnable-service (http/create-server service/service))

(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& args]
  (println "\nCreating your [DEV] server...")
  (-> service/service
      (merge {:env          :dev
              ::http/routes #(deref #'service/routes)
              ::http/join?  false
              ::http/port   8890
              ::http/allowed-origins {:creds true :allowed-origins (constantly true)}})
      http/default-interceptors
      http/dev-interceptors
      http/create-server
      http/start))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "Creating server...")
  (http/start runnable-service))

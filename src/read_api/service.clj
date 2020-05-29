(ns read-api.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [read-api.views.hello :as hello]))

(defroutes routes
  [[["/"
     ["/greet" {:get hello/respond-hello}]]]])

(def service {:env          :prod
              ::http/routes routes
              ::http/type   :jetty
              ::http/host   "0.0.0.0"
              ::http/port   8080})

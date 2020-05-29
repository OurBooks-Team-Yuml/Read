(ns read-api.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [read-api.views.hello :as hello]))

(defroutes routes
  [[["/"
     ["/greet" {:get hello/respond-hello}]]]])

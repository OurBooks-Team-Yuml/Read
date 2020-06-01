(ns read-api.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.ring-middlewares :as middleware]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ns-tracker.core :refer [ns-tracker]]
            [read-api.views.hello :as hello]
            [read-api.interceptors :as interceptors]))

(def routes
  (route/expand-routes
    #{["/reads/read/:book-id" :post [interceptors/echo interceptors/db-interceptor interceptors/read-book] :route-name :read]}))

(def modified-namespaces (ns-tracker "src"))

(def service {:env          :prod
              ::http/routes routes
              ::http/type   :jetty
              ::http/host   "0.0.0.0"
              ::http/port   8080
              ::http/interceptors [http/log-request
                                   http/not-found
                                   route/query-params
                                   (middleware/file-info)
                                   (middleware/resource "public")
                                   (route/method-param :_method)
                                   (route/router (fn []
                                     (doseq [ns-sym (modified-namespaces)]
                                       (require ns-sym :reload))
                                     routes))]})

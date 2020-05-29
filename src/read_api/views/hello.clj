(ns read-api.views.hello)

(defn respond-hello [request]
  {:status 200 :body "Hello, world!"})

(ns vorstellung.routes.home
  (:require
   [clojure.java.io :as io]
   [ring.util.response :refer [redirect]]
   [ring.util.http-response :as response]
   [buddy.auth :refer [authenticated?]]
   [vorstellung.layout :as layout]
   [vorstellung.middleware :as middleware]
   [vorstellung.upload :as upload]))

(defn home-page [request app]
  (layout/render request "home.html" {:script app}))

(defn home-routes []
  [""
   {:middleware [#_middleware/wrap-csrf
                 #_auth/wrap-auth
                 middleware/wrap-formats]}
   ["/"         {:get #(home-page % "/js/app.js")}]
   ["/upload" {:post upload/upload}]
   ["/docs" {:get (fn [_]
                    (-> (response/ok (-> "README.md" #_io/resource slurp))
                        (response/header "Content-Type" "text/plain; charset=utf-8")))}]])

(ns persons.core
  (:use ring.util.response)
  (:require
    [clojure.java.jdbc :as sql]
    [compojure.handler :as handler]
    [ring.middleware.json :as middleware]
		[compojure.core :refer :all]
		[org.httpkit.server :refer [run-server]])
  (:gen-class))

(def db {:subprotocol "mysql"
	:subname "//localhost:3306/personsdb"
	:user "root"
	:password "root"})

(defn initialize-db []
  (sql/execute! db ["CREATE TABLE IF NOT EXISTS persons (id VARCHAR(255) PRIMARY KEY, firstName VARCHAR(255), lastName VARCHAR(255));"]))

(defn generate-id [] (str (java.util.UUID/randomUUID)))

(defn get-persons []
  (response {
    :status "ok"
    :data (into [] (sql/query db ["SELECT * FROM persons;"]))
  }))

(defn get-person [id]
  (let [results (sql/query db ["SELECT * FROM persons WHERE id = ?;" id])]
    (cond
      (empty? results) (response {:status "ok"})
      :else (response {
        :status "ok"
        :data (first results)
      }))))

(defn register-person [data]
  (let [id (generate-id)]
    (let [person (assoc data "id" id)]
      (sql/insert! db :persons person))
    (get-person id)))


(defn update-person [id data]
  (let [person (assoc data "id" id)]
    (sql/update! db :persons person ["id=?" id]))
  (get-person id))

(defn delete-person [id]
  (sql/delete! db :persons ["id=?" id])
  (response {:status "ok"}))

(defroutes app-routes
  (GET "/" [] (get-persons))
  (POST "/" {body :body} (register-person body))
  (context "/:id" [id] (defroutes person-routes
    (GET "/" [] (get-person id))
    (PUT "/" {body :body} (update-person id body))
    (DELETE "/" [] (delete-person id)))))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(defn -main
  [& args]
  (def port 8080)
  (initialize-db)
  (run-server app {:port port})
	(println "Listening on port" port))

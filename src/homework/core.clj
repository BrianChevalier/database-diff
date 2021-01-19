(ns homework.core
  (:require [next.jdbc :as jdbc]
            [clojure.set :as set]))

(defn- classify
  "Classify the database entries by id
  `:missing` the id is missing in the new database
  `:new` the entry is a new entry in the database
  `:corrupt` the entry is corrupted from the old and new database (possibly corrupted)
  keys that are the same between databases don't appear in the output file"
  [id old new]
  (let [in-old? (contains? old id)
        in-new? (contains? new id)]
    (cond
     (and in-old? (not in-new?))      {:accounts/id id :status :missing}
     (and (not in-old?) in-new?)      {:accounts/id id :status :new}
     (not= (get old id) (get new id)) {:accounts/id id :status :corrupt})))

(defn- by-ids
  "Sort the database items by id in a map"
  [coll]
   (zipmap (map :accounts/id coll) coll))

(defn- all-ids [old new]
  (set/union (set (keys old)) (set (keys new))))

(defn diff
  "Diff the databases. Iterates over the keys in both maps and classifies them per `classify`"
  [old new]
  (let [old (by-ids old)
        new (by-ids new)]
    (for [id (all-ids old new)
         :let [out (classify id old new)]
         :when (not (nil? out))]
     out)))

(defn- diff-databases
  [old-database new-database]
  (diff
   (jdbc/execute! old-database ["SELECT * FROM accounts;"])
   (jdbc/execute! new-database ["SELECT * FROM accounts;"])))

(def old-database-config
  {:dbtype "postgres"
   :dbname "old"
   :user "old"
   :password "hehehe"
   :host "localhost"
   :post 5432})

(def new-database-config
  {:dbtype "postgres"
   :dbname "new"
   :user "new"
   :password "hahaha"
   :host "localhost"
   :port 5433})

(defn -main []
  (let [old-database (jdbc/get-datasource old-database-config)
        new-database (jdbc/get-datasource new-database-config)]
    (println "Diffing databases...")
    (spit "diff.edn" (pr-str (diff-databases old-database new-database)))
    (println "Printed results to diff.edn")))

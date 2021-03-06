#!/usr/bin/env inlein

'{:dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]]}

(require '[clojure.test :refer :all])
(require '[clojure.data.json :as json])

(try
  (load-file "check/solution.clj")
  (catch Exception ex
    (println (json/write-str {:status "error" :result (.getMessage ex)}))))

(def data (line-seq (java.io.BufferedReader. *in*)))

(def prepared-data
  (json/read-str (str "[" (apply str data) "]") :key-fn keyword))

(defn generate-tests
  [data solution]
  (doseq [x data]
    (if (:check x)
      (println (json/write-str {:status "ok" :result (:check x)}))
      (try (assert (= (:expected x) (apply solution (:arguments x))))
           (catch java.lang.AssertionError e
             (println (json/write-str {:status "failure" :result (:arguments x)})))))))

(generate-tests prepared-data solution)
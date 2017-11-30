(defproject boolo "0.0.1-SNAPSHOT"
  :description "Evalo"
  :url "http://github.com/RickMoynihan/evalo"
  :license {:name "Eclipse Public License - v1.0"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.9.0-RC1"]
                 [org.clojure/spec.alpha "0.1.134"]
                 [org.clojure/core.logic  "0.8.11"]]

  :repl-options {:init-ns evalo.core
                 :init (require 'evalo.core)}
  
  :source-paths ["src" "env/dev/src"]
  :jvm-opts ["-Djava.awt.headless=true"
             "-XX:-OmitStackTraceInFastThrow"
             ;; Use this property to control number
             ;; of connections in the SPARQLRepository connection pool:
             ;;
             ;;"-Dhttp.maxConnections=1"
             ]

  ;; Target JDK 7 expected JVM version (though we may now be able to target JDK8 in production)
  :javac-options ["-target" "7" "-source" "7"]
  :min-lein-version "2.7."
  )


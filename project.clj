(defproject com.company "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["./" "com/company" "src/main/java"]
  :prep-tasks ["javac" "compile"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :source-paths ["./" "com/company" "src/main/java"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ]
  :aot :all
  :main com.company.core
  :profiles {:uberjar {:aot :all}}
  )


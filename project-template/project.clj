(defproject sketch "0.1.0-SNAPSHOT"
  :description "A template for a sketch with Quil/Processing"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [genartlib/genartlib "0.1.23"]] ; utility functions
  :jvm-opts ["-Xms4000m" "-Xmx4000M" ; 4GB heap size
             "-server"
             "-Dsun.java2d.uiScale=1.0"] ; adjust scaling for high DPI displays
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :aot [sketch.dynamic]) ; consider disabling, this can cause edge-case issues

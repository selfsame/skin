(defproject selfsame/skin "0.1.0-SNAPSHOT"
  :description "Reagent style sheets."
  :url "http://github.com/selfsame/skin"
  :license {:name "The MIT License (MIT)"
            :url "https://github.com/selfsame/skin/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [reagent "0.7.0"]]
  :min-lein-version "2.5.3"
  :source-paths ["src" "test"]
  :plugins [[lein-cljsbuild "1.1.4"]]
  :clean-targets ^{:protect false} ["resources/public/js"
                                    "target"]
  :figwheel {:css-dirs ["resources/public/css"]}
  :profiles
  {:dev
   {:dependencies []
    :plugins      [[lein-figwheel "0.5.15"]]}}
  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src" "test"]
     :figwheel     {:on-jsload "skin.core/reload"}
     :compiler     {:main                 skin.core
                    :optimizations        :none
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/dev"
                    :asset-path           "js/dev"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src"]
     :compiler     {:main            skin.core
                    :optimizations   :advanced
                    :output-to       "resources/public/js/app.js"
                    :output-dir      "resources/public/js/min"
                    :elide-asserts   true
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]}
  :deploy-repositories [
  ["clojars" {
    :sign-releases false}]])

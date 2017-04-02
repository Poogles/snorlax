(ns snorlax.consumer
  (:require [clojure.core.async :as async]
            [gregor.core :as gregor]
            [clojure.tools.logging :as log]
            [mount.core :as mount :refer [defstate]]
            [snorlax.metrics :as metrics]
            [metrics.meters :as meters])
  (:gen-class))

(def running (atom false))

(def gregor-conf {"auto.offset.reset"       "earliest",
                  "key.deserializer" gregor/byte-array-deserializer,
                  "value.deserializer" gregor/byte-array-deserializer,
                  "auto.commit.interval.ms" "1000",
                  ;; TODO: Disable this.
                  "enable.auto.commit"      "true",
                  "max.poll.records"        "50",
                  "session.timeout.ms"      "30000"})

(defstate kafka-client
  :start (gregor/consumer (:kafka_bootstrap (mount/args))
                          (:consumer-group (mount/args))
                          (:topics (mount/args))
                          gregor-conf)
  :stop (gregor/close kafka-client))

(defn fetch-messages [channel]
  (log/info "Launching fetch-messages.")
  (while @running
    (let [consumer-seq (gregor/poll kafka-client)]
      ;; Poll returns a sequence, iterate over that.
      (doseq [record consumer-seq]
        (async/>!! channel  record)
        (meters/mark! metrics/throughput-meter))
      ;; TODO: Remove this as we shouldn't commit until done.
      (gregor/commit-offsets! kafka-client)))
  ;; We've stopped consuming, close.
  (async/close! channel)
  (log/info "Left consuming due to atom state change."))

(defstate consumer
  :start (do
           (reset! running true)
           (fetch-messages (:transform-chan (mount/args))))
  :stop (reset! running false))
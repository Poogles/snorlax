# snorlax

Snorlax is here and he's tired of your shit.

Snorlax streams data from Kafka to Cassandra, applying ETL style transformations
to messages as they go past.

Messages are only marked as consumed once a write has been confirmed from
Cassandra.


## Installation

git clone and lein uberjar, or download the docker container!


## Usage

FIXME: explanation

    $ java -jar snorlax-0.1.0-standalone.jar [args]


## Options

All bootstrapping is done using environment variables and envconsul.

Since what the app does is pretty simple the amount of configuration is slim!

Current configuration options are as follows...

| Option | Description | Example |
| ------ | -----------| ------- |
| KAFKA_BOOTSTRAP | Kafka bootstrap host and port | localhost:9092 |
| KAFKA_CONSUMER_GROUP | Kafka consumer group id | snorlax_core_production |
| CASSANDRA_BOOTSTRAP | Cassandra bootstrap host and port | localhost:4052 |
| CASSANDRA_TABLE | Cassandra table to write to | idb |
| STATSD_BOOTSTRAP | Statsd host to emit metrics to | localhost:4555 |
| STATSD_PREFIX | Statsd prefix for all metric paths | snorlax |
| STATSD_TAGS | Map of additional statsd tags | role:ds |


## Examples

...

### Bugs

Raise any bugs to trello on the devops board with the bugs tag.

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

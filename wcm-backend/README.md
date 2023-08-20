## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).


### Configuration

#### Post Collection
The Server calls the endpoint `https://thekey.academy/wp-json/wp/v2/posts` every 60 Seconds by default. You can change the endpoint by modifing `POST_URL` in `src/main/scala/actors/GatherPostActor.scala`. The Interval in which the server perfroms a request can be configured by modifing `FETCH_INTERVAL` in `src/main/scala/actors/ControlActor.scala`.
#### Websocket Endpoint
The Server will start at `http://localhost:8080` on default. For connecting with websockets use the route `\ws`
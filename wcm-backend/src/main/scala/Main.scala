import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import scala.io.StdIn

import actors.ControlActor
import models.WpPostObject
import models.WpPost
import scala.concurrent.ExecutionContextExecutor
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.BinaryMessage
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.model.HttpMethods.GET
import actors.ClientConnectionActor


object WordCountMap extends App {

  val posts_stub = Array(
    WpPost(
      0,
      WpPostObject(""),
      WpPostObject("<p> Ein einfaches Beispiel mit einem Beispiel </p>")
    ),
    WpPost(
      1,
      WpPostObject(""),
      WpPostObject("<p> Ein einfaches Beispiel mit einem Beispiel </p>")
    )
  )

  implicit val system: ActorSystem[ControlActor.ControlCommand] =
    ActorSystem(ControlActor(), "gather")

  implicit val executionContext: ExecutionContextExecutor =
    system.executionContext

  val route = path("ws") {
    handleWebSocketMessages(ClientConnectionActor.listen())
  }

  val bindingFuture =
    Http().newServerAt("localhost", 8080).bind(route)


  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  system ! ControlActor.Start()

  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

  // system ! GatherPostActor.GatherPost(POST_URL)
  /*
  val posts_stub = Array(WpPost(0,WpPostObject(""),WpPostObject("<p> Ein einfaches Beispiel mit einem Beispiel </p>")), WpPost(1,WpPostObject(""),WpPostObject("<p> Ein einfaches Beispiel mit einem Beispiel </p>")))

  val system: ActorSystem[PostsToWordcountMapActor.TransformToMap] =
    ActorSystem(PostsToWordcountMapActor(), "gather")

  system ! PostsToWordcountMapActor.TransformToMap(posts_stub)
   */

}

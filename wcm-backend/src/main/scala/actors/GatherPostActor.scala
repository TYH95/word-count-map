package actors

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import scala.util.{Failure, Success}
import akka.http.scaladsl.model.{HttpResponse,HttpRequest, StatusCodes}
import scala.concurrent.Future
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.Http
import models.{WpPost, WpPostObject}



object GatherPostActor {

  final case class GatherPost(url: String)

  def apply(): Behavior[GatherPost] =
    Behaviors.setup { context =>
      val transformer =
        context.spawn(StringToWordcountMapActor(), "transformer")
      implicit val postObjectFormat: RootJsonFormat[WpPostObject] = jsonFormat1(WpPostObject.apply)
      implicit val postFormat: RootJsonFormat[WpPost] = jsonFormat3(WpPost.apply)
      implicit val system = context.system
      implicit val executionContext = system.executionContext

      Behaviors.receiveMessage {
        case GatherPost(url) =>
          var req: HttpRequest = Get(url)
          val responseFuture: Future[HttpResponse] = Http().singleRequest(req)

          responseFuture.onComplete {

            case Success(response) =>
              // Convert your response body (response.entity) in a profile array. Note that it is a Future object
              var responseAsPosts: Future[Array[WpPost]] =
                Unmarshal(response.entity).to[Array[WpPost]]

              // When the Future is fulfilled
              responseAsPosts.onComplete {

                _.get match {

                  case posts: Array[WpPost] =>
                    println("success")
                }

              }

            case Failure(_) =>
               println(responseFuture)
              // Here your code if there is not a response
            
          }
          Behaviors.same

      }
    }

}
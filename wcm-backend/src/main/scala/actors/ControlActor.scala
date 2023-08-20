package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.MapView
import models.WpPost
import actors.ClientConnectionActor
import spray.json._
import DefaultJsonProtocol._

abstract class BaseControlActor {}

object ControlActor extends BaseControlActor {
  sealed trait ControlCommand

  final case class Start() extends ControlCommand

  final case class ReceiveWordCountMap(wcm: Map[String, Int])
      extends ControlCommand

  def apply(): Behavior[ControlCommand] =
    Behaviors.setup { context =>
      val deliverer =
        context.spawn(ClientConnectionActor(context.self), "deliverer")
      val gatherer = context.spawn(GatherPostActor(), "gatherer")
      val wordCountMapActor = context.spawn(
        WordcountMapActor(
          gatherer,
          context.self
        ),
        "wcm-model"
      )

      Behaviors.receiveMessage {
        case Start() =>
          wordCountMapActor ! WordcountMapActor.GetWordcountMap()
          Behaviors.same

        case ReceiveWordCountMap(wcm) =>
          println("Map Received")
          deliverer ! ClientConnectionActor.sendMessage(mapToJson(wcm))
          Behaviors.same
      }
    }

  def mapToJson(wcm: Map[String, Int]): String = {
      return wcm.toJson.compactPrint
  }
}

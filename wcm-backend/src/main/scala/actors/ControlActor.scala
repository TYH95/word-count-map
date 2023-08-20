package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.MapView
import actors.ClientConnectionActor
import spray.json._
import DefaultJsonProtocol._
import scala.concurrent.duration._
import akka.actor.typed.ActorRef

object ControlActor {

  sealed trait ControlCommand

  final case class Start() extends ControlCommand

  final case class Stop() extends ControlCommand
  final case class ReceiveWordCountMap(wcm: Map[String, Int])
      extends ControlCommand

  private case object Interval extends ControlCommand

  private val FETCH_INTERVAL = 30.second

  private var deliverer: ActorRef[ClientConnectionActor.ConnectionCommand] = null

  private var gatherer: ActorRef[GatherPostActor.SourceCommand] = null

  private var wordCountMapActor: ActorRef[WordcountMapActor.WordcountMapCommand] = null

  def apply(): Behavior[ControlCommand] =
    Behaviors.setup { context =>
      deliverer =
        context.spawn(ClientConnectionActor(context.self), "deliverer")
      gatherer = context.spawn(GatherPostActor(), "gatherer")
      wordCountMapActor = context.spawn(
        WordcountMapActor(
          gatherer,
          context.self
        ),
        "wcm-model"
      )
      idle()
    }

  private def idle(): Behavior[ControlCommand] = Behaviors.receiveMessage {
    case Start() =>
      inInterval()
    case _ =>
      Behaviors.unhandled
  }

  private def inInterval(): Behavior[ControlCommand] =
    Behaviors.withTimers[ControlCommand] { timers =>

      timers.startSingleTimer(Interval, FETCH_INTERVAL)
      Behaviors.receiveMessagePartial {
        case Interval =>
          wordCountMapActor ! WordcountMapActor.GetWordcountMap()
          Behaviors.same
        case ReceiveWordCountMap(wcm) =>
          deliverer ! ClientConnectionActor.sendMessage(mapToJson(wcm))
          inInterval()
        case Stop() =>
          idle()
      }
    }

  private def mapToJson(wcm: Map[String, Int]): String = {
    return wcm.toJson.compactPrint
  }
}

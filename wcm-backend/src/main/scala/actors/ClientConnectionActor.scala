package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import models.WebSocket

object ClientConnectionActor {
  sealed trait ConnectionCommand

  final case class sendMessage(msg: String) extends ConnectionCommand

  def apply(): Behavior[ConnectionCommand] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage { case sendMessage(msg) =>
        WebSocket.sendText(msg)
        Behaviors.same
      }
    }
}

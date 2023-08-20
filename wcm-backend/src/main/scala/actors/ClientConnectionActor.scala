package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.NotUsed
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source, SourceQueueWithComplete}
object ClientConnectionActor {

  sealed trait ConnectionCommand

  final case class sendMessage(msg: String) extends ConnectionCommand

  private var browserConnections: List[TextMessage => Unit] = List()

  private var controller: ActorRef[ControlActor.ControlCommand] = null

  def apply(controller: ActorRef[ControlActor.ControlCommand]): Behavior[ConnectionCommand] =
    Behaviors.setup { context =>
      this.controller = controller
      Behaviors.receiveMessage { case sendMessage(msg) =>
        sendText(msg)
        Behaviors.same
      }
    }

  def listen(): Flow[Message, Message, NotUsed] = {

    val inbound: Sink[Message, Any] = Sink.foreach(_ => ())
    val outbound: Source[Message, SourceQueueWithComplete[Message]] =
      Source.queue[Message](16, OverflowStrategy.fail)

    Flow.fromSinkAndSourceMat(inbound, outbound)((_, outboundMat) => {
      browserConnections ::= outboundMat.offer
      NotUsed
    })
  }

  def sendText(text: String): Unit = {
    for (connection <- browserConnections) connection(TextMessage.Strict(text))
  }
}

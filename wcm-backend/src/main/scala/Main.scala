import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import scala.collection.MapView

object StringToWordcountMapActor {

  final case class TransformToMap(value: String)

  def apply(): Behavior[TransformToMap] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage { message => {
        message match {
          case TransformToMap(value) => 
            val wordCountMap: MapView[String, Integer] = value.split("\\s+").groupBy(w => w).mapValues(_.size)
            println(wordCountMap.view.mapValues((x) => x).toMap)
            Behaviors.same
        }
      }}
    }

}

object GatherPostActor {

  final case class GatherPost(postContent: String)

  def apply(): Behavior[GatherPost] =
    Behaviors.setup { context =>
      val transformer =
        context.spawn(StringToWordcountMapActor(), "transformer")

      Behaviors.receiveMessage { message =>
        message match {
          case GatherPost(postContent) =>
            transformer ! StringToWordcountMapActor.TransformToMap(postContent)
            Behaviors.same
        }
      }
    }

}

object WordCountMap extends App {
  val system: ActorSystem[GatherPostActor.GatherPost] =
    ActorSystem(GatherPostActor(), "gather")

  system ! GatherPostActor.GatherPost("Ein einfaches Beispiel mit einem Beispiel doppelt.")
}

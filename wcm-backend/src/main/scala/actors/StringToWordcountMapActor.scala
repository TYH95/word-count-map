package actors
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.MapView


object StringToWordcountMapActor {

  final case class TransformToMap(value: String)

  def apply(): Behavior[TransformToMap] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage { message =>
        {
          message match {
            case TransformToMap(value) =>
              val wordCountMap: MapView[String, Integer] =
                value.split("\\s+").groupBy(w => w).mapValues(_.size)
              println(wordCountMap.view.mapValues((x) => x).toMap)
              Behaviors.same
          }
        }
      }
    }

}

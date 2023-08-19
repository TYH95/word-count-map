package actors
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.MapView
import models.WpPost

object PostsToWordcountMapActor {

  final case class TransformToMap(posts: Array[WpPost])

  def apply(): Behavior[TransformToMap] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage { case TransformToMap(posts) =>
        var mergedMap: Map[String, Int] = Map[String, Int]()

        posts.foreach[WpPost] { post =>
          // Get rid of HTML tags
          val sanatizedContent = post.content.rendered
            .replaceAll("""<(?!\/?a(?=>|\s.*>))\/?.*?>""", "")

          // Count Words in String
          val wordCountMap: MapView[String, Int] =
            sanatizedContent.split("\\s+").filter(w => !w.isEmpty()).groupBy(w => w).mapValues(_.size)
          
          mergedMap =
            mergedMap ++ wordCountMap.map{ case (k: String,v: Int) => k -> (v + mergedMap.getOrElse(k,0)) }
          post
        }

        println(mergedMap.view.mapValues((x) => x).toMap)
        Behaviors.same

      }
    }
}

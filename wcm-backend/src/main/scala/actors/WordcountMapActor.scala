package actors
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.MapView
import models.WpPost
import actors.ClientConnectionActor
import akka.actor.typed.ActorRef
import scala.util.{Failure, Success}
import akka.util.Timeout
import scala.concurrent.duration._

// Trait for the WordCountMap Model.
// Includes also some functions to sanatize Strings.
trait WordCountMap {

  // Remove all HTML Tags from given String
  // TODO: Find better solution then regex
  def stripHtmlTags(str: String): String = {
    return str
      .replaceAll("(<([^>]+)>)", " ")
      .replaceAll("""(&[\p{Digit}];)""", "")
      .replaceAll("\\s+", " ")
      .trim()
  }

  def removePunctuation(str: String): String = {
    return str
      .replaceAll("""[\p{Punct}&&[^-]]""", "")
      .replaceAll("""[”“]""", "")
      .trim()
  }

  // Counts each word in the post contents and returns the result als Map
  def transformToMap(posts: Array[WpPost]): Map[String, Int] = {
    var mergedMap: Map[String, Int] = Map[String, Int]()

    posts.foreach[WpPost] { post =>
      // Get rid of HTML tags
      var sanatizedContent: String = this.stripHtmlTags(post.content.rendered)
      sanatizedContent = this.removePunctuation(sanatizedContent)
      // Count Words in String
      val wordCountMap: MapView[String, Int] =
        sanatizedContent
          .split("\\s+")
          .filter(w => !w.isEmpty())
          .groupBy(w => w)
          .mapValues(_.size)

      mergedMap = mergedMap ++ wordCountMap.map { case (k: String, v: Int) =>
        k -> (v + mergedMap.getOrElse(k, 0))
      }
      post
    }
    return mergedMap
  }
}

// Actor to transform the received posts and to provide an interface to the fetch post logic.
object WordcountMapActor extends WordCountMap {
  sealed trait WordcountMapCommand

  final case class GetWordcountMap() extends WordcountMapCommand
  final case class TransformToMap(posts: Array[WpPost])
      extends WordcountMapCommand

  def apply(
      source: ActorRef[GatherPostActor.SourceCommand],
      receiver: ActorRef[ControlActor.ReceiveWordCountMap]
  ): Behavior[WordcountMapCommand] =
    Behaviors.setup { context =>
      val sourceActor: ActorRef[GatherPostActor.SourceCommand] = source

      Behaviors.receiveMessage {
        case TransformToMap(posts) =>
          val wcm: Map[String, Int] = this.transformToMap(posts)
          receiver ! ControlActor.ReceiveWordCountMap(wcm)
          Behaviors.same
        case GetWordcountMap() =>
          implicit val timeout: Timeout = 5.seconds

          context.ask(sourceActor, GatherPostActor.GatherPost.apply) {
            case Success(GatherPostActor.ReplyWithPost(posts)) =>
              println("Posts Received")
              TransformToMap(posts)
            case Failure(_) =>
              println("Failure")

              TransformToMap(Array[WpPost]())
          }
          Behaviors.same
      }
    }
}

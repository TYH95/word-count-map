import akka.actor.typed.ActorSystem

import actors.GatherPostActor
import actors.PostsToWordcountMapActor
import models.WpPostObject
import models.WpPost

final val POST_URL = "https://thekey.academy/wp-json/wp/v2/posts"

object WordCountMap extends App {

  val system: ActorSystem[GatherPostActor.GatherPost] =
    ActorSystem(GatherPostActor(), "gather")

  system ! GatherPostActor.GatherPost(POST_URL)

/*
  val posts_stub = Array(WpPost(0,WpPostObject(""),WpPostObject("<p> Ein einfaches Beispiel mit einem Beispiel </p>")), WpPost(1,WpPostObject(""),WpPostObject("<p> Ein einfaches Beispiel mit einem Beispiel </p>")))

  val system: ActorSystem[PostsToWordcountMapActor.TransformToMap] =
    ActorSystem(PostsToWordcountMapActor(), "gather")

  system ! PostsToWordcountMapActor.TransformToMap(posts_stub)
*/

}

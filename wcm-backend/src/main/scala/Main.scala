import akka.actor.typed.ActorSystem

import actors.GatherPostActor

final val POST_URL = "https://thekey.academy/wp-json/wp/v2/posts"

object WordCountMap extends App {

  val system: ActorSystem[GatherPostActor.GatherPost] =
    ActorSystem(GatherPostActor(), "gather")

   system ! GatherPostActor.GatherPost(POST_URL)
}

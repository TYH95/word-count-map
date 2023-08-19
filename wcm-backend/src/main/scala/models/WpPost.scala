package models

//Minimal Model for Wordpress Posts
final case class WpPost(id: Int, title: WpPostObject, content: WpPostObject)

//Wrapper type for Object in WpPost Response
final case class WpPostObject(rendered: String)
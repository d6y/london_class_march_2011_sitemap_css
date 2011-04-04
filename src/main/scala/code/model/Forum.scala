package code
package model

import net.liftweb._
import json._
import common._

case class Forum(id: String, name: String)

object Forum {
  private implicit val formats =
    net.liftweb.json.DefaultFormats//  + BigDecimalSerializer

  def all: List[Forum] = forums.map(_._2).toList

  private val forums = Map("123" -> Forum("123", "Lift"),
                           "456" -> Forum("456", "Scala"))

  def find(id: String): Box[Forum] = forums.get(id)

  implicit def forumToJValue(f: Forum): JValue = 
    Extraction.decompose(f)

  // define an extractor
  def unapply(id: String): Option[Forum] = find(id)

  def unapply(testF: Any): Option[(String, String)] = testF match {
    case f: Forum => Some((f.id, f.name))
    case _ => None
  }
}

case class ForumThread(id: String, forumId: String, name: String)

object ForumThread {
  private implicit val formats =
    net.liftweb.json.DefaultFormats//  + BigDecimalSerializer

  def all(f: Forum): List[ForumThread] = 
    threads.map(_._2).
  filter(_.forumId == f.id).toList

  private val threads = 
    Map(List(ForumThread("foo1", "123", "Lift Menu"),
             ForumThread("foo2", "123", "Lift REST"),
           ForumThread("s33", "456", "Implicits")).
      map(ft => ft.id -> ft) :_*)

  def find(id: String): Box[ForumThread] = threads.get(id)

  implicit def forumToJValue(f: ForumThread): JValue = 
    Extraction.decompose(f)

  // define an extractor
  def unapply(id: String): Option[ForumThread] = find(id)

  def unapply(testF: Any): Option[(String, String, String)] = testF match {
    case f: ForumThread => Some((f.id, f.forumId, f.name))
    case _ => None
  }
}

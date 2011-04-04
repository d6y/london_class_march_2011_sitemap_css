package code
package snippet

import model._

import net.liftweb._
import common._
import http._
import sitemap._
import util._
import Helpers._
import scala.xml.Text

object MainForumPage {
  lazy val menu = Menu.i("Forums") / "forums"

  def render = "li *" #> Forum.all.map(f =>
    "a *+" #> f.name & "a [href]" #> AForum.menu.calcHref(f))
}

object AForum {
  lazy val menu = 
    Menu.param[Forum]("AForum", 
                      Loc.LinkText(f => Text(S.?("Forum") +": "+f.name)),
                      (s: String) => {
                        println("Looking up forum "+s)
                        val ret = Forum.find(s)
                        println("Found "+ret)
                        ret
                        },
                      (f: Forum) => f.id) / "forums" / * 
}

class AForum(f: Forum) {
  def render = "li *" #>
  ForumThread.all(f).map(t =>
    "a *+" #> t.name & "a [href]" #> AThread.menu.calcHref(f -> t))
}

object AThread {
  lazy val menu = 
    Menu.params[(Forum, 
                 ForumThread)]("AThread", 
                               Loc.LinkText(f => 
                                 Text(S.?("Thread") +": "+f._2.name)),
                               {
                                 case Forum(f) ::
                                 ForumThread(ft) :: Nil =>
                                   Full(f -> ft)
                                 case _ => Empty
                               },
                               (f: (Forum, ForumThread)) => 
                                 f._1.id :: f._2.id :: Nil) / "forums" / * / * 
}

class AThread(p: (Forum, ForumThread)) {
  val forum = p._1
  val thread = p._2

  def render = "@forum -*" #> forum.name & "@thread *+" #> thread.name
}

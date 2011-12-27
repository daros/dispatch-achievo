package org.daros

import dispatch._
import dispatch.jsoup.JSoupHttp._
import thread.ThreadSafeHttpClient
import org.apache.http.cookie.Cookie
import scala.collection.JavaConversions._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-25 13:33
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
class Achievo(val user: String, pw: String) {
  val h = Http
  login

  def login {
    val request = Login << Map("auth_user" -> user, "auth_pw" -> pw)
    h(request \\> { doc =>
      // check for login failed
      if (doc.getElementById("loginform") != null) {
        h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.clear()
      }
    })
  }

  def login_ns {
    val request = Login << Map("auth_user" -> user, "auth_pw" -> pw)
    val ns =  h(request as_jsoupedNodeSeq)
    if ((ns \\ "div").find(n => (n \ "@id").text == "loginform").isDefined) {
      h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.clear()
    }
  }

  def login_crude {
    val request = Login << Map("auth_user" -> user, "auth_pw" -> pw)
    jsouped(request) { doc =>
      if (doc.getElementById("loginform") != null) {
        h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.clear()
      }
    }
  }

  private def jsouped(request: Request)(op: Document => Unit) {
    val html = h(request as_str)
    val baseUri = request.to_uri.toString
    val doc = Jsoup.parse(html, baseUri)
    op(doc)
  }

  def login_very_crude {
    val request = Login << Map("auth_user" -> user, "auth_pw" -> pw)
    val html =  h(request as_str)
    val doc = Jsoup.parse(html)
    if (doc.getElementById("loginform") != null) {
      h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.clear()
    }
  }

  def login_failing {
    val request = Login << Map("auth_user" -> user, "auth_pw" -> pw)
    h(request </> { ns =>
      if ((ns \\ "div").find(n => (n \ "@id").text == "loginform").isDefined) {
        h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.clear()
      }
    })
  }

  def logout {
    h(Logout as_str)
    h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.clear()
  }

  def achievoCookie: Option[Cookie] = {
    h.client.asInstanceOf[ThreadSafeHttpClient].getCookieStore.getCookies.find(_.getName == "achievo")
  }

}

object Achievo extends Request(:/("ssl.linpro.no").secure  >\ "iso-8859-1") {
  def apply(user: String, pw: String) = new Achievo(user, pw)
}

object Login extends Request(Achievo / "achievo" / "index.php")

object Logout extends Request(Achievo / "achievo" / "index.php" <<? Map("atklogout" -> "-1"))

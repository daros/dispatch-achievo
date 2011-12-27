package org.daros

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Suite, AbstractSuite, Spec}
import xml.Null
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-25 13:08
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@RunWith(classOf[JUnitRunner])
class AchievoLogin extends Spec with ShouldMatchers with AchievoIntegration {
  describe("Achievo login") {
    it("should set cookie for correct credentials") {
      achievo.achievoCookie.isDefined should be(true)
    }

    it("the cookie should be 32  char long") {
      achievo.achievoCookie.get.getValue.size should be(32)
    }

  }
}
@RunWith(classOf[JUnitRunner])
class AchievoLogout extends Spec with ShouldMatchers with AchievoIntegration {
  describe("Achievo logout") {
    it("should clean old cookie and make room for a new") {
      val cookieBefore = achievo.achievoCookie.get.getValue
      achievo.logout
      achievo.login
      val cookieAfter =  achievo.achievoCookie.get.getValue

      cookieBefore should not be (cookieAfter)
    }
  }
}
@RunWith(classOf[JUnitRunner])
class AchievoLoginFail extends Spec with ShouldMatchers {
  describe("Achievo login") {
    it("should not set cookie for incorrect credentials") {
      var achievo: Achievo = null
      try {
        achievo = Achievo("wrong", "pw")
        achievo.achievoCookie.isDefined should be(false)
      } finally {
        achievo.logout
      }
    }
  }
}
@RunWith(classOf[JUnitRunner])
class ScalaTestBasic extends Spec with ShouldMatchers {

  describe("Basic test") {
    it("should return true") {
      (1 == 1) should equal(true)
    }

    it("should count characters") {
      "Hello".size should equal(5)
    }

    it("should have a pending example")(pending)
  }

}

trait AchievoIntegration extends AbstractSuite { this: Suite =>
  var achievo: Achievo = _

  abstract override def withFixture(test: NoArgTest) {
    try {
      import secret.Secret._
      achievo = Achievo(name, pw)

      println("user: " + achievo.user)
      println("cookie: " + achievo.achievoCookie.getOrElse("-"))

      super.withFixture(test)
    } catch {
      case e => println(e.getMessage)
    } finally {
      achievo.logout
    }
  }
}

package hu.bets.matches.web.api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class FootballMatchResourceTest extends WordSpec with FootballMatchResource with Matchers with ScalatestRouteTest {

  "The service" should {

    "return a an up and running message" in {
      Get("/matches/football/v1/info") ~> route ~> check {
        responseAs[String] shouldEqual "<html><body><h1>Football-matches service up and running</h1></body></html>"
      }
    }
  }
}

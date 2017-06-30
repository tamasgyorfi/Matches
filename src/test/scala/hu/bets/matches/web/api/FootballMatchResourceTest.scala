package hu.bets.matches.web.api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.junit.Test
import org.scalatest.{Matchers, WordSpec}

class FootballMatchResourceTest extends WordSpec with FootballMatchResource with Matchers with ScalatestRouteTest {

  @Test
  def infoEndpointReturnsUpAndRunningMessage(): Unit = {

    Get("/matches/football/v1/info") ~> route ~> check {
      responseAs[String] shouldEqual "<html><body><h1>Football-matches service up and running</h1></body></html>"
    }
  }

  @Test
  def schedulesEndpointShouldReturnNotImplementedResult(): Unit = {
    Get("/matches/football/v1/schedules?nrOfDays=14") ~> route ~> check {
      responseAs[String] shouldEqual "<html><body><h1>Not yet implemented.</h1></body></html>"
    }
  }

}

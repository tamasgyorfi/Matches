package hu.bets.matches.web.api

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import hu.bets.matches.actors.SceduledMatchesProviderActor
import hu.bets.matches.service.MatchesService
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
  def schedulesEndpointShouldReturnNSchedulesResult(): Unit = {

    val request = """{"token":"nwrur09w-nadjsfh0-jdasklj"}"""

    Post("/matches/football/v1/schedules", HttpEntity(ContentTypes.`application/json`, request.getBytes)) ~> route ~> check {
      responseAs[String] shouldEqual """{"matches":[{"matchId":"sr:match:11854534","date":{},"competition":"UEFA Champions League 17/18","homeTeam":{"name":"Alashkert","abbreviation":"ALA","nationality":"Armenia"},"awayTeam":{"name":"FC Santa Coloma","abbreviation":"FCC","nationality":"Andorra"}}, {"matchId":"sr:match:11854535","date":{},"competition":"UEFA Champions League 17/18","homeTeam":{"name":"Alashkert","abbreviation":"ALA","nationality":"Armenia"},"awayTeam":{"name":"FC Santa Coloma","abbreviation":"FCC","nationality":"Andorra"}}],"error":"","token":""}"""
    }
  }

  override def newScheduledMatchesProviderActor(): ActorRef = {
    system.actorOf(Props(new SceduledMatchesProviderActor(new TestMatchesService())))
  }

}

class FootballMatchResourceNegativeTest extends WordSpec with FootballMatchResource with Matchers with ScalatestRouteTest {

  @Test
  def shouldReturnErrorIfRequestTimesOut(): Unit = {
    val request = """{"token":"nwrur09w-nadjsfh0-jdasklj"}"""

    Post("/matches/football/v1/schedules", HttpEntity(ContentTypes.`application/json`, request.getBytes)) ~> route ~> check {
      responseAs[String] shouldEqual """{"matches":[], "error":"test value", "token":""}"""
    }
  }

  override def newScheduledMatchesProviderActor(): ActorRef = {
    throw new IllegalArgumentException("test value")
  }

}

class TestMatchesService extends MatchesService {
  override def getSchedules(): List[String] = List(
    """{"matchId":"sr:match:11854534","date":{},"competition":"UEFA Champions League 17/18","homeTeam":{"name":"Alashkert","abbreviation":"ALA","nationality":"Armenia"},"awayTeam":{"name":"FC Santa Coloma","abbreviation":"FCC","nationality":"Andorra"}}""",
    """{"matchId":"sr:match:11854535","date":{},"competition":"UEFA Champions League 17/18","homeTeam":{"name":"Alashkert","abbreviation":"ALA","nationality":"Armenia"},"awayTeam":{"name":"FC Santa Coloma","abbreviation":"FCC","nationality":"Andorra"}}""")
}


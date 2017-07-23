package hu.bets.matches.web

import java.util.concurrent.TimeUnit

import hu.bets.common.services.Services
import hu.bets.common.util.servicediscovery.EurekaFacade
import hu.bets.matches.model.{MatchResult, Result}
import org.junit.Test
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class MatchResultSenderTest extends MockitoSugar {

  @Test
  def sendMatchResultShouldSendCorrectPayloadToServiceEndpoint() :Unit = {
    val eurekaFacade = mock[EurekaFacade]
    val postRequestSender = mock[PostRequestSender]
    val payload = """{"results":[{"result":{"homeTeamName":"team1","awayTeamName":"team2","matchId":"id","competitionId":"id","homeTeamGoals":1,"awayTeamGoals":0}}],"token":"token-to-be-filled"}"""

    when(eurekaFacade.resolveEndpoint(Services.SCORES.getServiceName)).thenReturn("endpoint")

    val sut = new MatchResultSender(eurekaFacade, postRequestSender)
    sut.sendMatches(List(new MatchResult(new Result("team1", "team2", "id", "id", 1, 0))))
    TimeUnit.MILLISECONDS.sleep(500)

    verify(postRequestSender).runPost("endpoint/scores/football/v1/results", payload)
  }

}

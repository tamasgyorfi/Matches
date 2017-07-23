package hu.bets.matches.actors

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.gateway.{ApiKeyReader, MatchInfoGateway}
import hu.bets.matches.model.{MatchResult, Result}
import hu.bets.matches.web.{MatchResultSender, PostRequestSender}
import hu.bets.servicediscovery.EurekaFacadeImpl
import org.junit.{Ignore, Test}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class ResultsActorTest extends TestKit ( ActorSystem ( "Test" ) ) with ImplicitSender with MockitoSugar {


  @Ignore
  @Test
  def test(): Unit = {
    System.getProperties.setProperty ( "EUREKA_URL", "https://user:footballheureka@football-discovery-server.herokuapp.com/eureka/" )

    val keyReader = new ApiKeyReader
    val matchInfoGateway = new MatchInfoGateway ( keyReader )
    val matchResultSender = new MatchResultSender ( new EurekaFacadeImpl (EnvironmentVarResolver.getEnvVar("url")), new PostRequestSender )

    val sut = system.actorOf ( Props ( new ResultsActor ( matchInfoGateway, matchResultSender ) {
      private[actors] override def getResults: List[MatchResult] = {
        List ( MatchResult (Result( "Kayserispor", "Basaksehir FK", "sr:match:9792041", "Super Lig", 0, 1 ) ))
      }} ) )

    sut ! ResultsRequest
    TimeUnit.DAYS.sleep(1)
  }

  @Test
  def shouldCallDependenciesWhenCorrectMessageIsReceived() : Unit = {
    val matchInfoGateway = mock[MatchInfoGateway]
    val matchResultSender = mock[MatchResultSender]
    val matches = mock[List[MatchResult]]
    val sut = system.actorOf ( Props ( new ResultsActor ( matchInfoGateway, matchResultSender )))

    when(matchInfoGateway.getMatchResults).thenReturn(matches)

    sut ! ResultsRequest
    TimeUnit.MILLISECONDS.sleep(500)

    verify(matchInfoGateway).getMatchResults
    verify(matchResultSender).sendMatches(matches)
  }
}

package hu.bets.matches.actors

import akka.actor.Actor
import hu.bets.matches.gateway.MatchInfoGateway
import hu.bets.matches.model.MatchResult
import hu.bets.matches.web.MatchResultSender
import org.slf4j.{Logger, LoggerFactory}

case class ResultsRequest()

class ResultsActor(matchInfoGateway: MatchInfoGateway, matchResultSender: MatchResultSender) extends Actor {

  private val LOGGER: Logger = LoggerFactory.getLogger ( classOf [ResultsActor] )

  override def receive: Receive = {
    case ResultsRequest =>
      LOGGER info "Received message to deliver match results."
      sendResults ( getResults )
    case msg:Any => LOGGER info("Unknown message received: {}", msg)
  }

  private[actors] def getResults: List[MatchResult] = {
    LOGGER info "Trying to retrieve matches from the match service."
    matchInfoGateway.getMatchResults
  }

  def sendResults(results: List[MatchResult]): Unit = {
    LOGGER info("Sending results to scores-service: {}", results)
    matchResultSender.sendMatches(results)
  }
}

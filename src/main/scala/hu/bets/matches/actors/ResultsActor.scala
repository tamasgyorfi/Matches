package hu.bets.matches.actors

import akka.actor.Actor
import hu.bets.matches.gateway.MatchInfoGateway
import hu.bets.matches.model.MatchResult
import org.slf4j.{Logger, LoggerFactory}

case class ResultsRequest()

class ResultsActor(matchInfoGateway: MatchInfoGateway) extends Actor {

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[ResultsActor])

  override def receive: Receive = {
    case ResultsRequest => {
      sendResults(getResults)
    }
  }

  private def getResults: List[MatchResult] = {
    LOGGER info "Trying to retrieve matches from the match service."
    matchInfoGateway.getMatchResults
  }

  def sendResults(getResults: List[MatchResult]): Unit = {

  }

}

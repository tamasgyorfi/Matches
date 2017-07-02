package hu.bets.matches.actors

import akka.actor.Actor
import hu.bets.matches.model.ScheduledMatch
import hu.bets.matches.service.MatchesService
import org.apache.log4j.Logger

case class ScheduledMatchesRequest()

case class ScheduledMatchesResponse(scheduledMatches: List[ScheduledMatch], token: String)

class SceduledMatchesProviderActor(matchesService: MatchesService) extends Actor {

  private val LOGGER: Logger = Logger.getLogger(classOf[SceduledMatchesProviderActor])

  override def receive: Receive = {
    case ScheduledMatchesRequest => sender ! ScheduledMatchesResponse(matchesService.getSchedules(), "")
    case msg: Any => LOGGER.info("Unknown message received. " + msg)
  }
}

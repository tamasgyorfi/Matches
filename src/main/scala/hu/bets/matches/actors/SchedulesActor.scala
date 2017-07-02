package hu.bets.matches.actors

import akka.actor.Actor
import hu.bets.matches.dataaccess.SchedulesDao
import hu.bets.matches.gateway.{MatchInfoGateway, ScheduleRetrievalException}
import hu.bets.matches.model.ScheduledMatch
import org.apache.log4j.Logger

case class ScheduleRequest() {}

class SchedulesActor(matchInfoGateway: MatchInfoGateway, schedulesDao: SchedulesDao) extends Actor {

  private val LOGGER: Logger = Logger.getLogger(classOf[SchedulesActor])

  override def receive: Receive = {
    case ScheduleRequest =>
      try {
        saveSchedules(getSchedules)
      } catch {
        case e: ScheduleRetrievalException => LOGGER error("Exception while trying to retrieve schedules. ", e)
        case ex: Exception => LOGGER error("Exception while trying to save schedules. ", ex)
      }

    case message: Any => LOGGER info "Unknown message received: " + message
  }

  private def getSchedules: List[ScheduledMatch] = {
    val scheduledMatches = matchInfoGateway getScheduledMatches 7
    LOGGER info "Retrieved the following matches from the third party cervice: " + scheduledMatches

    scheduledMatches
  }

  private def saveSchedules(scheduledMatches: List[ScheduledMatch]) = {
    val failedSchedules = schedulesDao.saveSchedules(scheduledMatches)
    LOGGER info "The following schedules could not be saved into the database: " + failedSchedules

    failedSchedules
  }
}

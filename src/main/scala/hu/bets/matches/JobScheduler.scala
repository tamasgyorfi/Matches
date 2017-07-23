package hu.bets.matches

import java.time._

import akka.actor.ActorRef
import hu.bets.matches.actors.ScheduleRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class JobScheduler(schedulesActor: ActorRef, resultsActor: ActorRef) {

  private val ZONE = ZoneId.of("Europe/London")

  private[matches] def scheduleScheduleRetrieval: Unit = AkkaSingletons.getScheduler.schedule ( 0 hours, 3 hours, schedulesActor, ScheduleRequest )
  private[matches] def scheduleResultsRetrieval: Unit = {
    val initialDelay = calculateInitialDelay()
    AkkaSingletons.getScheduler.schedule ( initialDelay millis, 24 hours, schedulesActor, ScheduleRequest )
  }

  private[matches] def calculateInitialDelay(): Long = {
    val now = ZonedDateTime.now(ZONE)
    val tomorrowStart = now.toLocalDate.plusDays(1).atStartOfDay(ZONE)

    java.time.Duration.between( now , tomorrowStart ).toMillis()
  }

  def scheduleAll(): Unit = {
    scheduleScheduleRetrieval
    scheduleResultsRetrieval
  }
}

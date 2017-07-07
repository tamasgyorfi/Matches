package hu.bets.matches

import akka.actor.ActorRef
import hu.bets.matches.actors.ScheduleRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class JobScheduler(schedulesActor: ActorRef, resultsActor: ActorRef) {

  def schedule(): Unit = {
    AkkaSingletons.getScheduler.schedule(0 hours, 3 hours, schedulesActor, ScheduleRequest())
    AkkaSingletons.getScheduler.schedule(0 hours, 3 hours, null)
  }
}

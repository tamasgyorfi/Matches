package hu.bets.matches

import akka.actor.ActorRef

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class JobScheduler(matchSchedulesActor: ActorRef) {

  def schedule(): Unit = {
    AkkaSingletons.getScheduler.schedule(3 hours, 3 hours, null)
  }
}

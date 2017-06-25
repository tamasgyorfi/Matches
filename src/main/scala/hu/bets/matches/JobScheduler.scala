package hu.bets.matches

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class JobScheduler(matchSchedulesActor: ActorRef) {

  private val actorSystem = ActorSystem()
  private val scheduler = actorSystem.scheduler

  def schedule(): Unit = {
    scheduler.schedule(3 hours, 3 hours, null)
  }
}

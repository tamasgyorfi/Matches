package hu.bets.matches

import akka.actor.ActorSystem
import hu.bets.matches.gateway.MatchInfoGateway
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class JobScheduler(matchInfoGateway: MatchInfoGateway) {

  val actorSystem = ActorSystem()
  val scheduler = actorSystem.scheduler

  def schedule(): Unit = {
    scheduler.schedule(3 hours, 3 hours, null)
  }
}

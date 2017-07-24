package hu.bets.matches

import akka.actor.{ActorSystem, Scheduler}
import akka.stream.ActorMaterializer

object AkkaSingletons {

  private implicit val actorSystem = ActorSystem("matches-service")
  private implicit val materializer = ActorMaterializer()
  private val scheduler = actorSystem.scheduler

  def getScheduler : Scheduler = scheduler
  def getActorSystem: ActorSystem = actorSystem
  def getMaterializer: ActorMaterializer = materializer
}

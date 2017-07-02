package hu.bets.matches

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object AkkaSingletons {

  private implicit val actorSystem = ActorSystem("matches-service")
  private implicit val materializer = ActorMaterializer()
  private val scheduler = actorSystem.scheduler

  def getScheduler = scheduler

  def getActorSystem = actorSystem

  def getMaterializer = materializer
}

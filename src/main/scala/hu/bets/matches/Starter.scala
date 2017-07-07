package hu.bets.matches

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.Http
import hu.bets.matches.actors.SceduledMatchesProviderActor
import hu.bets.matches.config.ApplicationConfig
import hu.bets.matches.web.api.FootballMatchResource
import org.slf4j.{Logger, LoggerFactory}

private class Starter extends FootballMatchResource {

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[Starter])

  private implicit val system = AkkaSingletons.getActorSystem
  private implicit val materializer = AkkaSingletons.getMaterializer
  private implicit val executionContext = system.dispatcher

  private val applicationConfig: ApplicationConfig = new ApplicationConfig

  def run() {
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080);//EnvironmentVarResolver.getEnvVar("HOST"), EnvironmentVarResolver.getEnvVar("PORT").toInt)

    LOGGER.info("Matches service up and running.")

    while (true) {
      // don't let it terminate
    }

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  override def newScheduledMatchesProviderActor: ActorRef = AkkaSingletons.getActorSystem.actorOf(Props(new SceduledMatchesProviderActor(applicationConfig.getMatchService)))
}

object Starter extends App {
  override def main(args: Array[String]): Unit = {
    new Starter().run()
  }
}

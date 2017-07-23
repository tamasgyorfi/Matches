package hu.bets.matches

import akka.actor.ActorRef
import akka.http.scaladsl.Http
import hu.bets.common.services.Services
import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.config.ApplicationConfig
import hu.bets.matches.web.api.FootballMatchResource
import org.slf4j.{Logger, LoggerFactory}

private class Starter extends FootballMatchResource with ApplicationConfig {

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[Starter])

  private implicit val system = AkkaSingletons.getActorSystem
  private implicit val materializer = AkkaSingletons.getMaterializer
  private implicit val executionContext = system.dispatcher

  def run(): Unit = {
    val eurekaClient = BasicAppConfig.getEurekaClient
    val reg = eurekaClient.registerNonBlockingly(Services.MATCHES.getServiceName)
    val hook = sys.addShutdownHook(() => eurekaClient.unregister())

    run(getJobsScheduler)
  }

  private def run(jobScheduler: JobScheduler) {
    LOGGER.info("Initializing the web server.")
    val bindingFuture = Http().bindAndHandle(route, EnvironmentVarResolver.getEnvVar("HOST"), EnvironmentVarResolver.getEnvVar("PORT").toInt)
    jobScheduler.scheduleAll()

    LOGGER.info("Matches service up and running.")

    while (true) {
      // don't let it terminate
    }

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  override def newScheduledMatchesProviderActor(): ActorRef = getSchedulesProvider
}

object Starter extends App {
  override def main(args: Array[String]): Unit = {
    new Starter().run()
  }
}

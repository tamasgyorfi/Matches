package hu.bets.matches

import akka.http.scaladsl.Http
import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.web.api.FootballMatchResource
import org.apache.log4j.Logger

private class Starter extends FootballMatchResource {

  private val LOGGER: Logger = Logger.getLogger(classOf[Starter])

  private implicit val system = AkkaSingletons.getACtorSystem
  private implicit val materializer = AkkaSingletons.getMaterializer
  private implicit val executionContext = system.dispatcher

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
}

object Starter extends App {
  override def main(args: Array[String]): Unit = {
    new Starter().run()
  }
}

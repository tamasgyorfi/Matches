package hu.bets.matches.web.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._

trait FootballMatchResource {

  val route =

    pathPrefix("matches" / "football" / "v1") {
      get {
        path("info") {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><body><h1>Football-matches service up and running</h1></body></html>"))
        }
      } ~
        get {
          path("schedules") {
            parameters('nrOfDays) { (nrOfDays) => {
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><body><h1>Not yet implemented.</h1></body></html>"))
            }
            }
          }
        }
    }
}
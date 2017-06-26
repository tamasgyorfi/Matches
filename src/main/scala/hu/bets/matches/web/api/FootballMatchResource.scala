package hu.bets.matches.web.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._

trait FootballMatchResource {

  val route =
    pathPrefix("matches" / "football" / "v1") {
      path("info") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><body><h1>Football-matches service up and running</h1></body></html>"))
        }
      }
    }

}

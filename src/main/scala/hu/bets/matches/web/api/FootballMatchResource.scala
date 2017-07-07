package hu.bets.matches.web.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import hu.bets.matches.actors.{ScheduledMatchesRequest, ScheduledMatchesResponse}
import net.liftweb.json.DefaultFormats

import scala.concurrent.Await
import scala.concurrent.duration._

trait FootballMatchResource {

  private case class SecureScheduleRequest(token: String)

  private implicit val formats = DefaultFormats

  implicit val timeout = Timeout(5 seconds)
  val route =

    pathPrefix("matches" / "football" / "v1") {
      get {
        path("info") {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<html><body><h1>Football-matches service up and running</h1></body></html>"))
        }
      } ~
        post {
          path("schedules") {
            entity(as[String]) { request => {
              val resultFuture = newScheduledMatchesProviderActor() ? ScheduledMatchesRequest
              val result = Await.result(resultFuture, timeout.duration).asInstanceOf[ScheduledMatchesResponse]

              val retVal = """{"matches":[""" + result.scheduledMatches.mkString(", ") +"""],"token":"""" + result.token +""""}"""
              complete(HttpEntity(ContentTypes.`application/json`, retVal))
            }
            }
          }
        }
    }

  def newScheduledMatchesProviderActor(): ActorRef
}
package hu.bets.matches.web

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import hu.bets.matches.AkkaSingletons

class PostRequestSender {

  private implicit val materializer = AkkaSingletons.getMaterializer

  private def makePost(serviceEndpoint: String, payload: String): HttpRequest = {
    val searchObjectEntity = HttpEntity ( ContentTypes.`application/json`, payload )
    HttpRequest ( HttpMethods.POST, serviceEndpoint, List (), searchObjectEntity )
  }

  private def sendPost(httpRequest: HttpRequest) = {
    val http = Http ( AkkaSingletons.getActorSystem )
    http.singleRequest ( httpRequest )
  }

  def runPost(serviceEndpoint: String, payload: String): Unit = {
    sendPost ( makePost ( serviceEndpoint, payload ) )
  }
}

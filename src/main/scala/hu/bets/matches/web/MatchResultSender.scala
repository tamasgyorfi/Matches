package hu.bets.matches.web

import hu.bets.common.services.Services
import hu.bets.common.util.servicediscovery.EurekaFacade
import hu.bets.matches.AkkaSingletons
import hu.bets.matches.model.{MatchResult, MatchResultToScoreService}
import net.liftweb.json.Extraction._
import net.liftweb.json.compactRender
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}


class MatchResultSender(eurekaFacade: EurekaFacade, postRequestSender: PostRequestSender) {

  private implicit val formats = net.liftweb.json.DefaultFormats
  private val SCORES_POST_BET_ENDPOINT = "/scores/football/v1/results"
  private val LOGGER = LoggerFactory.getLogger ( classOf [MatchResultSender] )

  def sendMatches(matches: List[MatchResult]): Unit = {
    val scoresServiceBase = eurekaFacade.resolveEndpoint ( Services.SCORES.getServiceName )
    val finalServiceName = scoresServiceBase + SCORES_POST_BET_ENDPOINT

    Future {
      blocking {
        try {
          val payload = compactRender ( decompose ( MatchResultToScoreService ( matches, "token-to-be-filled" ) ) )
          postRequestSender.runPost ( finalServiceName, payload )
          LOGGER.info ( "Successfully sent payload to scores service: {}", payload )
        } catch {
          case e: Exception => LOGGER.error ( "Unable to send payload to the scores service: " + matches, e )
        }
      }
    }
  }
}

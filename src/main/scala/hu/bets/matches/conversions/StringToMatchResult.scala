package hu.bets.matches.conversions

import com.jayway.jsonpath.{Configuration, JsonPath}
import hu.bets.matches.model.{MatchResult, Result}
import net.minidev.json.JSONArray
import org.slf4j.{Logger, LoggerFactory}

object StringToMatchResult {

  private val HOME_GOALS = "$.home_score"
  private val AWAY_GOALS = "$.away_score"
  private val MATCH_STATUS = "$.match_status"
  private val STATUS = "$.status"
  private val SPORT_EVENT = "$.sport_event"
  private val SPORT_EVENT_STATUS = "$.sport_event_status"


  private val LOGGER: Logger = LoggerFactory.getLogger ( "StringToMatchResult" )

  implicit def stringToMatchResults(payload: String): List[Option[MatchResult]] = {
    if (payload.contains ( "No results for this date." )) {
      List ( None )
    } else {
      convert ( payload )
    }
  }

  private def convert(payload: String): List[Option[MatchResult]] = {
    val document = Configuration.defaultConfiguration.jsonProvider.parse ( payload )
    val eventsArray: JSONArray = JsonPath.read ( document, "$.results" )

    Range ( 0, eventsArray.size () ).map ( index => {
      try {
        val event: Object = JsonPath.read ( eventsArray.get ( index ), SPORT_EVENT )
        val status: Object = JsonPath.read ( eventsArray.get ( index ), SPORT_EVENT_STATUS )

        createMatchResult ( event, status )
      } catch {
        case e: Exception =>
          LOGGER.info ( "Exception caught", e )
          None
      }
    } ).toList
  }

  def createMatchResult(event: Object, status: Object): Option[MatchResult] = {
    if (matchEnded ( status )) {
      val game = MatchConverter.getMatch ( event )
      Some ( MatchResult ( Result(game.homeTeam.name, game.awayTeam.name,
        game.matchId, game.competition,
        JsonPath.read ( status, HOME_GOALS ).toString.toInt,
        JsonPath.read ( status, AWAY_GOALS ).toString.toInt ) ))

    } else None
  }

  def matchEnded(doc: Object): Boolean = {
    ("ended" equals JsonPath.read ( doc, MATCH_STATUS ).toString) && ("closed" equals JsonPath.read ( doc, STATUS ).toString)
  }
}

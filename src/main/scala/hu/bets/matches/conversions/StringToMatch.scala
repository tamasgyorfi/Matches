package hu.bets.matches.conversions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.jayway.jsonpath.{Configuration, JsonPath}
import hu.bets.matches.model.{ScheduledMatch, Team}
import net.minidev.json.JSONArray
import org.apache.log4j.Logger

object StringToMatch {

  private val LOGGER: Logger = Logger.getLogger("StringToMatch")

  implicit def stringToMatches(payload: String): List[Option[ScheduledMatch]] = {
    val document = Configuration.defaultConfiguration.jsonProvider.parse(payload)
    val eventsArray: JSONArray = JsonPath.read(document, "$.sport_events")

    Range(0, eventsArray.size() - 1).map(index => {
      try {
        val game = getMatch(eventsArray.get(index))
        LOGGER.info("Read match from JSON: " + game)

        Some(game)
      } catch {
        case e: Exception =>
          LOGGER.info("Exception caught", e)
          Option.empty
      }
    }).toList
  }

  private def getTeam(document: Object) = {
    Team(JsonPath.read(document, "$.name").toString,
      JsonPath.read(document, "$.abbreviation").toString,
      JsonPath.read(document, "$.country").toString)
  }

  private def getMatch(document: Object) = {
    ScheduledMatch(JsonPath.read(document, "$.id").toString,
      LocalDateTime.parse(JsonPath.read(document, "$.scheduled").toString, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
      JsonPath.read(document, "$.season.name").toString,
      getTeam(JsonPath.read(document, "$.competitors[0]")),
      getTeam(JsonPath.read(document, "$.competitors[1]")))
  }
}

package hu.bets.matches.conversions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.jayway.jsonpath.{Configuration, JsonPath}
import hu.bets.matches.model.{ScheduledMatch, Team}
import net.minidev.json.JSONArray
import org.slf4j.{Logger, LoggerFactory}

object StringToMatch {

  private val TEAM_NAME = "$.name"
  private val SCHEDULE_ID = "$.id"
  private val DATE = "$.scheduled"
  private val COMPETITION = "$.season.name"
  private val ABBREVIATION = "$.abbreviation"
  private val COUNTRY = "$.country"
  private val COMPETITORS = "$.competitors[%d]"

  private val LOGGER: Logger = LoggerFactory.getLogger("StringToMatch")

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

  implicit def stringToLocalDateTime(s: String): LocalDateTime = {
    LocalDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  }

  private def getTeam(document: Object) = {
    Team(JsonPath.read(document, TEAM_NAME).toString,
      JsonPath.read(document, ABBREVIATION).toString,
      JsonPath.read(document, COUNTRY).toString)
  }

  private def getMatch(document: Object) = {
    ScheduledMatch(
      JsonPath.read(document, SCHEDULE_ID).toString,
      JsonPath.read(document, DATE).toString,
      JsonPath.read(document, COMPETITION).toString,
      getTeam(JsonPath.read(document, COMPETITORS.format(0))),
      getTeam(JsonPath.read(document, COMPETITORS.format(1))))
  }
}
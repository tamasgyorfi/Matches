package hu.bets.matches.conversions

import com.jayway.jsonpath.{Configuration, JsonPath}
import hu.bets.matches.model.ScheduledMatch
import net.minidev.json.JSONArray
import org.slf4j.{Logger, LoggerFactory}

object StringToScheduledMatch {

  private val EVENTS_ARRAY = "$.sport_events"
  private val LOGGER: Logger = LoggerFactory.getLogger("StringToScheduledMatch")

  implicit def stringToMatches(payload: String): List[Option[ScheduledMatch]] = {

    if (payload.contains("No events scheduled for this date.")) {
      List(None)
    } else {
      convert(payload)
    }
  }

  private def convert(payload: String): List[Option[ScheduledMatch]] = {
    val document = Configuration.defaultConfiguration.jsonProvider.parse(payload)
    val eventsArray: JSONArray = JsonPath.read(document, EVENTS_ARRAY)

    Range(0, eventsArray.size()).map(index => {
      try {
        val game = MatchConverter.getMatch(eventsArray.get(index))
        LOGGER.info("Read match from JSON: " + game)

        Some(game)
      } catch {
        case e: Exception =>
          LOGGER.info("Exception caught", e)
          Option.empty
      }
    }).toList
  }
}
package hu.bets.matches.conversions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.jayway.jsonpath.JsonPath
import hu.bets.matches.model.{ScheduledMatch, Team}

object MatchConverter {

  private val TEAM_NAME = "$.name"
  private val SCHEDULE_ID = "$.id"
  private val DATE = "$.scheduled"
  private val COMPETITION = "$.season.name"
  private val ABBREVIATION = "$.abbreviation"
  private val COUNTRY = "$.country"
  private val COMPETITORS = "$.competitors[%d]"

  implicit def stringToLocalDateTime(s: String): LocalDateTime = {

    LocalDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  }

  private def getTeam(document: Object) = {
    Team(JsonPath.read(document, TEAM_NAME).toString,
      JsonPath.read(document, ABBREVIATION).toString,
      JsonPath.read(document, COUNTRY).toString)
  }

  def getMatch(document: Object): ScheduledMatch = {
    ScheduledMatch(
      JsonPath.read(document, SCHEDULE_ID).toString,
      JsonPath.read(document, DATE).toString,
      JsonPath.read(document, COMPETITION).toString,
      getTeam(JsonPath.read(document, COMPETITORS.format(0))),
      getTeam(JsonPath.read(document, COMPETITORS.format(1))))
  }
}

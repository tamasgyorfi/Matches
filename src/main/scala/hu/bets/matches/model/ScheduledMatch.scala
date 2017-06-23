package hu.bets.matches.model

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.{JsonFormat, JsonIgnoreProperties, JsonProperty, JsonUnwrapped}

@JsonIgnoreProperties(ignoreUnknown = true)
case class ScheduledMatch(@JsonProperty("id") @JsonUnwrapped matchId: String,
                          @JsonProperty("scheduled") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss+ss:ss") date: LocalDateTime, homeTeam: Team, awayTeam: Team) {
  override def toString: String = "{ matchId: " + matchId + " date: " + date + " homeTeam: " + homeTeam + " awayTeam: " + awayTeam;
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class ScheduledMatches(@JsonProperty("sport_events") matches: List[ScheduledMatch])

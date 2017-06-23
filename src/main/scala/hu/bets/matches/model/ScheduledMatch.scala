package hu.bets.matches.model

import java.time.LocalDateTime

case class ScheduledMatch(matchId: String,
                          date: LocalDateTime,
                          competition: String,
                          homeTeam: Team,
                          awayTeam: Team) {

  override def toString: String = "{ matchId: " + matchId + " date: " + date + " competition: " + competition + " homeTeam: " + homeTeam + " awayTeam: " + awayTeam + "}";

}
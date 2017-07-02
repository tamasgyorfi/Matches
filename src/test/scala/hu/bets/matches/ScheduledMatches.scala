package hu.bets.matches

import java.time.LocalDateTime

import hu.bets.matches.model.{ScheduledMatch, Team}

object ScheduledMatches {

  val scheduledMatch1 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
  val scheduledMatch2 = ScheduledMatch("sr:match:11854535", LocalDateTime.parse("2017-06-28T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
  val scheduledMatch3 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-29T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))

}

package hu.bets.matches

import java.time.LocalDateTime

import hu.bets.matches.model.{ScheduledMatch, Team}

object ScheduledMatches {

  val scheduledMatch1 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
  val scheduledMatch2 = ScheduledMatch("sr:match:11854535", LocalDateTime.parse("2017-06-28T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
  val scheduledMatch3 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-29T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))

  val result = List("""{"matchId":"sr:match:11854535","date":{"date":{"year":2017,"month":6,"day":28},"time":{"hour":16,"minute":0,"second":0,"nano":0}},"competition":"UEFA Champions League 17/18","homeTeam":{"name":"Alashkert","abbreviation":"ALA","nationality":"Armenia"},"awayTeam":{"name":"FC Santa Coloma","abbreviation":"FCC","nationality":"Andorra"}}""", """{"matchId":"sr:match:11854534","date":{"date":{"year":2017,"month":6,"day":27},"time":{"hour":16,"minute":0,"second":0,"nano":0}},"competition":"UEFA Champions League 17/18","homeTeam":{"name":"Alashkert","abbreviation":"ALA","nationality":"Armenia"},"awayTeam":{"name":"FC Santa Coloma","abbreviation":"FCC","nationality":"Andorra"}}""")
}

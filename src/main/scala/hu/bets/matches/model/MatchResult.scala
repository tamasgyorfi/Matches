package hu.bets.matches.model

case class MatchResult(scheduledMatch: ScheduledMatch, homeTeamGoals: Int, awayTeamGoals: Int) {

  override def toString: String = "{homeTeamgoals: " + homeTeamGoals + ", awayTeamGoals: " + awayTeamGoals + ", " + scheduledMatch.toString + "}"
}

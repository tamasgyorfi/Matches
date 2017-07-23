package hu.bets.matches.model

case class Result(homeTeamName: String, awayTeamName: String, matchId: String, competitionId: String,
                  homeTeamGoals: Int, awayTeamGoals: Int) {

  override def toString: String = "{homeTeamgoals: " + homeTeamGoals + ", awayTeamGoals: " + awayTeamGoals + ", " +
    "homeTeamName: " + homeTeamName + ", awayTeamName:" + awayTeamName + ", matchId: " + matchId + ", competitionId: " + competitionId + "}"
}
case class MatchResult(result: Result) {
}

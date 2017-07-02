package hu.bets.matches.service

import hu.bets.matches.model.ScheduledMatch

trait MatchesService {

  def getSchedules(): List[ScheduledMatch]

}

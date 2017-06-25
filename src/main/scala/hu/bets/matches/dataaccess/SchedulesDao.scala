package hu.bets.matches.dataaccess

import hu.bets.matches.model.ScheduledMatch

trait SchedulesDao {

  /**
    * Saves a number of scheduled matches into the database.
    *
    * @param schedules
    * @return the scheduled matches which could not be saved.
    */
  def saveSchedules(schedules: List[ScheduledMatch]): List[ScheduledMatch]
}

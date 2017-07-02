package hu.bets.matches.dataaccess

import hu.bets.matches.model.ScheduledMatch

trait SchedulesDao {

  /**
    * Saves a number of scheduled matches into the data store.
    *
    * @param schedules the sechedules to be stored
    * @return the scheduled matches which could not be saved.
    */
  def saveSchedules(schedules: List[ScheduledMatch]): List[ScheduledMatch]

  /**
    * Returns the schedules for the upcoming days.
    *
    * @return schedules for the next days
    */
  def getAvailableSchedules: List[ScheduledMatch]
}

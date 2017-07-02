package hu.bets.matches.service

import hu.bets.matches.dataaccess.SchedulesDao
import hu.bets.matches.model.ScheduledMatch
import org.apache.log4j.Logger

class DefaultMatchesService(schedulesDao: SchedulesDao) extends MatchesService {

  private def LOGGER: Logger = Logger.getLogger(classOf[DefaultMatchesService])

  override def getSchedules(): List[ScheduledMatch] = {
    LOGGER.info("Incoming request for available schedules.")
    schedulesDao.getAvailableSchedules
  }
}

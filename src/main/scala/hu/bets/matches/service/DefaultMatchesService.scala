package hu.bets.matches.service

import hu.bets.matches.dataaccess.SchedulesDao
import org.slf4j.{Logger, LoggerFactory}

class DefaultMatchesService(schedulesDao: SchedulesDao) extends MatchesService {

  private def LOGGER: Logger = LoggerFactory.getLogger(classOf[DefaultMatchesService])

  override def getSchedules(): List[String] = {
    LOGGER.info("Incoming request for available schedules.")
    schedulesDao.getAvailableSchedules
  }
}

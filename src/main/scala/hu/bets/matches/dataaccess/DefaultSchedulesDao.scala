package hu.bets.matches.dataaccess

import com.google.gson.Gson
import hu.bets.matches.model.ScheduledMatch
import org.apache.log4j.Logger
import redis.clients.jedis.{Jedis, JedisPool}

import scala.collection.JavaConverters._

class DefaultSchedulesDao(jedisPool: JedisPool) extends SchedulesDao {

  private val LOGGER: Logger = Logger.getLogger(classOf[DefaultSchedulesDao])
  private val SCHEDULES_DB = 0

  /**
    * Saves a number of scheduled matches into the database.
    *
    * @param schedules the scheduled matches
    * @return the scheduled matches which could not be saved.
    */
  override def saveSchedules(schedules: List[ScheduledMatch]): List[ScheduledMatch] = {
    clearCache()
    saveScheduledMatches(schedules)
  }

  /**
    * Returns the schedules for the next days.
    *
    * @return schedules for the next days
    */
  override def getAvailableSchedules: List[ScheduledMatch] = {
    val jedis = getJedisForSchedules
    val keys = jedis.keys("*").asScala

    val jsonObjects = for (key <- keys) yield jedis.get(key)
    jedis.close()

    jsonObjects.map(jsonObject => new Gson().fromJson(jsonObject, classOf[ScheduledMatch])).toList
  }

  private def clearCache() = {
    val jedis = getJedisForSchedules
    jedis.flushDB()

    jedis.close()
  }

  private def saveScheduledMatches(schedules: List[ScheduledMatch]): List[ScheduledMatch] = {

    val jedis = getJedisForSchedules

    var retVal: List[ScheduledMatch] = List()
    schedules.foreach(scheduledMatch => {
      try {
        jedis.set(scheduledMatch.matchId, new Gson().toJson(scheduledMatch))
      } catch {
        case e: Exception => {
          LOGGER.error("Exception caught while trying to save schedules. ", e)
          retVal = retVal.::(scheduledMatch)
        }
      }
    })
    jedis.close()

    retVal
  }

  private def getJedisForSchedules: Jedis = {
    val jedis = jedisPool.getResource
    jedis.select(SCHEDULES_DB)

    jedis
  }
}

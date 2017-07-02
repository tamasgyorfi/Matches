package hu.bets.matches.dataaccess

import java.util.concurrent.TimeUnit

import com.google.gson.Gson
import hu.bets.matches.model.ScheduledMatch
import org.apache.log4j.Logger
import org.redisson.api.RReadWriteLock
import redis.clients.jedis.{Jedis, JedisPool}

import scala.collection.JavaConverters._
import scala.util.control.Breaks._

class DefaultSchedulesDao(jedisPool: JedisPool, lock: RReadWriteLock) extends SchedulesDao {

  private implicit val formats = net.liftweb.json.DefaultFormats

  private val GSON: Gson = new Gson()
  private val LOGGER: Logger = Logger.getLogger(classOf[DefaultSchedulesDao])
  private val SCHEDULES_DB = 0
  private val LOCK_TIMEOUT = 1000
  private val SCHEDULES_KEY_PREFIX = "SCHEDULE:"
  private val NR_OF_RETRIES = 3

  /**
    * Saves a number of scheduled matches into the database.
    *
    * @param schedules the scheduled matches
    * @return the scheduled matches which could not be saved.
    */
  override def saveSchedules(schedules: List[ScheduledMatch]): List[ScheduledMatch] = {
    val jedis: Jedis = getJedis
    val writeLock = lock.writeLock()

    if (writeLock.tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
      try {
        clearCache(jedis)
        val results = saveScheduledMatches(schedules, jedis)
        results
      } finally {
        writeLock.unlock()
        jedis.close()
      }
    } else {
      List()
    }
  }

  /**
    * Returns the schedules for the next days.
    *
    * @return schedules for the next days
    */
  override def getAvailableSchedules: List[ScheduledMatch] = {
    var result: List[ScheduledMatch] = List()
    breakable {
      for (i <- 1 to NR_OF_RETRIES) {
        result = getSchedules()
        if (!result.isEmpty) {
          break
        }
      }
    }

    result
  }

  private def getSchedules(): List[ScheduledMatch] = {
    val jedis: Jedis = getJedis
    val readLock = lock.readLock()

    if (readLock.tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
      try {
        val keys = jedis.keys(SCHEDULES_KEY_PREFIX + "*").asScala
        val jsonObjects = for (key <- keys) yield jedis.get(key)

        jedis.close()
        jsonObjects.map(jsonObject => GSON.fromJson(jsonObject, classOf[ScheduledMatch])).toList
      } finally {
        readLock.unlock()
        jedis.close()
      }
    } else {
      List()
    }
  }

  private def clearCache(jedis: Jedis)

  = {
    jedis.flushDB()
  }

  private def saveScheduledMatches(schedules: List[ScheduledMatch], jedis: Jedis): List[ScheduledMatch]

  = {

    var retVal: List[ScheduledMatch] = List()
    schedules.foreach(scheduledMatch => {
      try {
        jedis.set(SCHEDULES_KEY_PREFIX + scheduledMatch.matchId, GSON.toJson(scheduledMatch))
      } catch {
        case e: Exception =>
          LOGGER.error("Exception caught while trying to save schedules. ", e)
          retVal = retVal.::(scheduledMatch)
      }
    })

    retVal
  }

  private def getJedis: Jedis = {
    val jedis = jedisPool.getResource
    jedis.select(SCHEDULES_DB)

    jedis
  }
}

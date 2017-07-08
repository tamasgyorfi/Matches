package hu.bets.matches.dataaccess

import java.util.concurrent.TimeUnit

import com.google.gson.Gson
import hu.bets.matches.model.ScheduledMatch
import org.redisson.api.RReadWriteLock
import org.slf4j.{Logger, LoggerFactory}
import redis.clients.jedis.{Jedis, JedisPool}

import scala.collection.JavaConverters._
import scala.util.control.Breaks._

class DefaultSchedulesDao(jedisPool: JedisPool, lock: RReadWriteLock) extends SchedulesDao {

  private implicit val formats = net.liftweb.json.DefaultFormats

  private val GSON: Gson = new Gson()
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[DefaultSchedulesDao])
  private val SCHEDULES_DB = 0
  private val LOCK_TIMEOUT = 1000
  private val SCHEDULES_KEY = "SCHEDULES"
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
  override def getAvailableSchedules: List[String] = {
    var result: List[String] = List()
    breakable {
      for (_ <- 1 to NR_OF_RETRIES) {
        result = getSchedules
        if (result.nonEmpty) {
          break
        }
      }
    }

    result
  }

  private def getSchedules: List[String] = {
    val jedis: Jedis = getJedis
    val readLock = lock.readLock()

    if (readLock.tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
      LOGGER.info("Redis locked for reading.")
      try {
        val values = jedis.smembers(SCHEDULES_KEY).asScala
        LOGGER.info("Redis values: {}", values)
        values.toList
      } finally {
        readLock.unlock()
        jedis.close()
      }
    } else {
      List()
    }
  }

  private def clearCache(jedis: Jedis) = {
    jedis.flushDB()
  }

  private def saveScheduledMatches(schedules: List[ScheduledMatch], jedis: Jedis): List[ScheduledMatch] = {

    var retVal: List[ScheduledMatch] = List()
    schedules.foreach(scheduledMatch => {
      try {
        jedis.sadd(SCHEDULES_KEY, GSON.toJson(scheduledMatch))
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

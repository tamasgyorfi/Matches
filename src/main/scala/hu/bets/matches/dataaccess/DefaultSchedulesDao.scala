package hu.bets.matches.dataaccess

import java.util.concurrent.TimeUnit

import com.google.gson.Gson
import hu.bets.matches.model.ScheduledMatch
import org.apache.log4j.Logger
import org.redisson.api.RedissonClient
import redis.clients.jedis.{Jedis, JedisPool}

import scala.collection.JavaConverters._
import scala.util.control.Breaks._

class DefaultSchedulesDao(jedisPool: JedisPool, redissonClient: RedissonClient) extends SchedulesDao {

  private implicit val formats = net.liftweb.json.DefaultFormats

  private val GSON: Gson = new Gson()
  private val LOGGER: Logger = Logger.getLogger(classOf[DefaultSchedulesDao])
  private val SCHEDULES_DB = 0
  private val LOCK_TIMEOUT = 1000
  private val SCHEDULES_LOCK_NAME = "schedules_lock"
  private val SCHEDULES_KEY_PREFIX = "SCHEDULE:"

  /**
    * Saves a number of scheduled matches into the database.
    *
    * @param schedules the scheduled matches
    * @return the scheduled matches which could not be saved.
    */
  override def saveSchedules(schedules: List[ScheduledMatch]): List[ScheduledMatch] = {
    getJedisForSchedules match {
      case Some(jedis) =>
        clearCache(jedis)
        val results = saveScheduledMatches(schedules, jedis)
        jedis.close()
        results
      case None =>
        LOGGER.info("Unable to acquire the Redis lock. saveSchedules() completes without refreshing schedules.")
        List()
    }
  }

  /**
    * Returns the schedules for the next days.
    *
    * @return schedules for the next days
    */
  override def getAvailableSchedules: List[ScheduledMatch] = {
    retryGetLock(3) match {
      case Some(jedis) =>
        val keys = jedis.keys(SCHEDULES_KEY_PREFIX + "*").asScala
        val jsonObjects = for (key <- keys) yield jedis.get(key)

        jedis.close()
        jsonObjects.map(jsonObject => GSON.fromJson(jsonObject, classOf[ScheduledMatch])).toList
      case None =>
        LOGGER.info("Unable to acquire Redis lock. getAvailableSchedules completes without real schedules.")
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
        jedis.set(SCHEDULES_KEY_PREFIX + scheduledMatch.matchId, GSON.toJson(scheduledMatch))
      } catch {
        case e: Exception =>
          LOGGER.error("Exception caught while trying to save schedules. ", e)
          retVal = retVal.::(scheduledMatch)
      }
    })

    retVal
  }

  private def retryGetLock(nrOfTimes: Int): Option[Jedis] = {
    var jedisOption: Option[Jedis] = None

    breakable {
      for (i <- 1 to nrOfTimes) {
        jedisOption = getJedisForSchedules
        if (jedisOption.isDefined)
          break
        else
          TimeUnit.MILLISECONDS.sleep(500)
      }
    }
    jedisOption
  }

  private def getJedisForSchedules: Option[Jedis] = {
    val jedis = jedisPool.getResource
    jedis.select(SCHEDULES_DB)

    val lock = redissonClient.getLock(SCHEDULES_LOCK_NAME)
    if (lock.tryLock(LOCK_TIMEOUT, LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
      Some(jedis)
    } else {
      jedis.close()
      None
    }
  }
}

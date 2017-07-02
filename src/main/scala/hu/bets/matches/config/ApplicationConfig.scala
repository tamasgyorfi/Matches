package hu.bets.matches.config

import java.net.URI

import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.dataaccess.{DefaultSchedulesDao, SchedulesDao}
import hu.bets.matches.service.{DefaultMatchesService, MatchesService}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool

class ApplicationConfig {

  private val REDIS_URL = "REDIS_URL"

  private object JedisPoolHolder {
    private val jedisPool: JedisPool = {
      val config = new GenericObjectPoolConfig()
      config.setMaxTotal(124)
      new JedisPool(config, new URI(EnvironmentVarResolver.getEnvVar(REDIS_URL)))
    }

    def getJedisPool: JedisPool = jedisPool
  }

  def getSchedulesDao: SchedulesDao = new DefaultSchedulesDao(JedisPoolHolder.getJedisPool)

  def getMatchService: MatchesService = new DefaultMatchesService(getSchedulesDao)
}

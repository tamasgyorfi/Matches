package hu.bets.matches.config

import java.net.URI

import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.dataaccess.{DefaultSchedulesDao, SchedulesDao}
import hu.bets.matches.service.{DefaultMatchesService, MatchesService}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import redis.clients.jedis.JedisPool

class ApplicationConfig {

  private object JedisConfig {

    private val REDIS_URL = "REDIS_URL"

    private val jedisPool: JedisPool = {
      val config = new GenericObjectPoolConfig()
      config.setMaxTotal(124)
      new JedisPool(config, new URI(EnvironmentVarResolver.getEnvVar(REDIS_URL)))
    }

    private val redisLock: RedissonClient = {
      val config: Config = new Config()
      val updated = config.useClusterServers().addNodeAddress(EnvironmentVarResolver.getEnvVar(REDIS_URL))
      val redisson = Redisson.create(config)

      redisson
    }

    def getJedisPool: JedisPool = jedisPool

    def getRedisClient: RedissonClient = redisLock
  }

  def getSchedulesDao: SchedulesDao = new DefaultSchedulesDao(JedisConfig.getJedisPool, JedisConfig.getRedisClient)

  def getMatchService: MatchesService = new DefaultMatchesService(getSchedulesDao)
}

package hu.bets.matches.config

import java.net.URI

import akka.actor.{ActorRef, Props}
import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.actors.{SceduledMatchesProviderActor, SchedulesActor}
import hu.bets.matches.dataaccess.{DefaultSchedulesDao, SchedulesDao}
import hu.bets.matches.gateway.{ApiKeyReader, KeyReader, MatchInfoGateway}
import hu.bets.matches.service.{DefaultMatchesService, MatchesService}
import hu.bets.matches.{AkkaSingletons, JobScheduler}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.redisson.Redisson
import org.redisson.api.RReadWriteLock
import org.redisson.config.Config
import redis.clients.jedis.JedisPool

trait ApplicationConfig {

  private object JedisConfig {

    private val REDIS_URL = "REDIS_URL"
    private val REDIS_PASS = "REDIS_PASS"
    private val SCHEDULES_LOCK_NAME = "schedules_lock"

    private val jedisPool: JedisPool = {
      val config = new GenericObjectPoolConfig()
      config.setMaxTotal(15)
      new JedisPool(config, new URI(EnvironmentVarResolver.getEnvVar(REDIS_URL)))
    }

    private val redisLock: RReadWriteLock = {
      val config: Config = new Config()
      val updated = config.useReplicatedServers()
        .addNodeAddress(EnvironmentVarResolver.getEnvVar(REDIS_URL))
        .setPassword(EnvironmentVarResolver.getEnvVar(REDIS_PASS))
        .setMasterConnectionPoolSize(5)
        .setMasterConnectionMinimumIdleSize(4)
      val redisson = Redisson.create(config)

      redisson.getReadWriteLock(SCHEDULES_LOCK_NAME)
    }

    def getJedisPool: JedisPool = jedisPool

    def getRedisLock: RReadWriteLock = redisLock
  }

  private def getSchedulesDao: SchedulesDao = new DefaultSchedulesDao(JedisConfig.getJedisPool, JedisConfig.getRedisLock)

  private def getMatchService: MatchesService = new DefaultMatchesService(getSchedulesDao)

  private def getJobScheduler(schedules: ActorRef, results: ActorRef): JobScheduler = new JobScheduler(schedules, results)

  private def getKeyReader = new ApiKeyReader

  private def getMatchInfoGateway(keyReader: KeyReader) = new MatchInfoGateway(keyReader)

  private def getSchedulesActor(matchInfoGateway: MatchInfoGateway, schedulesDao: SchedulesDao) =
    AkkaSingletons.getActorSystem.actorOf(Props(new SchedulesActor(matchInfoGateway, schedulesDao)))

  def getSchedulesProvider: ActorRef =
    AkkaSingletons.getActorSystem.actorOf(Props(new SceduledMatchesProviderActor(getMatchService)))


  def getJobsScheduler: JobScheduler = {
    val matchInfoGateway = getMatchInfoGateway(getKeyReader)
    val schedulesActor = getSchedulesActor(matchInfoGateway, getSchedulesDao)
    getJobScheduler(schedulesActor, null)
  }
}

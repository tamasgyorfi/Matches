package hu.bets.matches.config

import java.net.URI

import akka.actor.{ActorRef, Props}
import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.actors.{ResultsActor, SceduledMatchesProviderActor, SchedulesActor}
import hu.bets.matches.dataaccess.{DefaultSchedulesDao, SchedulesDao}
import hu.bets.matches.gateway.{ApiKeyReader, MatchInfoGateway}
import hu.bets.matches.service.{DefaultMatchesService, MatchesService}
import hu.bets.matches.web.{MatchResultSender, PostRequestSender}
import hu.bets.matches.{AkkaSingletons, JobScheduler}
import hu.bets.servicediscovery.{EurekaFacade, EurekaFacadeImpl}
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

  object BasicAppConfig {
    val getSchedulesDao: SchedulesDao = new DefaultSchedulesDao(JedisConfig.getJedisPool, JedisConfig.getRedisLock)
    val getMatchService: MatchesService = new DefaultMatchesService(getSchedulesDao)
    val getKeyReader = new ApiKeyReader
    val getMatchInfoGateway = new MatchInfoGateway(getKeyReader)
    val getEurekaClient: EurekaFacade = new EurekaFacadeImpl(EnvironmentVarResolver.getEnvVar("EUREKA_URL"))
    val getPostRequestSender: PostRequestSender = new PostRequestSender
    val getResultsSender: MatchResultSender = new MatchResultSender(getEurekaClient, getPostRequestSender)
  }

  private def getJobScheduler(schedules: ActorRef, results: ActorRef): JobScheduler = new JobScheduler(schedules, results)

  private def getSchedulesActor(matchInfoGateway: MatchInfoGateway, schedulesDao: SchedulesDao) =
    AkkaSingletons.getActorSystem.actorOf(Props(new SchedulesActor(matchInfoGateway, schedulesDao)))

  def getSchedulesProvider: ActorRef =
    AkkaSingletons.getActorSystem.actorOf(Props(new SceduledMatchesProviderActor(BasicAppConfig.getMatchService)))

  def getMatchesActor: ActorRef =
    AkkaSingletons.getActorSystem.actorOf(Props(new ResultsActor(BasicAppConfig.getMatchInfoGateway, BasicAppConfig.getResultsSender)))

  def getJobsScheduler: JobScheduler = {
    val matchInfoGateway = BasicAppConfig.getMatchInfoGateway
    val schedulesActor = getSchedulesActor(matchInfoGateway, BasicAppConfig.getSchedulesDao)
    val matchesActor = getMatchesActor
    getJobScheduler(schedulesActor, matchesActor)
  }
}

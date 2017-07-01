package hu.bets.matches.dataaccess

import java.time.LocalDateTime

import com.fiftyonred.mock_jedis.MockJedisPool
import hu.bets.matches.model.{ScheduledMatch, Team}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.junit.Test
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito.when

class DefaultSchedulesDaoTest extends MockitoSugar{

  private val scheduledMatch1 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
  private val scheduledMatch2 = ScheduledMatch("sr:match:11854535", LocalDateTime.parse("2017-06-28T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
  private val scheduledMatch3 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-29T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))


  private val config: GenericObjectPoolConfig = new GenericObjectPoolConfig()
  private val jedisPool = new MockJedisPool(config, "test")
  private val sut: DefaultSchedulesDao = new DefaultSchedulesDao(jedisPool)

  @Test
  def shouldInsertAScheduleOnlyOnce(): Unit = {
    sut.saveSchedules(List(scheduledMatch1, scheduledMatch2, scheduledMatch3))

    assert(2 == jedisPool.getResource.keys("*").size)
  }

  @Test
  def previousEntriesShouldBeDeletedOnCacheUpdate(): Unit = {
    val scheduledMatch4 = ScheduledMatch("sr:match:11854999", LocalDateTime.parse("2017-06-29T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))

    sut.saveSchedules(List(scheduledMatch1, scheduledMatch2, scheduledMatch3))
    sut.saveSchedules(List(scheduledMatch4))

    assert(1 == jedisPool.getResource.keys("*").size)
  }

  @Test
  def shouldReturnFailedToProcessMatchIds(): Unit = {
    val scheduledMatch2 = mock[ScheduledMatch]
    when(scheduledMatch2.matchId).thenThrow(new IllegalArgumentException)

    val retVal = sut.saveSchedules(List(scheduledMatch1, scheduledMatch2, scheduledMatch3))

    assert(1 == jedisPool.getResource.keys("*").size)
    assert(1 == retVal.size)
  }

}

package hu.bets.matches.dataaccess

import java.time.LocalDateTime

import com.fiftyonred.mock_jedis.MockJedisPool
import hu.bets.matches.ScheduledMatches
import hu.bets.matches.model.{ScheduledMatch, Team}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.junit.Test
import org.mockito.Mockito.when
import org.scalatest.junit.JUnitSuite
import org.scalatest.mockito.MockitoSugar

class DefaultSchedulesDaoTest extends JUnitSuite with MockitoSugar {

  private val config: GenericObjectPoolConfig = new GenericObjectPoolConfig()
  private val jedisPool = new MockJedisPool(config, "test")
  private val sut: DefaultSchedulesDao = new DefaultSchedulesDao(jedisPool)

  @Test
  def shouldInsertAScheduleOnlyOnce(): Unit = {
    sut.saveSchedules(List(ScheduledMatches.scheduledMatch1, ScheduledMatches.scheduledMatch2, ScheduledMatches.scheduledMatch3))

    assert(2 == jedisPool.getResource.keys("*").size)
  }

  @Test
  def previousEntriesShouldBeDeletedOnCacheUpdate(): Unit = {
    val scheduledMatch4 = ScheduledMatch("sr:match:11854999", LocalDateTime.parse("2017-06-29T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))

    sut.saveSchedules(List(ScheduledMatches.scheduledMatch1, ScheduledMatches.scheduledMatch2, ScheduledMatches.scheduledMatch3))
    sut.saveSchedules(List(scheduledMatch4))

    assert(1 == jedisPool.getResource.keys("*").size)
  }

  @Test
  def shouldReturnFailedToProcessMatchIds(): Unit = {
    val scheduledMatch2 = mock[ScheduledMatch]
    when(scheduledMatch2.matchId).thenThrow(new IllegalArgumentException)

    val retVal = sut.saveSchedules(List(ScheduledMatches.scheduledMatch1, scheduledMatch2, ScheduledMatches.scheduledMatch3))

    assert(1 == jedisPool.getResource.keys("*").size)
    assert(1 == retVal.size)
  }

  @Test
  def shouldReturnAllSchedules(): Unit = {
    val entries = List(ScheduledMatches.scheduledMatch1, ScheduledMatches.scheduledMatch2)
    sut.saveSchedules(entries)

    assert(entries === sut.getAvailableSchedules)
  }
}

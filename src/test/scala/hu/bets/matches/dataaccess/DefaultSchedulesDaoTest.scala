package hu.bets.matches.dataaccess

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

import com.fiftyonred.mock_jedis.MockJedisPool
import hu.bets.matches.ScheduledMatches
import hu.bets.matches.model.{ScheduledMatch, Team}
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.junit.{Before, Test}
import org.mockito.Mockito.when
import org.redisson.api.{RLock, RReadWriteLock}
import org.scalatest.junit.JUnitSuite
import org.scalatest.mockito.MockitoSugar

class DefaultSchedulesDaoTest extends JUnitSuite with MockitoSugar {

  private val config: GenericObjectPoolConfig = new GenericObjectPoolConfig()
  private val jedisPool = new MockJedisPool(config, "test")

  private val readWriteLock: RReadWriteLock = mock[RReadWriteLock]
  private val readLock: RLock = mock[RLock]
  private val writeLock: RLock = mock[RLock]

  private var sut: DefaultSchedulesDao = _

  @Before
  def before(): Unit = {
    when(readWriteLock.readLock()).thenReturn(readLock)
    when(readWriteLock.writeLock()).thenReturn(writeLock)
    when(readLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS)).thenReturn(true)
    when(writeLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS)).thenReturn(true)

    sut = new DefaultSchedulesDao(jedisPool, readWriteLock)
  }

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
    when(readLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS)).thenReturn(true)

    val entries = List(ScheduledMatches.scheduledMatch1, ScheduledMatches.scheduledMatch2)
    sut.saveSchedules(entries)

    assert(ScheduledMatches.result === sut.getAvailableSchedules)
  }

  @Test
  def availableSchedulesShouldBeEmptyWhenLockCannotBeAcquired(): Unit = {
    when(readLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS)).thenReturn(false)
    val retVal = sut.saveSchedules(List(ScheduledMatches.scheduledMatch1, ScheduledMatches.scheduledMatch2, ScheduledMatches.scheduledMatch3))

    assert(List() === sut.getAvailableSchedules)
  }

  @Test
  def availableSchedulesShouldNotBeEmptyWhenLockCanBeAcquiredEventually(): Unit = {
    when(readLock.tryLock(1000, 1000, TimeUnit.MILLISECONDS)).thenReturn(false).thenReturn(false).thenReturn(true)
    val retVal = sut.saveSchedules(List(ScheduledMatches.scheduledMatch1, ScheduledMatches.scheduledMatch2))

    assert(ScheduledMatches.result === sut.getAvailableSchedules)
  }

}

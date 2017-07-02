package hu.bets.matches.service

import hu.bets.matches.dataaccess.SchedulesDao
import org.junit.{Before, Test}
import org.mockito.Mockito.verify
import org.scalatest.mockito.MockitoSugar

class DefaultMatchServiceTest extends MockitoSugar {

  var sut: DefaultMatchesService = _
  val dao: SchedulesDao = mock[SchedulesDao]

  @Before
  def before(): Unit = {
    sut = new DefaultMatchesService(dao)
  }

  @Test
  def shouldCallDao(): Unit = {
    sut.getSchedules()

    verify(dao).getAvailableSchedules
  }
}

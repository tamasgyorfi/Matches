package hu.bets.matches.gateway

import java.time.LocalDate

import org.junit.{Before, Test}
import org.mockito.Mockito._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mockito.MockitoSugar

class MatchResultGatewayTest extends JUnitSuite with MockitoSugar {

  val keyReader: KeyReader = mock[KeyReader]
  var sut: MatchResultGateway = _

  trait FakeDateProvider {
    def getCurrentDate: LocalDate = {
      LocalDate.parse("2017-03-29")
    }
  }

  @Before
  def setup(): Unit = {
    sut = new MatchResultGateway(keyReader) with FakeDateProvider {
      override def getCurrentDate: LocalDate = super[FakeDateProvider].getCurrentDate
    }
  }

  @Test
  def shouldReturnCorrectSchedulesEndpoint(): Unit = {
    when(keyReader.getApiKey()).thenReturn("API_KEY")
    assert("https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-06-03/schedule.json?api_key=API_KEY" === sut.getScheduleEndpoint("2017-06-03"))
  }

  @Test
  def shouldReturnCorrectResultsEndpoint(): Unit = {
    when(keyReader.getApiKey()).thenReturn("API_KEY")
    assert("https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-06-03/results.json?api_key=API_KEY" === sut.getResultEdpoint("2017-06-03"))
  }

  @Test
  def shouldReturnScheduleDaysForSevenDays(): Unit = {
    assert(List("2017-03-29", "2017-03-30", "2017-03-31", "2017-04-01", "2017-04-02", "2017-04-03", "2017-04-04") === sut.getDatesToQuery(7))
  }
}

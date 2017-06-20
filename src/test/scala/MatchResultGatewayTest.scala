package hu.bets.matches.gateway

import org.junit.{Before, Test}
import org.mockito.Mockito._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar

class MatchResultGatewayTest extends JUnitSuite with MockitoSugar {

  val keyReader = mock[KeyReader]
  var sut: MatchResultGateway = _

  @Before
  def setup(): Unit = {
    sut = new MatchResultGateway(keyReader)
  }

  @Test
  def shouldReturnCorrectSchedulesEndpoint(): Unit = {
    when(keyReader.getApiKey()).thenReturn("API_KEY")
    assert("https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-06-03/results.json?api_key=API_KEY" === sut.getScheduleEndpoint("2017-06-03"))
  }

  @Test
  def shouldReturnCorrectResultsEndpoint(): Unit = {
    when(keyReader.getApiKey()).thenReturn("API_KEY")
    assert("https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-06-03/results.json?api_key=API_KEY" === sut.getScheduleEndpoint("2017-06-03"))
  }

}

package hu.bets.matches.gateway

import java.time.{LocalDate, LocalDateTime}

import hu.bets.matches.model.{ScheduledMatch, Team}
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

  @Test
  def shouldParseSchedules(): Unit = {
    val json = "{\"generated_at\":\"2017-06-23T11:19:29+00:00\",\"schema\":\"http:\\/\\/schemas.sportradar.com\\/bsa\\/json\\/v1\\/endpoints\\/soccer\\/schedule.json\",\"sport_events\":[{\"hello\":\"world\"},{\"id\":\"sr:match:11854534\",\"scheduled\":\"2017-06-27T16:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854556\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:67966\",\"name\":\"Alashkert\",\"country\":\"Armenia\",\"country_code\":\"ARM\",\"abbreviation\":\"ALA\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:7711\",\"name\":\"FC Santa Coloma\",\"country\":\"Andorra\",\"country_code\":\"AND\",\"abbreviation\":\"FCC\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854528\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854540\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:5193\",\"name\":\"Vikingur Gota\",\"country\":\"Faroe Islands\",\"country_code\":\"FRO\",\"abbreviation\":\"GOT\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:277849\",\"name\":\"Trepca 89\",\"country\":\"Kosovo\",\"country_code\":\"KOS\",\"abbreviation\":\"TRE\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854532\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854554\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:5331\",\"name\":\"Hibernians FC\",\"country\":\"Malta\",\"country_code\":\"MLT\",\"abbreviation\":\"HIB\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:37977\",\"name\":\"FCI Tallinn\",\"country\":\"Estonia\",\"country_code\":\"EST\",\"abbreviation\":\"FCI\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854536\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854558\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:4921\",\"name\":\"The New Saints FC\",\"country\":\"Wales\",\"country_code\":\"WAL\",\"abbreviation\":\"TNS\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:126298\",\"name\":\"College Europa FC\",\"country\":\"Gibraltar\",\"country_code\":\"GIB\",\"abbreviation\":\"COL\",\"qualifier\":\"away\"}]}]}, "

    assert(
      List(
        Some(ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team ("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))),
        Some(ScheduledMatch("sr:match:11854528", LocalDateTime.parse("2017-06-27T18:00"), "UEFA Champions League 17/18", Team("Vikingur Gota", "GOT", "Faroe Islands"), Team("Trepca 89","TRE", "Kosovo"))),
        Some(ScheduledMatch("sr:match:11854532", LocalDateTime.parse("2017-06-27T18:00"), "UEFA Champions League 17/18", Team("Hibernians FC", "HIB", "Malta"), Team("FCI Tallinn", "FCI", "Estonia"))))

        ===

        sut.parse(json))
  }
}

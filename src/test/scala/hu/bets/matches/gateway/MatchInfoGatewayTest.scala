package hu.bets.matches.gateway

import java.time.{LocalDate, LocalDateTime}

import hu.bets.matches.model.{MatchResult, Result, ScheduledMatch, Team}
import org.junit.{Before, Test}
import org.mockito.Mockito._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mockito.MockitoSugar

class MatchInfoGatewayTest extends JUnitSuite with MockitoSugar {

  private val MATCHES_JSON = """{ "generated_at":"2017-07-22T12:59:50+00:00", "schema":"http:\/\/schemas.sportradar.com\/bsa\/json\/v1\/endpoints\/soccer\/results.json", "results":[ { "sport_event":{ "id":"sr:match:9792041", "scheduled":"2017-06-03T11:00:00+00:00", "start_time_tbd":false, "tournament_round":{ "type":"group", "number":34 }, "season":{ "id":"sr:season:33675", "name":"Super Lig 16\/17", "start_date":"2016-08-17", "end_date":"2017-06-04", "year":"16\/17", "tournament_id":"sr:tournament:52" }, "tournament":{ "id":"sr:tournament:52", "name":"Super Lig", "sport":{ "id":"sr:sport:1", "name":"Soccer" }, "category":{ "id":"sr:category:46", "name":"Turkey", "country_code":"TUR" } }, "competitors":[ { "id":"sr:competitor:3072", "name":"Kayserispor", "country":"Turkey", "country_code":"TUR", "abbreviation":"KAY", "qualifier":"home" }, { "id":"sr:competitor:3086", "name":"Basaksehir FK", "country":"Turkey", "country_code":"TUR", "abbreviation":"BAS", "qualifier":"away" } ] }, "sport_event_status":{ "status":"closed", "match_status":"ended", "home_score":0, "away_score":1, "winner_id":"sr:competitor:3086", "period_scores":[ { "home_score":0, "away_score":0, "type":"regular_period", "number":1 }, { "home_score":0, "away_score":1, "type":"regular_period", "number":2 } ] } } ] }""";

  val keyReader: KeyReader = mock [KeyReader]
  var sut: MatchInfoGateway = _

  trait FakeDateProvider {
    def getCurrentDate: LocalDate = {
      LocalDate.parse ( "2017-03-29" )
    }
  }

  @Before
  def setup(): Unit = {
    sut = new MatchInfoGateway ( keyReader ) with FakeDateProvider {
      override def getCurrentDate: LocalDate = super[FakeDateProvider].getCurrentDate

      override def getResults(date: String) = MATCHES_JSON
    }
  }

  @Test
  def shouldReturnCorrectSchedulesEndpoint(): Unit = {
    when ( keyReader.getApiKey () ).thenReturn ( "API_KEY" )
    assert ( "https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-06-03/schedule.json?api_key=API_KEY" === sut.getScheduleEndpoint ( "2017-06-03" ) )
  }

  @Test
  def shouldReturnCorrectResultsEndpoint(): Unit = {
    when ( keyReader.getApiKey () ).thenReturn ( "API_KEY" )
    assert ( "https://api.sportradar.us/soccer-t3/eu/en/schedules/2017-06-03/results.json?api_key=API_KEY" === sut.getResultEdpoint ( "2017-06-03" ) )
  }

  @Test
  def shouldReturnScheduleDaysForSevenDays(): Unit = {
    assert ( List ( "2017-03-29", "2017-03-30", "2017-03-31", "2017-04-01", "2017-04-02", "2017-04-03", "2017-04-04" ) === sut.getDatesToQuery ( 7 ) )
  }

  @Test
  def shouldParseSchedules(): Unit = {
    val json = "{\"generated_at\":\"2017-06-23T11:19:29+00:00\",\"schema\":\"http:\\/\\/schemas.sportradar.com\\/bsa\\/json\\/v1\\/endpoints\\/soccer\\/schedule.json\",\"sport_events\":[{\"hello\":\"world\"},{\"id\":\"sr:match:11854534\",\"scheduled\":\"2017-06-27T16:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854556\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:67966\",\"name\":\"Alashkert\",\"country\":\"Armenia\",\"country_code\":\"ARM\",\"abbreviation\":\"ALA\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:7711\",\"name\":\"FC Santa Coloma\",\"country\":\"Andorra\",\"country_code\":\"AND\",\"abbreviation\":\"FCC\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854528\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854540\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:5193\",\"name\":\"Vikingur Gota\",\"country\":\"Faroe Islands\",\"country_code\":\"FRO\",\"abbreviation\":\"GOT\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:277849\",\"name\":\"Trepca 89\",\"country\":\"Kosovo\",\"country_code\":\"KOS\",\"abbreviation\":\"TRE\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854532\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854554\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:5331\",\"name\":\"Hibernians FC\",\"country\":\"Malta\",\"country_code\":\"MLT\",\"abbreviation\":\"HIB\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:37977\",\"name\":\"FCI Tallinn\",\"country\":\"Estonia\",\"country_code\":\"EST\",\"abbreviation\":\"FCI\",\"qualifier\":\"away\"}]}]}"

    assert (
      List (
        ScheduledMatch ( "sr:match:11854534", LocalDateTime.parse ( "2017-06-27T16:00" ), "UEFA Champions League 17/18", Team ( "Alashkert", "ALA", "Armenia" ), Team ( "FC Santa Coloma", "FCC", "Andorra" ) ),
        ScheduledMatch ( "sr:match:11854528", LocalDateTime.parse ( "2017-06-27T18:00" ), "UEFA Champions League 17/18", Team ( "Vikingur Gota", "GOT", "Faroe Islands" ), Team ( "Trepca 89", "TRE", "Kosovo" ) ),
        ScheduledMatch ( "sr:match:11854532", LocalDateTime.parse ( "2017-06-27T18:00" ), "UEFA Champions League 17/18", Team ( "Hibernians FC", "HIB", "Malta" ), Team ( "FCI Tallinn", "FCI", "Estonia" ) ) )

        ===

        sut.parseSchedules ( json ) )
  }

  @Test
  def shouldParseResults(): Unit = {
    sut.parseMatchResults ( MATCHES_JSON ) ===
      List ( MatchResult (Result( "Kayserispor", "Basaksehir FK", "sr:match:9792041", "Super Lig", 0, 1 ) ))
  }

  @Test
  def shouldReturnOneDaysWorthOfResults(): Unit = {
    sut.getMatchResults === List ( MatchResult ( Result("Kayserispor", "Basaksehir FK", "sr:match:9792041", "Super Lig", 0, 1 ) ))
  }
}

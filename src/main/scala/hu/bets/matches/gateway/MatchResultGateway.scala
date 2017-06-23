package hu.bets.matches.gateway

import java.net.URI
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import hu.bets.matches.model.ScheduledMatches
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger

trait DateProvider {
  def getCurrentDate: LocalDate = {
    LocalDate.now()
  }
}

class MatchResultGateway(keyReader: KeyReader) extends DateProvider {

  private val LOGGER: Logger = Logger.getLogger(classOf[MatchResultGateway])

  private val BASE_API = "%s://api.sportradar.us/soccer-%s%d/eu/en/schedules/%s/%s.%s?api_key=%s"
  private val PROTOCOL = "https"
  private val STATUS = "t"
  private val VERSION = 3
  private val SCHEDULE_ENDPOINT = "schedule"
  private val RESULT_ENDPOINT = "results"
  private val PAYLOAD_FORMAT = "json"

  val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  private[gateway] def getScheduleEndpoint(date: String): String = {
    BASE_API.format(PROTOCOL, STATUS, VERSION, date, SCHEDULE_ENDPOINT, PAYLOAD_FORMAT, keyReader.getApiKey())
  }

  private[gateway] def getResultEdpoint(date: String): String = {
    BASE_API.format(PROTOCOL, STATUS, VERSION, date, RESULT_ENDPOINT, PAYLOAD_FORMAT, keyReader.getApiKey())
  }

  private[gateway] def getDatesToQuery(nrOfDays: Int): List[String] = {
    val currentDate = getCurrentDate
    Range(0, nrOfDays).toList.map(index => currentDate.plusDays(index)).map(day => FORMATTER.format(day))
  }

  def getSchedules(nrOfDays: Int): List[String] = {

    val httpClient = HttpClientBuilder.create.build
    val get = new HttpGet()

    getDatesToQuery(nrOfDays).map(dateString => {
      val endpoint = getScheduleEndpoint(dateString)
      get.setURI(new URI(endpoint))
      val response = EntityUtils.toString(httpClient.execute(get).getEntity)
      TimeUnit.SECONDS.sleep(1)

      response
    })
  }

  def parse(): Unit = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.registerModule(new JSR310Module)
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SS:SS"))

    val y = "{\"generated_at\":\"2017-06-23T11:19:29+00:00\",\"schema\":\"http:\\/\\/schemas.sportradar.com\\/bsa\\/json\\/v1\\/endpoints\\/soccer\\/schedule.json\",\"sport_events\":[{\"id\":\"sr:match:11854534\",\"scheduled\":\"2017-06-27T16:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854556\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:67966\",\"name\":\"Alashkert\",\"country\":\"Armenia\",\"country_code\":\"ARM\",\"abbreviation\":\"ALA\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:7711\",\"name\":\"FC Santa Coloma\",\"country\":\"Andorra\",\"country_code\":\"AND\",\"abbreviation\":\"FCC\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854528\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854540\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:5193\",\"name\":\"Vikingur Gota\",\"country\":\"Faroe Islands\",\"country_code\":\"FRO\",\"abbreviation\":\"GOT\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:277849\",\"name\":\"Trepca 89\",\"country\":\"Kosovo\",\"country_code\":\"KOS\",\"abbreviation\":\"TRE\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854532\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854554\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:5331\",\"name\":\"Hibernians FC\",\"country\":\"Malta\",\"country_code\":\"MLT\",\"abbreviation\":\"HIB\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:37977\",\"name\":\"FCI Tallinn\",\"country\":\"Estonia\",\"country_code\":\"EST\",\"abbreviation\":\"FCI\",\"qualifier\":\"away\"}]},{\"id\":\"sr:match:11854536\",\"scheduled\":\"2017-06-27T18:00:00+00:00\",\"start_time_tbd\":false,\"status\":\"not_started\",\"tournament_round\":{\"type\":\"qualification\",\"name\":\"cupqround\",\"other_match_id\":\"sr:match:11854558\"},\"season\":{\"id\":\"sr:season:41198\",\"name\":\"UEFA Champions League 17\\/18\",\"start_date\":\"2017-06-26\",\"end_date\":\"2018-05-27\",\"year\":\"17\\/18\",\"tournament_id\":\"sr:tournament:7\"},\"tournament\":{\"id\":\"sr:tournament:7\",\"name\":\"UEFA Champions League\",\"sport\":{\"id\":\"sr:sport:1\",\"name\":\"Soccer\"},\"category\":{\"id\":\"sr:category:393\",\"name\":\"International Clubs\"}},\"competitors\":[{\"id\":\"sr:competitor:4921\",\"name\":\"The New Saints FC\",\"country\":\"Wales\",\"country_code\":\"WAL\",\"abbreviation\":\"TNS\",\"qualifier\":\"home\"},{\"id\":\"sr:competitor:126298\",\"name\":\"College Europa FC\",\"country\":\"Gibraltar\",\"country_code\":\"GIB\",\"abbreviation\":\"COL\",\"qualifier\":\"away\"}]}]}, "
    val x = mapper.readValue(y, classOf[ScheduledMatches])

    println(x.toString)
  }


}

object runner extends App {
  override def main(args: Array[String]): Unit = {

    new MatchResultGateway(new ApiKeyReader()).parse()
  }
}

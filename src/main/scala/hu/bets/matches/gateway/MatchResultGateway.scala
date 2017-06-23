package hu.bets.matches.gateway

import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

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

  val BASE_API = "%s://api.sportradar.us/soccer-%s%d/eu/en/schedules/%s/%s.%s?api_key=%s"
  val PROTOCOL = "https"
  val STATUS = "t"
  val VERSION = 3
  val SCHEDULE_ENDPOINT = "schedule"
  val RESULT_ENDPOINT = "results"
  val PAYLOAD_FORMAT = "json"

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
}

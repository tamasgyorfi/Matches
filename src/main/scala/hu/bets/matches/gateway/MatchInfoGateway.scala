package hu.bets.matches.gateway

import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import hu.bets.matches.conversions.StringToMatch.stringToMatches
import hu.bets.matches.model.ScheduledMatch
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.slf4j.{Logger, LoggerFactory}


trait DateProvider {
  def getCurrentDate: LocalDate = {
    LocalDate.now()
  }
}

class MatchInfoGateway(keyReader: KeyReader) extends DateProvider {

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[MatchInfoGateway])

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

  private[gateway] def getSchedules(nrOfDays: Int): List[String] = {

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

  private[gateway] def parse(json: String): List[ScheduledMatch] = {
    val matches: List[Option[ScheduledMatch]] = json
    matches.filter(optional => optional.isDefined).map(optional => optional.get)
  }

  def getScheduledMatches(nrOfDays: Int): List[ScheduledMatch] = {
    try {
      val json = getSchedules(nrOfDays)
      json.flatMap(oneDayJson => parse(oneDayJson))
    } catch {
      case e: Exception => throw new ScheduleRetrievalException(e)
    }
  }
}
package hu.bets.matches.gateway

import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import hu.bets.common.util.EnvironmentVarResolver
import hu.bets.matches.conversions.StringToMatchResult.stringToMatchResults
import hu.bets.matches.conversions.StringToScheduledMatch.stringToMatches
import hu.bets.matches.model.{MatchResult, ScheduledMatch}
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

  private[gateway] def getDatesToQuery(nrOfDays: Int): List[LocalDate] = {
    val currentDate = getCurrentDate
    LOGGER info("Current date is: {}", currentDate)
    Range(0, nrOfDays).toList.map(index => currentDate.plusDays(index))
  }

  private[gateway] def getSchedules(nrOfDays: Int): List[String] = {

    val httpClient = HttpClientBuilder.create.build
    val get = new HttpGet()

    getDatesToQuery(nrOfDays).map(date => {
      val endpoint = getScheduleEndpoint(FORMATTER.format(date))
      get.setURI(new URI(endpoint))
      LOGGER info ("Endpoint is: {}", endpoint.substring(0, endpoint.indexOf("=")))
      val response = EntityUtils.toString(httpClient.execute(get).getEntity)
      LOGGER info ("Raw response from the third party service was: {}", response)
      TimeUnit.SECONDS.sleep(1)

      response
    })
  }

  private[gateway] def parseSchedules(json: String): List[ScheduledMatch] = {
    val matches: List[Option[ScheduledMatch]] = json
    matches.filter(optional => optional.isDefined).map(optional => optional.get)
  }

  def getScheduledMatches(nrOfDays: Int): List[ScheduledMatch] = {
    try {
      val json =
        EnvironmentVarResolver.getEnvVar("mode") match {
          case "debug" => List(MockResponse.getResponse)
          case _ => getSchedules(nrOfDays)
        }


      json.flatMap(oneDayJson => parseSchedules(oneDayJson)
      )
    } catch {
      case e: Exception =>
        throw new DataRetrievalException(e)
    }
  }

  def getMatchResults: List[MatchResult] = {
    LOGGER info "Trying to read match results."
    try {
      val date = getDatesToQuery(1).head.minusDays(1)
      LOGGER info("Will be querying external service for the date: {}", date)
      val results = getResults(FORMATTER.format(date))
      val parsedResults = parseMatchResults(results)
      LOGGER.info("Read the following results: {}", parsedResults)
      parsedResults
    } catch {
      case e: Exception => throw new DataRetrievalException(e)
    }
  }

  private[gateway] def getResults(date: String): String = {
    val httpClient = HttpClientBuilder.create.build
    val get = new HttpGet()
    val endpoint = getResultEdpoint(date)
    get.setURI(new URI(endpoint))
    val response = EntityUtils.toString(httpClient.execute(get).getEntity)
    TimeUnit.SECONDS.sleep(1)
    LOGGER info("Result from the results service: {}", response)
    response
  }

  private[gateway] def parseMatchResults(json: String): List[MatchResult] = {
    val matches: List[Option[MatchResult]] = json
    matches.filter(optional => optional.isDefined).map(optional => optional.get)
  }
}
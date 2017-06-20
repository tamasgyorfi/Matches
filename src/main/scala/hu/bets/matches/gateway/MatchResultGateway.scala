package hu.bets.matches.gateway

class MatchResultGateway(keyReader: KeyReader) {

  val BASE_API = "%s://api.sportradar.us/soccer-%s%d/eu/en/%s/%s/results.%s?api_key=%s"
  val PROTOCOL = "https"
  val STATUS = "t"
  val VERSION = 3
  val SCHEDULE_ENDPOINT = "schedules"
  val RESULT_ENDPOINT = "results"
  val PAYLOAD_FORMAT = "json"

  private[gateway] def getScheduleEndpoint(date: String): String = {
    BASE_API.format(PROTOCOL, STATUS, VERSION, SCHEDULE_ENDPOINT, date, PAYLOAD_FORMAT, keyReader.getApiKey())
  }

  private[gateway] def getResultEdpoint(date: String): String = {
    BASE_API.format(PROTOCOL, STATUS, VERSION, RESULT_ENDPOINT, date, PAYLOAD_FORMAT, keyReader.getApiKey())
  }
}

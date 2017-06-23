package hu.bets.matches.gateway

import org.apache.log4j.Logger

import scala.io.Source

trait KeyReader {
  def getApiKey(): String
}

class ApiKeyReader extends KeyReader {

  object ApiKeyReaderObject {

    val logger: Logger = Logger.getLogger(classOf[ApiKeyReader])

    private def readApiKey(): String = {

      logger info "Reading apikey from external file."
      try {
        val apiKey = Source.fromResource("apikey.txt").getLines().take(1).next()
        logger info "ApiKey found and read correctly."
        apiKey
      } catch {
        case e: Exception => {
          throw new MatchApiUnreachableException(e)
        }
      }
    }

    val apiKey = readApiKey()
  }

  override def getApiKey() = ApiKeyReaderObject.apiKey
}
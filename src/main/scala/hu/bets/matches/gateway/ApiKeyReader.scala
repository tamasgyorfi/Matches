package hu.bets.matches.gateway

import hu.bets.common.util.EnvironmentVarResolver
import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source

trait KeyReader {
  def getApiKey(): String
}

class ApiKeyReader extends KeyReader {

  object ApiKeyReaderObject {

    private val MATCHES_API_KEY: String = "MATCHES_API_KEY"
    private val LOGGER: Logger = LoggerFactory.getLogger(classOf[ApiKeyReader])

    private def readApiKey(): String = {

      LOGGER info "Reading apikey from external file."
      try {
        val apiKey = Source.fromResource("apikey.txt").getLines().take(1).next()
        LOGGER info "ApiKey found and read correctly."
        apiKey
      } catch {
        case e: Exception => {
          throw new MatchApiUnreachableException(e)
        }
      }
    }

    val apiKey = {
      EnvironmentVarResolver.getEnvVar(MATCHES_API_KEY) match {
        case "" => readApiKey()
        case value => value
      }

    }
  }

  override def getApiKey() = ApiKeyReaderObject.apiKey
}
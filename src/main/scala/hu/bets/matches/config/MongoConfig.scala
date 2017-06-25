package hu.bets.matches.config

import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.mongodb.{MongoClient, MongoClientURI}
import hu.bets.common.util.EnvironmentVarResolver
import org.bson.Document

private class MongoConfig {

  private val DB_URI_KEY = "MONGODB_URI"
  private val COLLECTION_NAME = "Schedules"
  private val DB_NAME = ""

  def getMongoClient(): MongoDatabase = {
    val dbUri = EnvironmentVarResolver.getEnvVar(DB_URI_KEY)
    val clientURI = new MongoClientURI(dbUri)
    val client = new MongoClient(clientURI)
    client.getDatabase(DB_NAME)
  }

  def getScheduleCollection(mongoDatabase: MongoDatabase): MongoCollection[Document] = mongoDatabase.getCollection(COLLECTION_NAME)
}

object MongoConfig {

  private val config: MongoConfig = new MongoConfig
  private val database: MongoDatabase = config.getMongoClient
  private val client: MongoCollection[Document] = config.getScheduleCollection(database)

  def getClient: MongoCollection[Document] = client

}

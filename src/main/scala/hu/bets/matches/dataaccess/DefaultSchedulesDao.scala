package hu.bets.matches.dataaccess

import com.google.gson.Gson
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import hu.bets.matches.model.ScheduledMatch
import org.bson.Document


class DefaultSchedulesDao(mongoCollection: MongoCollection[Document]) extends SchedulesDao {


  /**
    * Saves a number of scheduled matches into the database.
    *
    * @param schedules the scheduled matches
    * @return the scheduled matches which could not be saved.
    */
  override def saveSchedules(schedules: List[ScheduledMatch]): List[ScheduledMatch] = {
    val retVal: List[ScheduledMatch] = List()

    schedules.foreach(scheduledMatch => {
      try {
        insertIfAbsent(scheduledMatch)
      } catch {
        case _: Exception => scheduledMatch :: retVal
      }
    })

    retVal
  }

  private def insertIfAbsent(scheduledMatch: ScheduledMatch): Unit = {
    val query = Filters.eq("matchId", scheduledMatch.matchId)

    mongoCollection.find(query).first match {
      case null => mongoCollection.insertOne(Document.parse(new Gson().toJson(scheduledMatch)))
      case _ =>
    }
  }
}

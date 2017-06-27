package hu.bets.matches.dataaccess

import java.time.LocalDateTime

import com.github.fakemongo.Fongo
import com.mongodb.client.MongoCollection
import hu.bets.matches.model.{ScheduledMatch, Team}
import org.bson.Document
import org.junit.Test

class DefaultSchedulesDaoTest {

  private val collection: MongoCollection[Document] = new Fongo("test").getMongo.getDatabase("testDatabase").getCollection("testCollection")
  private val sut: DefaultSchedulesDao = new DefaultSchedulesDao(collection)

  @Test
  def shouldInsertAScheduleOnlyOnce(): Unit = {
    val scheduledMatch1 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
    val scheduledMatch2 = ScheduledMatch("sr:match:11854535", LocalDateTime.parse("2017-06-28T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))
    val scheduledMatch3 = ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-29T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra"))

    sut.saveSchedules(List(scheduledMatch1, scheduledMatch2, scheduledMatch3))

    assert(2 == collection.count())
  }
}

package hu.bets.matches.actors

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import hu.bets.matches.dataaccess.SchedulesDao
import hu.bets.matches.gateway.MatchInfoGateway
import hu.bets.matches.model.{ScheduledMatch, Team}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class SchedulesActorTest extends TestKit(ActorSystem("Test")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  private val matchInfoGateway = mock[MatchInfoGateway]
  private val scheduleDao = mock[SchedulesDao]

  private val sut = system.actorOf(Props(new SchedulesActor(matchInfoGateway, scheduleDao)))

  "Schedules actor" must {
    "do nothing when unknown message is received" in {
      sut ! "Unknown Message"

      verifyZeroInteractions(matchInfoGateway)
      verifyZeroInteractions(scheduleDao)
    }

    "retrieve and save schedules" in {
      val matches = List(ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra")))

      when(matchInfoGateway.getScheduledMatches(7)).thenReturn(matches)

      sut ! ScheduleRequest

      verify(scheduleDao).saveSchedules(matches)
    }
  }
}

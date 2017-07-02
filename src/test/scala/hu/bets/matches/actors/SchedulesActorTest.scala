package hu.bets.matches.actors

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import hu.bets.matches.dataaccess.SchedulesDao
import hu.bets.matches.gateway.MatchInfoGateway
import hu.bets.matches.model.{ScheduledMatch, Team}
import org.junit.Test
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class SchedulesActorTest extends TestKit(ActorSystem("Test")) with ImplicitSender with MockitoSugar {

  private val matchInfoGateway = mock[MatchInfoGateway]
  private val scheduleDao = mock[SchedulesDao]
  private val sut = system.actorOf(Props(new SchedulesActor(matchInfoGateway, scheduleDao)))

  @Test
  def shouldDoNothingWhenUnknownMessageIsReceive(): Unit = {
    sut ! "Unknown Message"

    verifyZeroInteractions(matchInfoGateway)
    verifyZeroInteractions(scheduleDao)
  }

  @Test
  def shouldHandleScheduleRequestMessage(): Unit = {

    val matches = List(ScheduledMatch("sr:match:11854534", LocalDateTime.parse("2017-06-27T16:00"), "UEFA Champions League 17/18", Team("Alashkert", "ALA", "Armenia"), Team("FC Santa Coloma", "FCC", "Andorra")))
    when(matchInfoGateway.getScheduledMatches(7)).thenReturn(matches)
    when(scheduleDao.saveSchedules(matches)).thenReturn(List())

    sut ! ScheduleRequest

    TimeUnit.SECONDS.sleep(1)
    verify(scheduleDao).saveSchedules(matches)
  }
}

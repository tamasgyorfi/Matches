package hu.bets.matches.gateway

case class MatchApiUnreachableException(e: Exception) extends RuntimeException {}

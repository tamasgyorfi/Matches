package hu.bets.matches.model

case class Team(name: String, nationality: String) {
  override def toString: String = "name: " + name + " nationality: " + nationality
}

package hu.bets.matches.model

case class Team(name: String, abbreviation: String, nationality: String) {
  override def toString: String = "[name: " + name + " abbreviation: " + abbreviation + " nationality: " + nationality + "]"
}

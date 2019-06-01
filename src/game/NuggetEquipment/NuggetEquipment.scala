package game.NuggetEquipment

abstract class NuggetEquipment {

  var numOwned: Int = 0
  var name: String = ""

  def nugsPerSecond(): Double
  def nugsPerPickup(): Double
  def costOfNextPurchase(): Double

  def buy(): Unit = {
    numOwned += 1
  }

}

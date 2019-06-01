package game.NuggetEquipment

class NuggetGenerator extends NuggetEquipment {

  this.name = "Generator"

  override def nugsPerPickup(): Double = {
    this.numOwned * 25
  }

  override def costOfNextPurchase(): Double = {
    1000 * Math.pow(1.05, this.numOwned)
  }

  override def nugsPerSecond(): Double = {
    this.numOwned * 100
  }
}

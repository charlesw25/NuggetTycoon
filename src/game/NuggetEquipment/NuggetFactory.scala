package game.NuggetEquipment

class NuggetFactory extends NuggetEquipment {

  this.name = "Factory"

  override def nugsPerPickup(): Double = {
    0.0
  }

  override def costOfNextPurchase(): Double = {
    150 * Math.pow(1.05, this.numOwned)
  }

  override def nugsPerSecond(): Double = {
    this.numOwned * 10
  }
}

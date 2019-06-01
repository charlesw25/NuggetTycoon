package game.NuggetEquipment

class NuggetFarm extends NuggetEquipment {

  this.name = "Farm"

  override def nugsPerPickup(): Double = {
    this.numOwned * 5 //2 nugs per pickup
  }

  override def costOfNextPurchase(): Double = {
    10 * Math.pow(1.05, this.numOwned) //10 + 10 percent each time
  }

  override def nugsPerSecond(): Double = {
    0.0
  }

}

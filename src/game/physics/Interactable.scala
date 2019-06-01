package game.physics

object Interactable {
  var next_ID: Int = 0
}

class Interactable(loc: PhysicsVector) {

  val location: PhysicsVector = this.loc

  val interactableID: Int = Interactable.next_ID
  Interactable.next_ID += 1

  override def toString: String = {
    "Id: " + this.interactableID
  }
}

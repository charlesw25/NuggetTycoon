package game.physics

import scala.collection.mutable.ListBuffer

class NPC(location: PhysicsVector) {
  val npcLocation: PhysicsVector = location

  val topLeft: PhysicsVector = new PhysicsVector(this.location.x - 5,this.location.y - 5)
  val topRight: PhysicsVector = new PhysicsVector(this.location.x + 5,this.location.y - 5)
  val bottomLeft: PhysicsVector = new PhysicsVector(this.location.x - 5, this.location.y + 5)
  val bottomRight: PhysicsVector = new PhysicsVector(this.location.x + 5, this.location.y + 5)


  val topBorder: Boundary = new Boundary(topLeft,topRight)
  val leftBorder: Boundary = new Boundary(topLeft,bottomLeft)
  val rightBorder: Boundary = new Boundary(topRight,bottomRight)
  val bottomBorder: Boundary = new Boundary(bottomLeft,bottomRight)

  val returnBorders: Unit => ListBuffer[Boundary] = (u: Unit) => {ListBuffer(topBorder, leftBorder, rightBorder, bottomBorder)}
}

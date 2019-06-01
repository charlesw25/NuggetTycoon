package game.physics

object Boundary {

  var next_ID: Int = 0

}
class Boundary(var EndPoint1: PhysicsVector, var EndPoint2: PhysicsVector) {

  val boundaryID: Int = Boundary.next_ID
  Boundary.next_ID += 1
}

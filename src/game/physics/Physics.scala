package game.physics

import java.awt.geom.Line2D

import game.Game

object Physics{
    def updateWorld(Worl: World, deltaTime: Double, game: Game): Unit = {

      val player = Worl.objects.head

      for(obj <- Worl.objects) {

        updateVelocity(obj, Worl, deltaTime)

        val potLocation = computePotentialLocation(obj,deltaTime)
        var collision: Boolean = false
        for(boun <- Worl.boundaries) {
          if(detectCollision(obj,potLocation,boun)) {
            collision = true
            println("Collision Detected")
          }
        }

        if(!collision){
          obj.location.x = potLocation.x
          obj.location.y = potLocation.y
        }
      }

      //if player is near interactable, remove interactable from list and add gold
      Worl.interactables.foreach((i: Interactable) => {
        if(computeDistance(Worl.objects.head.location,i.location) < 5){
          player.currency += 5
          Worl.interactables -= i
          game.player.currency += game.nugsPerPickup()

        }
      })
    }

  def computeDistance(vector1: PhysicsVector, vector2: PhysicsVector): Double = {
    Math.sqrt(Math.pow(vector1.x - vector2.x, 2.0) + Math.pow(vector1.y - vector2.y, 2.0))
  }

    def computePotentialLocation(ObjectX:PhysicalObject,deltaTime: Double): PhysicsVector = {

      val returnLocation: PhysicsVector = new PhysicsVector(0,0,0)


      returnLocation.x = ObjectX.location.x + (ObjectX.velocity.x * deltaTime)
      returnLocation.y = ObjectX.location.y + (ObjectX.velocity.y * deltaTime)
      returnLocation.z = ObjectX.location.z + (ObjectX.velocity.z * deltaTime)

      if (returnLocation.z <= 0){
        returnLocation.z = 0
      }

      returnLocation
    }

    def updateVelocity(ObjectX: PhysicalObject,zaWarudo: World, deltaTime: Double): Unit = {

      val new_vel = ObjectX.velocity.z - (zaWarudo.acceleration * deltaTime)


      if(ObjectX.location.z <= 0.0 && new_vel < 0.0) {
        ObjectX.velocity.z = 0.0
      }
      else {
        ObjectX.velocity.z = new_vel
      }
    }

    def detectCollision(obj: PhysicalObject, vec: PhysicsVector, bound: Boundary): Boolean = {

      if (obj.location.z < 0) {
        return false
      }

      if (vec.z < 0) {
        return false
      }

      val x1 = obj.location.x
      val y1 = obj.location.y
      val x2 = vec.x
      val y2 = vec.y

      val x11 = bound.EndPoint1.x
      val y11 = bound.EndPoint1.y
      val x22 = bound.EndPoint2.x
      val y22 = bound.EndPoint2.y

      val line1 = new Line2D.Double(x1,y1,x2,y2)
      val line2 = new Line2D.Double(x11,y11,x22,y22)

      if(line1.intersectsLine(line2)) {
        true
      }
      else {
        false
      }
    }
}
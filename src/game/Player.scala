package game

import physics.{PhysicalObject, PhysicsVector}

class Player(location: PhysicsVector, velocity: PhysicsVector) extends PhysicalObject(location, velocity) {
  //player currency
  var currency: Double = 0.0

  def showCurrency(): String = {
    "Currency: " + this.currency.toInt.toString
  }

  def clickCurrency(): Unit = {
    this.currency += 1
  }

  //player speed
  val speed: Double = 50.0


  //TODO: improve movement

  //movement methods
  def moveLeft(): Unit = {
    this.velocity.x = -this.speed
  }

  def moveRight(): Unit = {
    this.velocity.x = this.speed
  }

  def moveUp(): Unit = {
    this.velocity.y = -this.speed
  }

  def moveDown(): Unit = {
    this.velocity.y = this.speed
  }

  def releaseLeft(): Unit = {
    this.velocity.x = 0
  }

  def releaseRight(): Unit = {
    this.velocity.x = 0
  }

  def releaseUp(): Unit = {
    this.velocity.y = 0
  }

  def releaseDown(): Unit = {
    this.velocity.y = 0
  }

}

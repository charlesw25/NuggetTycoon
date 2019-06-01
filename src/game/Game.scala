package game

import game.NuggetEquipment._
import physics._

import scala.collection.mutable.ListBuffer

class Game {
  val world = new World(5) //rn acceleration doesnt do anything

  var playerSize: Int = 10
  val player: Player = new Player(new PhysicsVector(75, 75), new PhysicsVector(0,0))

  //World Lists
  var npcList: List[NPC] = List() //list of npcs (empty on startup)
  var walls: ListBuffer[Boundary] = ListBuffer(
    new Boundary(new PhysicsVector(0,2),new PhysicsVector(175,2)), //top
    new Boundary(new PhysicsVector(0,2), new PhysicsVector(0,168)), //left
    new Boundary(new PhysicsVector(168,0), new PhysicsVector(168,200)), //right
    new Boundary(new PhysicsVector(0,168), new PhysicsVector(175,168)) //bottom
  )
  var interactList: ListBuffer[Interactable] = ListBuffer()


  world.objects = ListBuffer(player)
  world.boundaries = walls
  world.interactables = interactList

  var nugquipment: Map[String, NuggetEquipment] = Map("Farm" -> new NuggetFarm, "Factory" -> new NuggetFactory, "Generator" -> new NuggetGenerator)

  // Nugget Calculations
  def nuggetsPerSecond(): Double = {
    var total: Double = 0
    nugquipment.values.foreach((e: NuggetEquipment) => {
      total += e.nugsPerSecond()
    })
    total
  }

  def nugsPerPickup(): Double = {
    var total: Double = 0
    nugquipment.values.foreach((e: NuggetEquipment) => {
      total += e.nugsPerPickup()
    })
    total
  }

  def buyEquipment(equipmentKey: String): Boolean = {
    if(player.currency >= nugquipment(equipmentKey).costOfNextPurchase()) {
      player.currency -= nugquipment(equipmentKey).costOfNextPurchase()
      nugquipment(equipmentKey).numOwned += 1
      true
    }
    false
  }

  def showEquipmentNum(key: String): String = {
    key match {
      case "Farm" => "Farms: " + nugquipment(key).numOwned.toString
      case "Factory" => "Factories: " + nugquipment(key).numOwned.toString
      case "Generator" => "Generators: " + nugquipment(key).numOwned.toString
      case _ => "ono :<"
    }
  }

  def update(deltaTime: Double): Unit = {
    Physics.updateWorld(this.world, deltaTime, this)
    player.currency += this.nuggetsPerSecond() * deltaTime
  }
}

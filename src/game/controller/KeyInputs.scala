package game.controller

import game.{GUI, Player}
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import game.physics.{NPC, PhysicsVector}

import scala.util.Random

class KeyInputs(player: Player) extends EventHandler[KeyEvent]{
  var count = 0
  override def handle(event: KeyEvent): Unit = {
    val key = event.getCode
    event.getEventType.getName match {
      case "KEY_RELEASED" => key.getName match{
        case "A" => println("Left Released"); this.player.releaseLeft()
        case "D" => println("Right Released"); this.player.releaseRight()
        case "W" => println("Up Released"); this.player.releaseUp()
        case "S" => println("Down Released"); this.player.releaseDown()
        case _ =>
      }
      case "KEY_PRESSED" => key.getName match {
        case "A" => println("Left Pressed"); this.player.moveLeft()
        case "D" => println("Right Pressed"); this.player.moveRight()
        case "W" => println("Up Pressed"); this.player.moveUp()
        case "S" => println("Down Pressed"); this.player.moveDown()
        case "E" => println("E Pressed")
          //TODO Tidy everything up involving npc spawning, forreal
          val random: Random = new Random()
//          GUI.game.npcList = GUI.game.npcList ++ List(new NPC(new PhysicsVector(random.nextInt(165),random.nextInt(165))))
//          GUI.game.world.boundaries = GUI.game.world.boundaries ++ GUI.game.npcList(count).returnBorders()
//          count += 1
        case "1" =>
          if(game.GUI.game.buyEquipment("Farm")){
            //TODO add sprites
            game.GUI.game.buyEquipment("Farm")
          }

        case "2" =>
          if(game.GUI.game.buyEquipment("Factory")){
            //TODO add sprites
            game.GUI.game.buyEquipment("Factory")
          }
        case "3" =>
          if(game.GUI.game.buyEquipment("Generator")){
            //TODO add sprites
            game.GUI.game.buyEquipment("Generator")
          }
        case _ =>
      }
      case _ =>
    }
  }
}

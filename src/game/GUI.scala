package game

import controller.KeyInputs
import game.physics._
import javafx.scene.input.{KeyEvent, MouseEvent}
import javafx.scene.paint.ImagePattern
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Group, Scene}
import scalafx.animation.AnimationTimer
import scalafx.scene.image.Image
import scalafx.scene.paint._
import scalafx.scene.shape.{Rectangle, Shape}
import scalafx.scene.text.Text
import scalafx.scene.text.Font

import scala.collection.mutable.ListBuffer
import scala.util.Random

object GUI extends JFXApp {
  var lastUpdateTime: Long = System.nanoTime()

  val scaleFactor: Double = 3.0

  var game = new Game()

  //map that logs npcs with Interactable IDs
  var npcSprites: Map[Int, Shape] = Map[Int,Shape]()

  //Group that holds all sprites/gui elements
  var sceneGfx: Group = new Group {}

  //handy map that logs sprites with Interactable IDs
  var interactableMap: Map[Int,Shape] = Map()

  //Map for Equipment Sprites

  var equipmentSpriteMap: Map[String, ListBuffer[Shape]] = Map(
    "Farm" -> ListBuffer(),
    "Factory" -> ListBuffer(),
    "Generator" -> ListBuffer()
  )

  val random: Random = new Random()

  //convert physics coords to gui coords
  def convert_X(gameX: Double, width: Double): Double = {
    (gameX - width / 2.0) * scaleFactor
  }

  def convert_Y(gameY: Double, height: Double): Double = {
    (gameY - height / 2.0) * scaleFactor
  }

  //method used to create playerSprite
  def playerSpr(x:Double,y:Double,color: Color): Shape = {
    new Rectangle{
      width =  scaleFactor * game.playerSize
      height = scaleFactor * game.playerSize

      translateX = convert_X(x.toDouble, game.playerSize)
      translateY = convert_Y(y.toDouble, game.playerSize)

      fill = Color.DarkOliveGreen
    }
  }

  //method used to create npc
  def createNPC(): Shape = {
    val random: Random = new Random()
    new Rectangle{
      width =  scaleFactor * game.playerSize
      height = scaleFactor * game.playerSize

      translateX = random.nextInt(450)
      translateY = random.nextInt(450)

      fill = Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255))
    }
  }

  //method used to create wallSprite (now unused)
  def createWallSprite(wall: Boundary): Rectangle = {
    val distance: Double = Physics.computeDistance(wall.EndPoint1, wall.EndPoint2)

    new Rectangle() {
      width = distance * scaleFactor
      height = 1.5 * scaleFactor
      translateX = convert_X((wall.EndPoint1.x + wall.EndPoint2.x) / 2.0, width.toDouble)
      translateY = convert_Y((wall.EndPoint1.y + wall.EndPoint2.y) / 2.0, height.toDouble)
      fill = Color.Black
    }
  }

  //Text/Background Stuff
  val tutorialText: Text = new Text(125.0, 250.0,"Click to Spawn a Nugget")
  tutorialText.font = new Font("-fx-font-size: 48pt",30.0)

  //Equipment Counter
  val equipmentTextFarm: Text = new Text(125,150, game.showEquipmentNum("Farm"))
  equipmentTextFarm.font = new Font("-fx-font-size: 48pt",30.0)

  val equipmentTextFactory: Text = new Text(125,175, game.showEquipmentNum("Factory"))
  equipmentTextFactory.font = new Font("-fx-font-size: 48pt",30.0)

  val equipmentTextGenerator: Text = new Text(125,200, game.showEquipmentNum("Generator"))
  equipmentTextGenerator.font = new Font("-fx-font-size: 48pt",30.0)

  //Text that displays the currency
  val currencyText: Text = new Text(115.0, 100, game.player.showCurrency())
  currencyText.font = new Font("-fx-font-size: 48pt",30.0)


  //make the playersprite
  val playerSprite: Shape = playerSpr(game.player.location.x, game.player.location.y, Color.DarkOliveGreen)

//  val image = new Image("game/images/testPlayer.png")
//  val imagePattern = new ImagePattern(image)
//  playerSprite.setFill(imagePattern)

  //add necessary nodes to sceneGfx, playersprite is added last so its on a higher layer
  sceneGfx.children.add(tutorialText)
  sceneGfx.children.add(equipmentTextFarm)
  sceneGfx.children.add(equipmentTextFactory)
  sceneGfx.children.add(equipmentTextGenerator)
  sceneGfx.children.add(currencyText)
  sceneGfx.children.add(playerSprite)


  this.stage = new PrimaryStage {
    this.title = "Nugget Tycoon"
    resizable_=(false) //cant resize, can't fullscreen
    this.icons.add(new Image("game/images/nuggey.png"))
    scene = new Scene(500,500) {
      fill = Color.DarkGray

      content = List(sceneGfx)

      addEventHandler(KeyEvent.ANY, new KeyInputs(game.player))
      onMouseClicked = (e: MouseEvent) => {
        //game.player.clickCurrency()

        game.interactList = game.interactList ++ List(new Interactable(new PhysicsVector(random.nextInt(165),random.nextInt(165))))
        game.world.interactables += game.interactList.last
      }
    }

    def update(time: Long): Unit = {
      val dt: Double = (time - lastUpdateTime) / 1000000000.0
      lastUpdateTime = time
      //update the game method
      game.update(dt)

      currencyText.text.value = game.player.showCurrency()
      equipmentTextFarm.text.value = game.showEquipmentNum("Farm")
      equipmentTextFactory.text.value = game.showEquipmentNum("Factory")
      equipmentTextGenerator.text.value = game.showEquipmentNum("Generator")

      //updates player movement with respect to game location
      playerSprite.translateX.value = convert_X(game.player.location.x, game.playerSize)
      playerSprite.translateY.value = convert_Y(game.player.location.y, game.playerSize)


      //creates npc sprites for every npc in the npcList in gameObject
      //TODO Sprites keep getting added, use map functionality to prevent any repeats
      game.npcList.foreach((npc: NPC) => {
        val sprite: Shape = new Rectangle() {
          width =  scaleFactor * game.playerSize
          height = scaleFactor * game.playerSize
          translateX = convert_X(npc.npcLocation.x, game.playerSize)
          translateY = convert_Y(npc.npcLocation.y, game.playerSize)
          fill = Color.Black
        }
        sceneGfx.children.add(sprite)
      })

      val gameIdList: ListBuffer[Int] = ListBuffer()
      game.world.interactables.foreach((i:Interactable) => {

        gameIdList += i.interactableID

        if(!interactableMap.contains(i.interactableID)){ //if the map doesnt contain the id of the game interactable list
          val interactableSprite: Shape = new Rectangle() {
            width = scaleFactor * (game.playerSize / 2) * 1.5
            height = scaleFactor * (game.playerSize / 2) * 1.5
            translateX = convert_X(i.location.x, game.playerSize / 2)
            translateY = convert_Y(i.location.y, game.playerSize / 2)
            fill = Color.Red
          }
          val image = new Image("game/images/nuggey.png")
          val imagePattern = new ImagePattern(image)
          interactableSprite.setFill(imagePattern)
          sceneGfx.children.add(interactableSprite)
          interactableMap += (i.interactableID -> interactableSprite)
        }
      })

       interactableMap.keys.foreach((id:Int) => {
         //if interactableMapID is not in gameIDList; in other words if interactable is deleted from game, remove from sprite
         if(!gameIdList.contains(id)) {
           sceneGfx.children.remove(sceneGfx.children.indexOf(interactableMap(id)))
           interactableMap = interactableMap.-(id)

         }
       })

      //TODO add sprites for farm. factory, and generator
      val farmSprite: Shape = new Rectangle() {
        width =  scaleFactor * game.playerSize / 4
        height = scaleFactor * game.playerSize / 4

        translateX = 50 + random.nextInt(100)
        translateY = random.nextInt(50)

        fill = Color.rgb(0, 255, 0)
      }

      if(equipmentSpriteMap("Farm").size != game.nugquipment("Farm").numOwned) {
        sceneGfx.children.add(farmSprite)
        equipmentSpriteMap("Farm") += farmSprite
      }

      val factorySprite: Shape = new Rectangle() {
        width = scaleFactor * game.playerSize / 2
        height = scaleFactor * game.playerSize  / 2

        translateX = 150 + random.nextInt(100)
        translateY = random.nextInt(50)

        fill = Color.Blue
      }

      if(equipmentSpriteMap("Factory").size != game.nugquipment("Factory").numOwned) {
        sceneGfx.children.add(factorySprite)
        equipmentSpriteMap("Factory") += factorySprite
      }

      val generatorSprite: Shape = new Rectangle() {
        width =  scaleFactor * game.playerSize
        height = scaleFactor * game.playerSize

        translateX = 250 + random.nextInt(100)
        translateY = random.nextInt(50)

        fill = Color.Gray
      }
      if(equipmentSpriteMap("Generator").size != game.nugquipment("Generator").numOwned) {
        sceneGfx.children.add(generatorSprite)
        equipmentSpriteMap("Generator") += generatorSprite
      }
    }
    AnimationTimer(update).start()
  }
}

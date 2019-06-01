package game.physics

import game.Player

import scala.collection.mutable.ListBuffer

class World(var acceleration: Double) {
  //listbuffers make it easier to manipulate lists
  var objects: ListBuffer[Player] = ListBuffer()
  var boundaries: ListBuffer[Boundary] = ListBuffer()
  var interactables: ListBuffer[Interactable] = ListBuffer()

}

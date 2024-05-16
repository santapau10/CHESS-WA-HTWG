package chess.view

import chess.util.*
import chess.models.{Board, Pieces}
import chess.controller.*

import scala.io.StdIn
import scala.util.matching.Regex

class TUI(controller: Controller) extends Observer with Observable {
  add(this)
  override def update(event: Event): Unit = {
    event match {
      case Event.BOARD_CHANGED => println(controller.boardToString())
        notifyObservers(Event.COORDINATE_INPUT)
      case Event.COORDINATE_INPUT => readCoordinates()
      case _ =>
    }
  }
  def readCoordinates(): Unit = {
    val inputPattern: Regex = "([a-z])(\\d) ([a-z])(\\d)".r
    println("\nGib die Koordinaten im Format Buchstabe-Zahl Buchstabe-Zahl (z.B. a1 b3) ein: ")
    val input = StdIn.readLine().toLowerCase.trim
    controller.handleAction(controller.currentState.actionFromInput(input))
    }
}
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
      case Event.COORDINATE_INPUT => controller.movePieces()
      case _ =>
    }
  }
  def readCoordinates(): (Int,Int,Int,Int) = {
    val inputPattern: Regex = "([a-z])(\\d) ([a-z])(\\d)".r
    println("\nGib die Koordinaten im Format Buchstabe-Zahl Buchstabe-Zahl (z.B. a1 b3) ein: ")
    val input = StdIn.readLine().toLowerCase.trim

    input match {
      case inputPattern(letter1, number1, letter2, number2) =>
        val column1 = letter1.head - 'a'
        val row1 = number1.toInt - 1
        val column2 = letter2.head - 'a'
        val row2 = number2.toInt - 1
        //notifyObservers(Event.BOARD_CHANGED)
        (column1, row1, column2, row2)

      case _ =>
        println("Ungültiges Eingabeformat. Bitte gib die Koordinaten im richtigen Format ein (z.B. a1 b3).")
        notifyObservers(Event.COORDINATE_INPUT)
        null
    }
  }
}
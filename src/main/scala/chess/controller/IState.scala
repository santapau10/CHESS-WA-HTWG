package chess.controller

import scala.util.matching.Regex
import chess.util.{Observable, Observer, *}

trait IState(controller: Controller):

  def print(): Unit
  def actionFromInput(input: String): IAction
  def message: String
private class State(controller: Controller) extends IState(controller):

  def print(): Unit = ()

  def message: String = ""

  def actionFromInput(input: String): IAction = NoAction()

case class GameState(controller: Controller) extends State(controller):

  override def print(): Unit = {
    println(controller.boardToString())
    println("\nEnter the coordinates in the format Letter-Number Letter-Number (e.g., a1 b3): ")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d) ([a-z])(\\d)".r
    input match {
      case inputPattern(letter1, number1, letter2, number2) =>
        val column1 = letter1.head - 'a'
        val row1 = number1.toInt - 1
        val column2 = letter2.head - 'a'
        val row2 = number2.toInt - 1
        if(row1 < controller.size -1 && row1 > -1 && row2 < controller.size -1 && row2 > -1 &&
          column1 < controller.size -1 && column1 > -1 && column2 < controller.size -1 && column2 > -1) {
          MovePiecesAction(column1, row1, column2, row2)
        } else {
          InvalidAction("invalid move")
          StartGame()
        }
      case "undo" =>
        UndoAction()
      case "u" =>
        UndoAction()
      case "redo" =>
        RedoAction()
      case "r" =>
        RedoAction()
      case _ =>
        InvalidAction("invalid format")
        InputAction()
    }
  }

case class PreGameState(controller: Controller) extends State(controller):

  override def print(): Unit = {
    println("welcome to an astonishing round of chess :)")
    println("type 'start' to begin a new game")
  }

  override def message: String = "CHESS"

  override def actionFromInput(input: String): IAction = {
    val i = input.toLowerCase()
    i match
      case "start" =>
        StartGame()
      case _ =>
        InvalidAction("invalid format")
  }
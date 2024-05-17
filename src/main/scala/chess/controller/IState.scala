package chess.controller

import scala.util.matching.Regex
import chess.util.{Observable, Observer, *}

trait IState(controller: Controller):

  def print(): Unit
  def actionFromInput(input: String): IAction
  //def handleInput(input: IUserInput): IAction
  def message: String
  //def toXml: Elem
  //def toJson: JsValue

private class State(controller: Controller) extends IState(controller):

  def print(): Unit = ()

  def message: String = ""

  def actionFromInput(input: String): IAction = NoAction()

  /*
  def handleInput(input: IUserInput): IAction =
    input match
      case UndoInput => UndoAction()
      case RedoInput => RedoAction()
      case InvalidInput(msg) => InvalidAction(msg)
      case _ => NoAction()
  */


  //override def toXml: Elem = <state>{getClass.getSimpleName}</state>

  //override def toJson: JsValue = Json.toJson(getClass.getSimpleName)


case class GameState(controller: Controller) extends State(controller):

  override def print(): Unit = ()

  override def message: String = "CHESS"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d) ([a-z])(\\d)".r
    input match {
      case inputPattern(letter1, number1, letter2, number2) =>
        val column1 = letter1.head - 'a'
        val row1 = number1.toInt - 1
        val column2 = letter2.head - 'a'
        val row2 = number2.toInt - 1
        MovePiecesAction(column1, row1, column2, row2)
      case "undo" =>
        UndoAction()
      case "u" =>
        UndoAction()
      case "redo" =>
        RedoAction()
      case "r" =>
        RedoAction()
      case _ =>
        println("Ungültiges Eingabeformat. Bitte gib die Koordinaten im richtigen Format ein (z.B. a1 b3).")
        InputAction()
    }
  }
  /*
  override def handleInput(input: IUserInput): IAction =
    input match
      case UndoInput => UndoAction()
      case RedoInput => RedoAction()
      case InvalidInput(msg) => InvalidAction(msg)
      case _ => NoAction()
  */
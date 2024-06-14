package chess.controller.controller

import chess.controller.*
import chess.models.*
import chess.models.game.Colors
import chess.util.*

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}


private class State(controller: IController) extends IState(controller):

  def print(): Unit = ()

  def message: String = ""

  def actionFromInput(input: String): IAction = NoAction()

case class TurnStateBlack(controller: IController) extends State(controller):

  override def print(): Unit = {
    println(controller.boardToString())
    println("\nBlacks Turn.Enter the coordinates in the format Letter-Number Letter-Number (e.g., a1 b3): ")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d) ([a-z])(\\d)".r
    Try {
      input match {
        case inputPattern(letter1, number1, letter2, number2) =>
          val column1 = letter1.head - 'a'
          val row1 = number1.toInt - 1
          val column2 = letter2.head - 'a'
          val row2 = number2.toInt - 1
          if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column1 && piece.getCords._2 == row1 && piece.getColor.equals(Colors.BLACK))) {
            MovePiecesBlack(column1, row1, column2, row2)
          } else {
            InvalidAction("invalid move")
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
      }
    } match {
      case Success(action) => action
      case Failure(exception) => InvalidAction("invalid format")
    }
  }

case class PreGameState(controller: IController) extends State(controller):

  override def print(): Unit = {
    println("welcome to an astonishing round of chess :)")
    println("type 'start' to begin a new game")
  }

  override def message: String = "CHESS"

  override def actionFromInput(input: String): IAction = {
    Try {
      val i = input.toLowerCase()
      i match {
        case "start" =>
          StartGame()
        case "s" =>
          StartGame()
        case _ =>
          InvalidAction("invalid format")
      }
    } match {
      case Success(action) => action
      case Failure(exception) => InvalidAction("invalid format")
    }
  }

case class TurnStateWhite(controller: IController) extends State(controller):
  override def print(): Unit = {
    println(controller.boardToString())
    println("\nWhites turn. Enter the coordinates in the format Letter-Number Letter-Number (e.g., a1 b3): ")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d) ([a-z])(\\d)".r
    Try {
      input match {
        case inputPattern(letter1, number1, letter2, number2) =>
          val column1 = letter1.head - 'a'
          val row1 = number1.toInt - 1
          val column2 = letter2.head - 'a'
          val row2 = number2.toInt - 1

          if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column1 && piece.getCords._2 == row1 && piece.getColor.equals(Colors.WHITE))) {
            MovePiecesWhite(column1, row1, column2, row2)
          } else {
            InvalidAction("invalid move")
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
      }
    } match {
      case Success(action) => action
      case Failure(exception) => InvalidAction("invalid format")
    }
  }

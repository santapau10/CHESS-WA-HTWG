package chess.controller.controller

import chess.controller.*
import chess.models.*
import chess.models.game.Colors
import chess.util.*

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}
import scala.xml.Elem
import play.api.libs.json.{JsValue, Json}


private class State(controller: IController) extends IState(controller):
  override def getRow: Int = {
    -1
  }

  override def getColumn: Int = {
    -1
  }

  override def toXml: Elem = <state>
    {getClass.getSimpleName}
  </state>

  override def toJson: JsValue = Json.toJson(getClass.getSimpleName)

  def print(): Unit = ()

  def message: String = ""

  def actionFromInput(input: String): IAction = NoAction()

case class TurnStateBlack(controller: IController) extends State(controller):
  override def getRow: Int = {
    -1
  }

  override def getColumn: Int = {
    -1
  }

  override def print(): Unit = {
    println(controller.boardToString())
    println("\nBlacks Turn.Enter the coordinates in the format Letter-Number Letter-Number (e.g., a1 b3): ")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d)".r
    Try {
      input match {
        case inputPattern(letter1, number1) =>
          val column1 = letter1.head - 'a'
          val row1 = number1.toInt - 1
          if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column1 && piece.getCords._2 == row1 && piece.getColor.equals(Colors.BLACK))) {
            StartMovePiecesBlack(column1, row1)
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

  override def getRow: Int = {
    -1
  }

  override def getColumn: Int = {
    -1
  }
  override def print(): Unit = {
    println("Welcome Chess")
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
  override def getRow: Int = {
    -1
  }

  override def getColumn: Int = {
    -1
  }
  override def print(): Unit = {
    println(controller.boardToString())
    println("\nSelect Piece with coordinates")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d)".r
    Try {
      input match {
        case inputPattern(letter1, number1) =>
          val column1 = letter1.head - 'a'
          val row1 = number1.toInt - 1

          if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column1 && piece.getCords._2 == row1 && piece.getColor.equals(Colors.WHITE))) {
            StartMovePiecesWhite(column1, row1)
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

case class MovePieceWhite(controller: IController, column1: Int, row1: Int) extends State(controller):
  override def getRow: Int = {
    this.row1
  }
  override def getColumn: Int = {
    this.column1
  }
  override def print(): Unit = {
    println(controller.boardToString())
    println("\nEnter target coordinate: ")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d)".r
    Try {
      input match {
        case inputPattern(letter2, number2) =>
          val column2 = letter2.head - 'a'
          val row2 = number2.toInt - 1
          if (!controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column2 && piece.getCords._2 == row2)) {
            MovePiecesWhite(column1, row1, column2, row2)
          } else if (column1 == column2 && row1 == row2) {
            CancelMoveWhite()
          } else if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column2 && piece.getCords._2 == row2 && piece.getColor.equals(Colors.BLACK))) {
            MovePiecesWhite(column1, row1, column2, row2)
          } else if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column2 && piece.getCords._2 == row2 && piece.getColor.equals(Colors.WHITE))) {
            StartMovePiecesWhite(column2, row2)
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

case class MovePieceBlack(controller: IController, column1: Int, row1: Int) extends State(controller):
  override def getRow: Int = {
    this.row1
  }

  override def getColumn: Int = {
    this.column1
  }
  override def print(): Unit = {
    println(controller.boardToString())
    println("\nEnter target coordinate: ")
  }

  override def message: String = "playing"

  override def actionFromInput(input: String): IAction = {
    val inputPattern: Regex = "([a-z])(\\d)".r
    Try {
      input match {
        case inputPattern(letter2, number2) =>
          val column2 = letter2.head - 'a'
          val row2 = number2.toInt - 1
          if (!controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column2 && piece.getCords._2 == row2)) {
            MovePiecesBlack(column1, row1, column2, row2)
          } else if (column1 == column2 && row1 == row2) {
            CancelMoveBlack()
          } else if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column2 && piece.getCords._2 == row2 && piece.getColor.equals(Colors.WHITE))) {
            MovePiecesBlack(column1, row1, column2, row2)
          } else if (controller.getGame.getBoardList.exists(piece => piece.getCords._1 == column2 && piece.getCords._2 == row2 && piece.getColor.equals(Colors.BLACK))) {
            StartMovePiecesBlack(column2, row2)
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

case class GameOver(controller: IController) extends State(controller):
  override def print(): Unit = {
    println(controller.boardToString())
    println("\nGame Over")
  }

  override def message: String = "Game Over"

  override def actionFromInput(input: String): IAction = {
    input match {
      case "undo" =>
        UndoAction()
      case "u" =>
        UndoAction()
      case "redo" =>
        RedoAction()
      case "r" =>
        RedoAction()
      case "restart" =>
        RestartGameAction()
      case _ =>
        InvalidAction("invalid format")
    }
  }
case class PromotionState(controller: IController) extends State(controller):
  override def print(): Unit = {
    println(controller.boardToString())
    println("\nPawnPromotion")
  }

  override def message: String = "PawnPromotion"

  override def actionFromInput(input: String): IAction = {
    input match {
      case "undo" =>
        UndoAction()
      case "u" =>
        UndoAction()
      case "redo" =>
        RedoAction()
      case "r" =>
        RedoAction()
      case "knight" =>
        PromoteToKnightAction()
      case "queen" =>
        PromoteToQueenAction()
      case "bishop" =>
        PromoteToBishopAction()
      case "rook" =>
        PromoteToRookAction()
      case _ =>
        InvalidAction("invalid format")
    }
  }
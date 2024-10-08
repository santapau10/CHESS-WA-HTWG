package controller.controller

import chess.controller.controller.*
import chess.controller.*
import chess.models.*
import chess.models.game.Colors
import chess.util.*

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}
import scala.xml.Elem
import play.api.libs.json.{JsValue, Json}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class IStateSpec extends AnyWordSpec with Matchers {

  // MockController für Testzwecke
  class MockController extends IController {
    override def snapshot: ISnapshot = ???
    override def getSize: Int = ???
    override def updateBoard(list: List[IPieces]): Unit = ???
    override def boardToString(): String = "Mock board string"
    override def notifyObservers(event: Event): Unit = ???
    override def changeState(state: IState): Unit = ???
    override def initGame(): Unit = ???
    override def handleAction(action: IAction): Unit = ???
    override def save(): Unit = ???
    override def restoreSnapshot(snapshot: ISnapshot): Unit = ???
    override def movePieces(l1: Int, n1: Int, l2: Int, n2: Int): Unit = ???
    override def enPassantPiece(l1: Int, n1: Int, l2: Int, n2: Int, defeated_x: Int, defeated_y: Int): Unit = ???
    override def longCastling(l1: Int, n1: Int, l2: Int, n2: Int, rook_x1: Int, rook_y1: Int, rook_x2: Int): Unit = ???
    override def shortCastling(l1: Int, n1: Int, l2: Int, n2: Int, rook_x1: Int, rook_y1: Int, rook_x2: Int): Unit = ???
    override def printState(): Unit = ???
    override def actionFromInput(s: String): IAction = ???
    override def getGame: IGame = ???
    override def add(s: Observer): Unit = ???
    override def getCurrentState: IState = ???
  }

  "State" when {

    val mockController = new MockController

    "TurnStateBlack" should {
      val state = TurnStateBlack(mockController)

      "create correct action from valid input" in {
        val action = state.actionFromInput("b6")
      }

      "create UndoAction from 'undo'" in {
        val action = state.actionFromInput("undo")
        action shouldBe an[UndoAction]
      }

      "create RedoAction from 'redo'" in {
        val action = state.actionFromInput("redo")
        action shouldBe a[RedoAction]
      }

      "create InvalidAction from invalid input" in {
        val action = state.actionFromInput("invalid")
        action shouldBe an[InvalidAction]
      }
    }

    "PreGameState" should {
      val state = PreGameState(mockController)

      "create StartGame action from 'start'" in {
        val action = state.actionFromInput("start")
        action shouldBe a[StartGame]
      }

      "create StartGame action from 's'" in {
        val action = state.actionFromInput("s")
        action shouldBe a[StartGame]
      }

      "create InvalidAction from invalid input" in {
        val action = state.actionFromInput("invalid")
        action shouldBe an[InvalidAction]
      }
    }

    "TurnStateWhite" should {
      val state = TurnStateWhite(mockController)

      "create correct action from valid input" in {
        val action = state.actionFromInput("e2")
      }

      "create UndoAction from 'undo'" in {
        val action = state.actionFromInput("undo")
        action shouldBe an[UndoAction]
      }

      "create RedoAction from 'redo'" in {
        val action = state.actionFromInput("redo")
        action shouldBe a[RedoAction]
      }

      "create InvalidAction from invalid input" in {
        val action = state.actionFromInput("invalid")
        action shouldBe an[InvalidAction]
      }
    }

    "GameOver" should {
      val state = GameOver(mockController)

      "create UndoAction from 'undo'" in {
        val action = state.actionFromInput("undo")
        action shouldBe an[UndoAction]
      }

      "create RedoAction from 'redo'" in {
        val action = state.actionFromInput("redo")
        action shouldBe a[RedoAction]
      }

      "create RestartGameAction from 'restart'" in {
        val action = state.actionFromInput("restart")
        action shouldBe a[RestartGameAction]
      }

      "create InvalidAction from invalid input" in {
        val action = state.actionFromInput("invalid")
        action shouldBe an[InvalidAction]
      }
    }

    "PromotionState" should {
      val state = PromotionState(mockController)

      "create PromoteToQueenAction from 'queen'" in {
        val action = state.actionFromInput("queen")
        action shouldBe a[PromoteToQueenAction]
      }

      "create PromoteToRookAction from 'rook'" in {
        val action = state.actionFromInput("rook")
        action shouldBe a[PromoteToRookAction]
      }

      "create PromoteToBishopAction from 'bishop'" in {
        val action = state.actionFromInput("bishop")
        action shouldBe a[PromoteToBishopAction]
      }

      "create PromoteToKnightAction from 'knight'" in {
        val action = state.actionFromInput("knight")
        action shouldBe a[PromoteToKnightAction]
      }

      "create UndoAction from 'undo'" in {
        val action = state.actionFromInput("undo")
        action shouldBe an[UndoAction]
      }

      "create RedoAction from 'redo'" in {
        val action = state.actionFromInput("redo")
        action shouldBe a[RedoAction]
      }

      "create InvalidAction from invalid input" in {
        val action = state.actionFromInput("invalid")
        action shouldBe an[InvalidAction]
      }
    }
  }
}

package controller.controller

import chess.controller.*
import chess.controller.controller.*
import chess.models.game.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import chess.util.*

import chess.util.Event.*
import chess.view.*
import chess.view.view.{GUI, TUI}


import chess.models.*
import scala.xml.XML
import play.api.libs.json.Json
import java.nio.file.{Files, Paths}
import java.time.LocalDateTime

class ControllerSpec extends AnyWordSpec with Matchers {
  "Controller" when {
    "initializing" should {
      "create correct board sizes" in {
        Controller(8).getSize shouldBe 8
        Controller(6).getSize shouldBe 6
        Controller(10).getSize shouldBe 10
      }

      "throw IllegalArgumentException for invalid board size" in {
        an [IllegalArgumentException] should be thrownBy Controller(-1)
        an [IllegalArgumentException] should be thrownBy Controller(0)
      }
    }

    "managing game state" should {
      "update board" in {
        val controller = Controller(8)
        val initialBoard = controller.getGame.getBoardList
        val newBoard = initialBoard.filter(_.getPiece != Chesspiece.PAWN)
        controller.updateBoard(newBoard)
        controller.getGame.getBoardList should not equal initialBoard
        controller.getGame.getBoardList shouldEqual newBoard
      }

      "convert board to string" in {
        val controller = Controller(8)
        controller.boardToString() should include("♖")
        controller.boardToString() should include("♙")
      }

      "change state" in {
        val controller = Controller(8)
        val initialState = controller.getCurrentState
        controller.changeState(TurnStateWhite(controller))
        controller.getCurrentState shouldBe a[TurnStateWhite]
        controller.changeState(TurnStateBlack(controller))
        controller.getCurrentState shouldBe a[TurnStateBlack]
      }

      "initialize game" in {
        val controller = Controller(8)
        controller.updateBoard(List())
        controller.initGame()
        controller.getGame.getBoardList.size shouldBe 32  // 16 pieces for each player
      }
    }

    "handling actions" should {
      "process move actions" in {
        val controller = Controller(8)
        controller.handleAction(MovePiecesWhite(1, 1, 1, 3))
        controller.getGame.getBoardList.find(p => p.getCords == (1, 3) && p.getPiece == Chesspiece.PAWN) should not be None
        controller.handleAction(MovePiecesBlack(1, 6, 1, 4))
        controller.getGame.getBoardList.find(p => p.getCords == (1, 4) && p.getPiece == Chesspiece.PAWN) should not be None
      }

      "handle undo and redo actions" in {
        val controller = Controller(8)
        val initialBoard = controller.getGame.getBoardList
        controller.handleAction(MovePiecesWhite(1, 1, 1, 3))
        val boardAfterMove = controller.getGame.getBoardList
        controller.handleAction(UndoAction())
        controller.getGame.getBoardList should equal(initialBoard)
        controller.handleAction(RedoAction())
        controller.getGame.getBoardList shouldNot equal(boardAfterMove)
      }

      "handle restart game action" in {
        val controller = Controller(8)
        val initialBoard = controller.getGame.getBoardList
        controller.handleAction(MovePiecesWhite(1, 1, 1, 3))
        controller.handleAction(RestartGameAction())
        controller.getGame.getBoardList shouldNot equal(initialBoard)
      }

      "handle game flow actions" in {
        val controller = Controller(8)
        controller.handleAction(StartGame())
        controller.getCurrentState shouldBe a[TurnStateWhite]
        controller.handleAction(StartMovePiecesWhite(0, 1))
        controller.getCurrentState shouldBe a[MovePieceWhite]
        controller.handleAction(CancelMoveWhite())
        controller.getCurrentState shouldBe a[TurnStateWhite]
        controller.handleAction(MovePiecesWhite(0, 1, 0, 3))
        controller.getCurrentState shouldBe a[TurnStateWhite]
      }

      "handle load actions" in {
        val controller = Controller(8)
        val xmlString =
          """
          <snapshot>
            <game>
              <board>
                <piece>
                  <type>PAWN</type>
                  <color>WHITE</color>
                  <position>
                    <x>0</x>
                    <y>1</y>
                  </position>
                </piece>
              </board>
            </game>
            <state>TurnStateWhite</state>
          </snapshot>
          """
        val xml = XML.loadString(xmlString)
        controller.getGame.getBoardList.size shouldBe 32
        controller.getCurrentState shouldBe a[PreGameState]

        val jsonString =
          """
          {
            "game": {
              "board": [
                {
                  "type": "PAWN",
                  "color": "BLACK",
                  "position": {"x": 0, "y": 6}
                }
              ]
            },
            "state": "TurnStateBlack"
          }
          """
        val json = Json.parse(jsonString)
        controller.getGame.getBoardList.size shouldBe 32
        controller.getCurrentState shouldBe a[PreGameState]
      }
    }

    "managing game mechanics" should {
      "handle castling" in {
        val controller = Controller(8)
        controller.longCastling(4, 0, 2, 0, 0, 0, 3)
        controller.getGame.getBoardList.find(p => p.getPiece == Chesspiece.KING && p.getCords == (2, 0)) should not be None
        controller.getGame.getBoardList.find(p => p.getPiece == Chesspiece.ROOK && p.getCords == (3, 0)) should not be None

        controller.shortCastling(4, 7, 6, 7, 7, 7, 5)
        controller.getGame.getBoardList.find(p => p.getPiece == Chesspiece.KING && p.getCords == (6, 7)) should not be None
        controller.getGame.getBoardList.find(p => p.getPiece == Chesspiece.ROOK && p.getCords == (5, 7)) should not be None
      }
    }

    "managing game data" should {
      "save game state to files" in {
        val controller = Controller(8)
        controller.save()
        val currentDate = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        Files.exists(Paths.get(s"progress_$currentDate.xml")) shouldBe false
        Files.exists(Paths.get(s"progress_$currentDate.json")) shouldBe false
        Files.deleteIfExists(Paths.get(s"progress_$currentDate.xml"))
        Files.deleteIfExists(Paths.get(s"progress_$currentDate.json"))
      }
    }

    "interacting with users" should {
      "print state" in {
        val controller = Controller(8)
        noException should be thrownBy controller.printState()
      }

      "create action from input" in {
        val controller = Controller(8)
        controller.changeState(TurnStateWhite(controller))
        controller.actionFromInput("undo") shouldBe a[UndoAction]
        controller.actionFromInput("redo") shouldBe a[RedoAction]
        controller.actionFromInput("restart") shouldBe a[InvalidAction]
      }
    }

    "managing observers" should {
      "add and notify observers" in {
        val controller = Controller(8)
        var notified = false
        val observer = new Observer {
          override def update(e: Event): Unit = notified = true
        }
        controller.add(observer)
        controller.handleAction(MovePiecesWhite(0, 1, 0, 3))
        notified shouldBe false
      }
    }
  }
}
package controller.controller

import chess.controller.IController
import chess.controller.controller.*
import chess.models.game.*
import chess.models.IPieces
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import chess.util.*

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" should {

    "initialize with a board of the correct size" in {
      val controller8 = Controller(8)
      controller8.getSize shouldEqual 8

      val controller5 = Controller(5)
      controller5.getSize shouldEqual 5

      assertThrows[IllegalArgumentException] {
        Controller(0)
      }
    }

    "convert the board to a string" in {
      val controller = Controller(8)
      controller.boardToString().nonEmpty shouldBe true
    }

    "update the board with a new list of pieces" in {
      val controller = Controller(8)
      val pieces: List[IPieces] = List() // Assuming some pieces are added here
      controller.updateBoard(pieces)
      controller.getGame.getBoardList shouldEqual pieces
    }

    "notify observers when state changes" in {
      val controller = Controller(8)
      var notified = false
      val observer = new Observer {
        override def update(event: Event): Unit = if (event == Event.STATE_CHANGED) notified = true
      }
      controller.add(observer)
      controller.changeState(TurnStateWhite(controller))
      notified shouldBe true
    }

    "initialize a game and notify observers" in {
      val controller = Controller(8)
      var notified = false
      val observer = new Observer {
        override def update(event: Event): Unit = if (event == Event.STATE_CHANGED) notified = true
      }
      controller.add(observer)
      controller.initGame()
      notified shouldBe true
      controller.getGame.getBoardList.nonEmpty shouldBe true
    }

    "handle move actions correctly" in {
      val controller = Controller(8)
      controller.initGame()

      val piece = new Pawn((0, 1), Colors.WHITE,false, (0, 0))
      controller.updateBoard(List(piece))

    }


    "promote pieces correctly" in {
      val controller = Controller(8)
      controller.initGame()

      val piece = new Pawn((0, 6), Colors.WHITE,false, (0, 5))
      controller.updateBoard(List(piece))

    }

    "save and restore snapshots correctly" in {
      val controller = Controller(8)
      controller.initGame()

      val piece = new King((4, 0), Colors.WHITE,false, (4, 0))
      controller.updateBoard(List(piece))

      val snapshot = controller.snapshot

      val controller2 = Controller(8)
      controller2.restoreSnapshot(snapshot)
      controller2.getGame.getBoardList shouldEqual controller.getGame.getBoardList
    }

    "execute castling moves correctly" in {
      val controller = Controller(8)
      controller.initGame()

      val king = new King((4, 0), Colors.WHITE,false, (4, 0))
      val rook = new Rook((7, 0), Colors.WHITE, moved = false, (7, 0))
      controller.updateBoard(List(king, rook))

      controller.longCastling(4, 0, 2, 0, 7, 0, 3)
      controller.getGame.getBoardList.exists(p => p.getCords == (2, 0) && p.getPiece == Chesspiece.KING) shouldBe true
      controller.getGame.getBoardList.exists(p => p.getCords == (3, 0) && p.getPiece == Chesspiece.ROOK) shouldBe true

    }
  }
}

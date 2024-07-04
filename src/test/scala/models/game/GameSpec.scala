package models.game

import chess.models.game.*

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.xml.XML
import chess.controller.*
import chess.models.{Board, IBoardBuilder, IGame, IPieces}
import chess.models.game.*
import chess.models.*
import chess.view.*
import chess.view.view.TUI
import play.api.libs.json.{JsValue, Json, Reads, Writes}
import scala.xml.{Elem, Node}

class GameSpec extends AnyWordSpec with Matchers {

  // Mock IPieces implementation for testing purposes
  case class MockPiece(cords: (Int, Int), color: Colors, moved: Boolean, lastCords: (Int, Int)) extends IPieces {
    override def getLastCords: (Int, Int) = lastCords
    override def isMoved: Boolean = moved
    override def getColor: Colors = color
    override def getPiece: Chesspiece = Chesspiece.PAWN
    override def getCords: (Int, Int) = cords
    override def toString: String = "MockPiece"
    override def getIconPath: String = "mock_icon_path"

    override def toXml: Elem = <mock_piece/>
    override def toJson: JsValue = Json.obj("mocked" -> "piece")
    override def checkMove(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): Boolean = true
  }

  "A Game" when {
    val mockedBoardBuilder = new Board_equal_8(8)
    val mockedPiece1 = King((0, 0), Colors.WHITE, false, (0, 0))
    val mockedPiece2 = Queen((1, 1), Colors.BLACK, false, (0, 0))
    val game = new Game(mockedBoardBuilder, List(mockedPiece1, mockedPiece2))




    "be queried for board piece list" in {
      game.getBoardList shouldEqual List(mockedPiece1, mockedPiece2)
    }

    "check if king is in check" in {
      val checklist = List(mockedPiece1, mockedPiece2)
      game.isKingInCheck(checklist, Colors.WHITE) shouldEqual true
    }

    "check if king is in checkmate" in {
      val checklist = List(mockedPiece1, mockedPiece2)
      game.isKingInCheckmate(checklist, Colors.WHITE) shouldEqual false
    }

    // Additional tests can be added for edge cases and specific scenarios
  }
}

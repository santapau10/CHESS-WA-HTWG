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

  "A Game" should {
    val board = new Board_equal_8(8)
    val whitePawn = new Pawn((0, 1), Colors.WHITE, false, (0, 1))
    val blackKing = new King((4, 0), Colors.BLACK, false, (4, 0))
    val whiteKing = new King((4, 7), Colors.WHITE, false, (4, 7))
    val pieces = List(whitePawn, blackKing, whiteKing)
    val game = new Game(board, pieces)

    "return the correct board" in {
      game.getBoard should be(board)
    }

    "return the correct list of pieces" in {
      game.getBoardList should be(pieces)
    }

    "convert to XML correctly" in {
      val xml = game.toXml
      (xml \\ "game" \\ "board").nonEmpty should be(true)
      (xml \\ "game" \\ "pieces" \\ "pawn").nonEmpty should be(true)
      (xml \\ "game" \\ "pieces" \\ "king").size should be(2)
    }

    "convert to JSON correctly" in {
      val json = game.toJson
      (json \ "game" \ "board").isDefined should be(true)
      (json \ "game" \ "pieces").asOpt[JsValue].map(_.as[Seq[JsValue]].size) should be(Some(3))
    }

    "convert to string correctly" in {
      game.toString should include("♟") // white pawn
      game.toString should include("♚") // black king
      game.toString should include("♔") // white king
    }

    "detect if king is in check" in {
      game.isKingInCheck(pieces, Colors.BLACK) should be(false)
      game.isKingInCheck(pieces, Colors.WHITE) should be(false)

      val checkingPiece = new Rook((4, 1), Colors.BLACK, false, (4, 1))
      val newPieces = checkingPiece :: pieces
      game.isKingInCheck(newPieces, Colors.WHITE) should be(true)
    }

    "detect if king is in checkmate" in {
      game.isKingInCheckmate(pieces, Colors.BLACK) should be(false)
      game.isKingInCheckmate(pieces, Colors.WHITE) should be(false)

      val checkmatePieces = List(
        new King((0, 0), Colors.BLACK, false, (0, 0)),
        new Rook((1, 7), Colors.WHITE, false, (1, 7)),
        new Rook((0, 7), Colors.WHITE, false, (0, 7))
      )
      game.isKingInCheckmate(checkmatePieces, Colors.BLACK) should be(true)
    }
  }

  "Game companion object" should {
    "create a game from XML" in {
      val xml =
        <game>
          <board>
            <field>
              <type>Board_equal_8</type>
              <size>8</size>
            </field>
          </board>
          <pieces>
            <pawn>
              <cords><x>0</x><y>1</y></cords>
              <color>WHITE</color>
              <moved>false</moved>
              <lastcords><x>0</x><y>1</y></lastcords>
            </pawn>
          </pieces>
        </game>

      val game = Game.fromXml(xml)
      game.getBoard.getSize() should be(8)
      game.getBoardList.size should be(1)
      game.getBoardList.head.getPiece should be(Chesspiece.PAWN)
    }

    "create a game from JSON" in {
      val json = Json.parse(
        """
    {
      "game": {
        "board": {
          "className": "Board_equal_8",
          "size": 8
        },
        "pieces": [
          {
            "className": "Pawn",
            "cords": {"x": 0, "y": 1},
            "color": "WHITE",
            "moved": false,
            "lastcords": {"x": 0, "y": 1}
          }
        ]
      }
    }
    """
      )
    }

    "throw an exception for invalid XML" in {
      val invalidXml = <game></game>
      an [IllegalArgumentException] should be thrownBy Game.fromXml(invalidXml)
    }
  }
}
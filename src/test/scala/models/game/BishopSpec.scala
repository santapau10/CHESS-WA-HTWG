package models.game

import chess.models.game.*

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BishopSpec extends AnyWordSpec with Matchers {

  "A Bishop" when {

    val cords = (3, 3)
    val color = Colors.WHITE
    val moved = false
    val lastCords = (0, 0)
    val bishop = new Bishop(cords, color, moved, lastCords)

    "be queried for last coordinates" in {
      bishop.getLastCords shouldEqual lastCords
    }

    "be queried for move status" in {
      bishop.isMoved shouldEqual moved
    }

    "be queried for color" in {
      bishop.getColor shouldEqual color
    }

    "be queried for piece type" in {
      bishop.getPiece shouldEqual Chesspiece.BISHOP
    }

    "be queried for current coordinates" in {
      bishop.getCords shouldEqual cords
    }

    "provide a string representation" in {
      bishop.toString shouldEqual "♝"
    }

    "provide an icon path based on color" in {
      bishop.getIconPath shouldEqual "/white/Bishop.png"
    }


    "convert to JSON" in {
      val expectedJson: JsValue = Json.obj(
        "cords" -> Json.obj("x" -> 3, "y" -> 3),
        "color" -> "WHITE",
        "moved" -> "false",
        "piece" -> "BISHOP",
        "lastcords" -> Json.obj("x" -> 0, "y" -> 0)
      )

      bishop.toJson shouldEqual expectedJson
    }

    "check valid move" in {
      val piecesList = List(
        new Bishop((1, 1), Colors.WHITE, true, (0, 0)),
        new Bishop((3, 3), Colors.BLACK, true, (0, 0)),
        new Bishop((1, 4), Colors.BLACK, true, (0, 0)),
        new Bishop((1, 5), Colors.BLACK, true, (0, 0))
      )

      bishop.checkMove(3, 3, 6, 6, piecesList) shouldEqual true
    }

    "check invalid move" in {
      val piecesList = List(
        new Bishop((1, 1), Colors.WHITE, true, (0, 0)),
        new Bishop((3, 3), Colors.BLACK, true, (0, 0)),
        new Bishop((4, 4), Colors.BLACK, true, (0, 0)),
        new Bishop((5, 5), Colors.BLACK, true, (0, 0))
      )

      bishop.checkMove(3, 3, 5, 5, piecesList) shouldEqual false
    }
  }
}

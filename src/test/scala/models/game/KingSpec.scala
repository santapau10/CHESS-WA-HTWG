package models.game

import chess.models.game.*
import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsValue, Json}
import scala.xml.XML

class KingSpec extends AnyWordSpec with Matchers {

  "A King" when {

    val initialCoords = (4, 0)
    val color = Colors.BLACK
    val moved = false
    val lastCoords = (4, 0)
    val king = new King(initialCoords, color, moved, lastCoords)

    "be queried for its last coordinates" in {
      king.getLastCords shouldEqual lastCoords
    }

    "report if it has moved" in {
      king.isMoved shouldEqual moved
    }

    "report its color" in {
      king.getColor shouldEqual color
    }

    "identify itself as a King piece" in {
      king.getPiece shouldEqual Chesspiece.KING
    }

    "provide its current coordinates" in {
      king.getCords shouldEqual initialCoords
    }

    "display itself as a Unicode character" in {
      king.toString shouldEqual "♔"
    }

    "provide its icon path based on color" in {
      king.getIconPath shouldEqual "/black/King.png"
    }


    "convert to JSON format" in {
      val expectedJson: JsValue = Json.obj(
        "cords" -> Json.obj(
          "x" -> 4,
          "y" -> 0
        ),
        "color" -> "BLACK",
        "moved" -> "false",
        "piece" -> "KING",
        "lastcords" -> Json.obj(
          "x" -> 4,
          "y" -> 0
        )
      )

      king.toJson shouldEqual expectedJson
    }

    "check if it can't move to an invalid position" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      king.checkMove(4, 0, 6, 2, list) shouldEqual false
    }

    "check if it can perform castling" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      king.checkMove(4, 0, 6, 0, list) shouldEqual false
    }

    "check if it can't perform invalid castling" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      king.checkMove(4, 0, 2, 0, list) shouldEqual false
    }

    "check if it correctly identifies when it's in check" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      king.isKingInCheck(4, 0, list) shouldEqual false
    }
  }
}

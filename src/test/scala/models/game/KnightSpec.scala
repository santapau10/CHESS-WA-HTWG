package models.game

import chess.models.game.*

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.xml.XML

class KnightSpec extends AnyWordSpec with Matchers {

  "A Knight" when {

    val initialCoords = (1, 0)
    val color = Colors.WHITE
    val moved = false
    val lastCoords = (1, 0)
    val knight = new Knight(initialCoords, color, moved, lastCoords)

    "be queried for its last coordinates" in {
      knight.getLastCords shouldEqual lastCoords
    }

    "report if it has moved" in {
      knight.isMoved shouldEqual moved
    }

    "report its color" in {
      knight.getColor shouldEqual color
    }

    "identify itself as a Knight piece" in {
      knight.getPiece shouldEqual Chesspiece.KNIGHT
    }

    "provide its current coordinates" in {
      knight.getCords shouldEqual initialCoords
    }

    "display itself as a Unicode character" in {
      knight.toString shouldEqual "♞"
    }

    "provide its icon path based on color" in {
      knight.getIconPath shouldEqual "/white/Knight.png"
    }


    "convert to JSON format" in {
      val expectedJson: JsValue = Json.obj(
        "cords" -> Json.obj(
          "x" -> 1,
          "y" -> 0
        ),
        "color" -> "WHITE",
        "moved" -> "false",
        "piece" -> "KNIGHT",
        "lastcords" -> Json.obj(
          "x" -> 1,
          "y" -> 0
        )
      )

      knight.toJson shouldEqual expectedJson
    }

    "check valid knight moves" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      knight.checkMove(1, 0, 2, 2, list) shouldEqual true
      knight.checkMove(1, 0, 3, 1, list) shouldEqual true
    }

    "check invalid knight moves" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      knight.checkMove(1, 0, 1, 1, list) shouldEqual false // Not a knight move
      knight.checkMove(1, 0, 1, 2, list) shouldEqual false // Not a knight move
    }
  }
}

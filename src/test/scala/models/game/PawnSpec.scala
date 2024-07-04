package models.game

import chess.models.game.*
import chess.models.IPieces
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsValue, Json}
import scala.xml.XML

class PawnSpec extends AnyWordSpec with Matchers {

  "A Pawn" when {

    val initialCoords = (3, 1)
    val color = Colors.WHITE
    val moved = false
    val lastCoords = (3, 1)
    val pawn = new Pawn(initialCoords, color, moved, lastCoords)

    "be queried for its last coordinates" in {
      pawn.getLastCords shouldEqual lastCoords
    }

    "report if it has moved" in {
      pawn.isMoved shouldEqual moved
    }

    "report its color" in {
      pawn.getColor shouldEqual color
    }

    "identify itself as a Pawn piece" in {
      pawn.getPiece shouldEqual Chesspiece.PAWN
    }

    "provide its current coordinates" in {
      pawn.getCords shouldEqual initialCoords
    }

    "display itself as a Unicode character" in {
      pawn.toString shouldEqual "♟"
    }

    "provide its icon path based on color" in {
      pawn.getIconPath shouldEqual "/white/Pawn.png"
    }


    "convert to JSON format" in {
      val expectedJson: JsValue = Json.obj(
        "cords" -> Json.obj(
          "x" -> 3,
          "y" -> 1
        ),
        "color" -> "WHITE",
        "moved" -> "false",
        "piece" -> "PAWN",
        "lastcords" -> Json.obj(
          "x" -> 3,
          "y" -> 1
        )
      )

      pawn.toJson shouldEqual expectedJson
    }

    "check if it can move forward one step" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      pawn.checkMove(3, 1, 3, 2, list) shouldEqual false
    }

    "check if it can move forward two steps from initial position" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      pawn.checkMove(3, 1, 3, 3, list) shouldEqual false
    }

    "check if it can't move forward two steps from non-initial position" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      pawn.checkMove(3, 2, 3, 4, list) shouldEqual false
    }

    "check if it can capture diagonally" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      pawn.checkMove(3, 1, 4, 2, list) shouldEqual false // No piece to capture
    }

    "check if it can perform en passant" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      // Simulate a scenario where en passant is possible
      pawn.checkMove(3, 1, 4, 2, list :+ new Pawn((4, 3), Colors.BLACK, moved = true, (4, 1))) shouldEqual false
    }
  }
}


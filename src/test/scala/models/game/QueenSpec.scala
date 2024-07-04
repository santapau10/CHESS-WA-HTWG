package models.game

import chess.models.game.*

import chess.models.*
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.xml.XML

class QueenSpec extends AnyWordSpec with Matchers {

  "A Queen" when {

    val initialCoords = (3, 3)
    val color = Colors.WHITE
    val moved = false
    val lastCoords = (3, 3)
    val queen = Queen(initialCoords, color, moved, lastCoords)

    "be queried for its last coordinates" in {
      queen.getLastCords shouldEqual lastCoords
    }

    "report if it has moved" in {
      queen.isMoved shouldEqual moved
    }

    "report its color" in {
      queen.getColor shouldEqual color
    }

    "identify itself as a Queen piece" in {
      queen.getPiece shouldEqual Chesspiece.QUEEN
    }

    "provide its current coordinates" in {
      queen.getCords shouldEqual initialCoords
    }

    "display itself as a Unicode character" in {
      queen.toString shouldEqual "♛"
    }

    "provide its icon path based on color" in {
      queen.getIconPath shouldEqual "/white/Queen.png"
    }


    "convert to JSON format" in {
      val expectedJson: JsValue = Json.obj(
        "cords" -> Json.obj(
          "x" -> 3,
          "y" -> 3
        ),
        "color" -> "WHITE",
        "moved" -> "false",
        "piece" -> "QUEEN",
        "lastcords" -> Json.obj(
          "x" -> 3,
          "y" -> 3
        )
      )

      queen.toJson shouldEqual expectedJson
    }

    "check valid queen moves" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      queen.checkMove(3, 3, 5, 5, list) shouldEqual true
      queen.checkMove(3, 3, 1, 3, list) shouldEqual true
      queen.checkMove(3, 3, 3, 6, list) shouldEqual true
    }

    "check invalid queen moves" in {
      val list = List.empty[IPieces] // Replace with actual list for meaningful test
      queen.checkMove(3, 3, 4, 5, list) shouldEqual false // Not a queen move
      queen.checkMove(3, 3, 1, 1, list) shouldEqual true // a queen move
      queen.checkMove(3, 3, 3, 3, list) shouldEqual true
    }
    "check path obstruction" in {
      val list = List(new Pawn((4, 4), Colors.WHITE, moved = true, (4, 4))) // Simulate obstruction
      queen.checkMove(3, 3, 5, 5, list) shouldEqual false
    }
  }
}

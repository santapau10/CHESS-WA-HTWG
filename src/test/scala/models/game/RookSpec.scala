package models.game

import chess.models.game.*

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsObject, JsValue, Json}
import scala.xml.{Elem, Node}

class RookSpec extends AnyWordSpec with Matchers {

  "A Rook" when {

    val cords = (3, 3)
    val color = Colors.WHITE
    val moved = false
    val lastCords = (0, 0)

    val rook = new Rook(cords, color, moved, lastCords)

    "initialized" should {
      "have correct attributes" in {
        rook.getCords should be(cords)
        rook.getColor should be(color)
        rook.isMoved should be(moved)
        rook.getLastCords should be(lastCords)
        rook.getPiece should be(Chesspiece.ROOK)
        rook.toString should be("♜")
        rook.getIconPath should be(if (color == Colors.BLACK) "/black/Rook.png" else "/white/Rook.png")
      }
    }

    "serialize to XML" in {
      val xml: Elem = rook.toXml
      (xml \ "color").text.trim should be(color.toString)
      (xml \ "moved").text.trim should be(moved.toString)
    }

    "serialize to JSON" in {
      val json: JsObject = rook.toJson.as[JsObject]
      (json \ "cords" \ "x").as[Int] should be(cords._1)
      (json \ "cords" \ "y").as[Int] should be(cords._2)
      (json \ "color").as[String] should be(color.toString)
      (json \ "lastcords" \ "x").as[Int] should be(lastCords._1)
    }

    "validate moves correctly" in {
      val board: List[IPieces] = List(
        new Rook((3, 0), Colors.WHITE, moved = false, (0, 0)),
        new Rook((0, 3), Colors.BLACK, moved = false, (0, 0)),
        new Rook((3, 6), Colors.BLACK, moved = false, (0, 0)),
        new Rook((5, 4), Colors.WHITE, moved = false, (0, 0)),
        new Rook((1, 1), Colors.WHITE, moved = false, (0, 0))
      )


      rook.checkMove(3, 3, 3, 5, board) should be(true) // Blocked vertical move
      rook.checkMove(3, 3, 5, 3, board) should be(true) // Blocked horizontal move

      rook.checkMove(3, 3, 1, 1, board) should be(false) // Diagonal move
    }
  }
}

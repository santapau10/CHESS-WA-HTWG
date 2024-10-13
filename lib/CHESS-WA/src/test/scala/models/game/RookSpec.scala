package models.game

import chess.models.game.*

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsObject, JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class RookSpec extends AnyWordSpec with Matchers {
  "A Rook" should {
    val rook = new Rook((4, 4), Colors.WHITE, moved = false, last_cords = (4, 4))
    val blackRook = new Rook((4, 4), Colors.BLACK, moved = true, last_cords = (3, 3))

    "have correct properties" in {
      rook.getCords should be ((4, 4))
      rook.getLastCords should be ((4, 4))
      rook.isMoved should be (false)
      rook.getColor should be (Colors.WHITE)
      rook.getPiece should be (Chesspiece.ROOK)
    }

    "have correct string representation" in {
      rook.toString should be ("♜")
      blackRook.toString should be ("♖")
    }

    "have correct icon path" in {
      rook.getIconPath should be ("/white/Rook.png")
      blackRook.getIconPath should be ("/black/Rook.png")
    }

    "convert to correct XML" in {
      val xml = rook.toXml
      (xml \\ "cords" \ "x").text.trim.toInt should be (4)
      (xml \\ "cords" \ "y").text.trim.toInt should be (4)
      (xml \\ "color").text.trim should be ("WHITE")
      (xml \\ "moved").text.trim.toBoolean should be (false)
      (xml \\ "lastcords" \ "x").text.trim.toInt should be (4)
      (xml \\ "lastcords" \ "y").text.trim.toInt should be (4)
    }

    "convert to correct JSON" in {
      val json = rook.toJson
      (json \ "cords" \ "x").as[Int] should be (4)
      (json \ "cords" \ "y").as[Int] should be (4)
      (json \ "color").as[String] should be ("WHITE")
      (json \ "moved").as[String].toBoolean should be (false)
      (json \ "piece").as[String] should be ("ROOK")
      (json \ "lastcords" \ "x").as[Int] should be (4)
      (json \ "lastcords" \ "y").as[Int] should be (4)
    }

    "validate correct rook moves" in {
      val pieces = List(rook)
      rook.checkMove(4, 4, 4, 8, pieces) should be (true) // Vertical move
      rook.checkMove(4, 4, 8, 4, pieces) should be (true) // Horizontal move
    }

    "invalidate incorrect rook moves" in {
      val pieces = List(rook)
      rook.checkMove(4, 4, 5, 5, pieces) should be (false) // Diagonal move
      rook.checkMove(4, 4, 6, 5, pieces) should be (false) // Knight-like move
    }

    "invalidate moves to squares occupied by same color pieces" in {
      val anotherWhitePiece = new Rook((4, 8), Colors.WHITE, moved = false, last_cords = (4, 8))
      val pieces = List(rook, anotherWhitePiece)
      rook.checkMove(4, 4, 4, 8, pieces) should be (false)
    }

    "validate moves to squares occupied by opposite color pieces" in {
      val blackPiece = new Rook((4, 8), Colors.BLACK, moved = false, last_cords = (4, 8))
      val pieces = List(rook, blackPiece)
      rook.checkMove(4, 4, 4, 8, pieces) should be (true)
    }

    "invalidate moves when path is blocked" in {
      val blockingPiece = new Rook((4, 6), Colors.BLACK, moved = false, last_cords = (4, 6))
      val pieces = List(rook, blockingPiece)
      rook.checkMove(4, 4, 4, 8, pieces) should be (false)
    }
  }
}
package models.game

import chess.models.game.*

import chess.models.*
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.xml.XML


class QueenSpec extends AnyWordSpec with Matchers {
  "A Queen" should {
    val queen = Queen((4, 4), Colors.WHITE, moved = false, last_cords = (4, 4))
    val blackQueen = Queen((4, 4), Colors.BLACK, moved = true, last_cords = (3, 3))

    "have correct properties" in {
      queen.getCords should be ((4, 4))
      queen.getLastCords should be ((4, 4))
      queen.isMoved should be (false)
      queen.getColor should be (Colors.WHITE)
      queen.getPiece should be (Chesspiece.QUEEN)
    }

    "have correct string representation" in {
      queen.toString should be ("♛")
      blackQueen.toString should be ("♕")
    }

    "have correct icon path" in {
      queen.getIconPath should be ("/white/Queen.png")
      blackQueen.getIconPath should be ("/black/Queen.png")
    }

    "convert to correct XML" in {
      val xml = queen.toXml
      (xml \\ "cords" \ "x").text.trim.toInt should be (4)
      (xml \\ "cords" \ "y").text.trim.toInt should be (4)
      (xml \\ "color").text.trim should be ("WHITE")
      (xml \\ "moved").text.trim.toBoolean should be (false)
      (xml \\ "lastcords" \ "x").text.trim.toInt should be (4)
      (xml \\ "lastcords" \ "y").text.trim.toInt should be (4)
    }

    "convert to correct JSON" in {
      val json = queen.toJson
      (json \ "cords" \ "x").as[Int] should be (4)
      (json \ "cords" \ "y").as[Int] should be (4)
      (json \ "color").as[String] should be ("WHITE")
      (json \ "moved").as[String].toBoolean should be (false)
      (json \ "piece").as[String] should be ("QUEEN")
      (json \ "lastcords" \ "x").as[Int] should be (4)
      (json \ "lastcords" \ "y").as[Int] should be (4)
    }

    "validate correct queen moves" in {
      val pieces = List(queen)
      queen.checkMove(4, 4, 4, 8, pieces) should be (true) // Vertical move
      queen.checkMove(4, 4, 8, 4, pieces) should be (true) // Horizontal move
      queen.checkMove(4, 4, 7, 7, pieces) should be (true) // Diagonal move
    }

    "invalidate incorrect queen moves" in {
      val pieces = List(queen)
      queen.checkMove(4, 4, 6, 5, pieces) should be (false) // Knight-like move
      queen.checkMove(4, 4, 5, 7, pieces) should be (false) // Invalid move
    }

    "invalidate moves to squares occupied by same color pieces" in {
      val anotherWhitePiece = Queen((4, 8), Colors.WHITE, moved = false, last_cords = (4, 8))
      val pieces = List(queen, anotherWhitePiece)
      queen.checkMove(4, 4, 4, 8, pieces) should be (false)
    }

    "validate moves to squares occupied by opposite color pieces" in {
      val blackPiece = Queen((4, 8), Colors.BLACK, moved = false, last_cords = (4, 8))
      val pieces = List(queen, blackPiece)
      queen.checkMove(4, 4, 4, 8, pieces) should be (true)
    }

    "invalidate moves when path is blocked" in {
      val blockingPiece = Queen((4, 6), Colors.BLACK, moved = false, last_cords = (4, 6))
      val pieces = List(queen, blockingPiece)
      queen.checkMove(4, 4, 4, 8, pieces) should be (false)
    }
  }
}
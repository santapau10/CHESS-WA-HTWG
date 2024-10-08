package models.game

import chess.models.game.*
import chess.models.IPieces
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.xml.XML


class PawnSpec extends AnyWordSpec with Matchers {
  "A Pawn" should {
    val pawn = new Pawn((4, 2), Colors.WHITE, moved = false, last_cords = (4, 2))
    val blackPawn = new Pawn((4, 7), Colors.BLACK, moved = true, last_cords = (4, 6))

    "have correct properties" in {
      pawn.getCords should be ((4, 2))
      pawn.getLastCords should be ((4, 2))
      pawn.isMoved should be (false)
      pawn.getColor should be (Colors.WHITE)
      pawn.getPiece should be (Chesspiece.PAWN)
    }

    "have correct string representation" in {
      pawn.toString should be ("♟")
      blackPawn.toString should be ("♙")
    }

    "have correct icon path" in {
      pawn.getIconPath should be ("/white/Pawn.png")
      blackPawn.getIconPath should be ("/black/Pawn.png")
    }

    "convert to correct XML" in {
      val xml = pawn.toXml
      (xml \\ "cords" \ "x").text.trim.toInt should be (4)
      (xml \\ "cords" \ "y").text.trim.toInt should be (2)
      (xml \\ "color").text.trim should be ("WHITE")
      (xml \\ "moved").text.trim.toBoolean should be (false)
      (xml \\ "lastcords" \ "x").text.trim.toInt should be (4)
      (xml \\ "lastcords" \ "y").text.trim.toInt should be (2)
    }

    "convert to correct JSON" in {
      val json = pawn.toJson
      (json \ "cords" \ "x").as[Int] should be (4)
      (json \ "cords" \ "y").as[Int] should be (2)
      (json \ "color").as[String] should be ("WHITE")
      (json \ "moved").as[String].toBoolean should be (false)
      (json \ "piece").as[String] should be ("PAWN")
      (json \ "lastcords" \ "x").as[Int] should be (4)
      (json \ "lastcords" \ "y").as[Int] should be (2)
    }

    "validate correct pawn moves" in {
      val pieces = List(pawn)
      pawn.checkMove(4, 2, 4, 3, pieces) should be (true) // One step forward
      pawn.checkMove(4, 2, 4, 4, pieces) should be (true) // Two steps forward (first move)
    }

    "invalidate incorrect pawn moves" in {
      val pieces = List(pawn)
      pawn.checkMove(4, 2, 4, 1, pieces) should be (false) // Backward move
      pawn.checkMove(4, 2, 5, 2, pieces) should be (false) // Sideways move
      pawn.checkMove(4, 2, 5, 3, pieces) should be (false) // Diagonal move without capture
    }

    "validate diagonal capture moves" in {
      val blackPiece = new Pawn((5, 3), Colors.BLACK, moved = false, last_cords = (5, 3))
      val pieces = List(pawn, blackPiece)
      pawn.checkMove(4, 2, 5, 3, pieces) should be (true)
    }

    "invalidate moves when path is blocked" in {
      val blockingPiece = new Pawn((4, 3), Colors.BLACK, moved = false, last_cords = (4, 3))
      val pieces = List(pawn, blockingPiece)
      pawn.checkMove(4, 2, 4, 4, pieces) should be (false)
    }

    "validate en passant capture" in {
      val enPassantPawn = new Pawn((5, 4), Colors.BLACK, moved = false, last_cords = (5, 6))
      val pieces = List(pawn, enPassantPawn)
      pawn.checkMove(4, 4, 5, 5, pieces) should be (false)
    }

    "invalidate en passant capture when conditions are not met" in {
      val nonEnPassantPawn = new Pawn((5, 4), Colors.BLACK, moved = true, last_cords = (5, 5))
      val pieces = List(pawn, nonEnPassantPawn)
      pawn.checkMove(4, 4, 5, 5, pieces) should be (false)
    }
  }
}
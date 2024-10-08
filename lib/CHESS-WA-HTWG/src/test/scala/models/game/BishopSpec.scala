package models.game

import chess.models.game.*

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import chess.models.game.{Bishop, Colors, Chesspiece}

class BishopSpec extends AnyWordSpec with Matchers {

  "A Bishop" should {
    val bishop = new Bishop((3, 3), Colors.WHITE, moved = false, (3, 3))
    val blackBishop = new Bishop((3, 3), Colors.BLACK, moved = true, (2, 2))

    "have correct properties" in {
      bishop.getLastCords should be ((3, 3))
      bishop.isMoved should be (false)
      bishop.getColor should be (Colors.WHITE)
      bishop.getPiece should be (Chesspiece.BISHOP)
      bishop.getCords should be ((3, 3))
    }

    "have correct string representation" in {
      bishop.toString should be ("♝")
      blackBishop.toString should be ("♗")
    }

    "have correct icon path" in {
      bishop.getIconPath should be ("/white/Bishop.png")
      blackBishop.getIconPath should be ("/black/Bishop.png")
    }

    "convert to XML correctly" in {
      val xml = bishop.toXml
      (xml \\ "bishop" \\ "cords" \\ "x").text.trim.toInt should be (3)
      (xml \\ "bishop" \\ "cords" \\ "y").text.trim.toInt should be (3)
      (xml \\ "bishop" \\ "color").text.trim should be ("WHITE")
      (xml \\ "bishop" \\ "moved").text.trim.toBoolean should be (false)
      (xml \\ "bishop" \\ "lastcords" \\ "x").text.trim.toInt should be (3)
      (xml \\ "bishop" \\ "lastcords" \\ "y").text.trim.toInt should be (3)
    }

    "convert to JSON correctly" in {
      val json = bishop.toJson
      (json \ "cords" \ "x").as[Int] should be (3)
      (json \ "cords" \ "y").as[Int] should be (3)
      (json \ "color").as[String] should be ("WHITE")
      (json \ "moved").as[String].toBoolean should be (false)
      (json \ "piece").as[String] should be ("BISHOP")
      (json \ "lastcords" \ "x").as[Int] should be (3)
      (json \ "lastcords" \ "y").as[Int] should be (3)
    }

    "check valid moves correctly" in {
      val emptyBoard: List[IPieces] = List(bishop)

      bishop.checkMove(3, 3, 5, 5, emptyBoard) should be (true)
      bishop.checkMove(3, 3, 1, 1, emptyBoard) should be (true)
      bishop.checkMove(3, 3, 5, 1, emptyBoard) should be (true)
      bishop.checkMove(3, 3, 1, 5, emptyBoard) should be (true)

      bishop.checkMove(3, 3, 4, 4, emptyBoard) should be (true)
      bishop.checkMove(3, 3, 2, 2, emptyBoard) should be (true)
      bishop.checkMove(3, 3, 4, 2, emptyBoard) should be (true)
      bishop.checkMove(3, 3, 2, 4, emptyBoard) should be (true)

      bishop.checkMove(3, 3, 3, 4, emptyBoard) should be (false)
      bishop.checkMove(3, 3, 4, 3, emptyBoard) should be (false)
    }

    "check invalid moves due to obstacles" in {
      val obstaclePiece = new Bishop((4, 4), Colors.BLACK, moved = false, (4, 4))
      val board: List[IPieces] = List(bishop, obstaclePiece)

      bishop.checkMove(3, 3, 5, 5, board) should be (false)
      bishop.checkMove(3, 3, 4, 4, board) should be (true)
    }

    "check valid captures" in {
      val targetPiece = new Bishop((5, 5), Colors.BLACK, moved = false, (5, 5))
      val board: List[IPieces] = List(bishop, targetPiece)

      bishop.checkMove(3, 3, 5, 5, board) should be (true)
    }

    "check invalid captures of same color" in {
      val samColorPiece = new Bishop((5, 5), Colors.WHITE, moved = false, (5, 5))
      val board: List[IPieces] = List(bishop, samColorPiece)

      bishop.checkMove(3, 3, 5, 5, board) should be (false)
    }
  }
}
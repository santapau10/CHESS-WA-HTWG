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

  "A King" should {
    val king = new King((4, 0), Colors.WHITE, moved = false, (4, 0))
    val blackKing = new King((4, 7), Colors.BLACK, moved = true, (4, 7))

    "have correct properties" in {
      king.getLastCords should be ((4, 0))
      king.isMoved should be (false)
      king.getColor should be (Colors.WHITE)
      king.getPiece should be (Chesspiece.KING)
      king.getCords should be ((4, 0))
    }

    "have correct string representation" in {
      king.toString should be ("♚")
      blackKing.toString should be ("♔")
    }

    "have correct icon path" in {
      king.getIconPath should be ("/white/King.png")
      blackKing.getIconPath should be ("/black/King.png")
    }

    "convert to XML correctly" in {
      val xml = king.toXml
      (xml \\ "king" \\ "cords" \\ "x").text.trim.toInt should be (4)
      (xml \\ "king" \\ "cords" \\ "y").text.trim.toInt should be (0)
      (xml \\ "king" \\ "color").text.trim should be ("WHITE")
      (xml \\ "king" \\ "moved").text.trim.toBoolean should be (false)
      (xml \\ "king" \\ "lastcords" \\ "x").text.trim.toInt should be (4)
      (xml \\ "king" \\ "lastcords" \\ "y").text.trim.toInt should be (0)
    }

    "convert to JSON correctly" in {
      val json = king.toJson
      (json \ "cords" \ "x").as[Int] should be (4)
      (json \ "cords" \ "y").as[Int] should be (0)
      (json \ "color").as[String] should be ("WHITE")
      (json \ "moved").as[String].toBoolean should be (false)
      (json \ "piece").as[String] should be ("KING")
      (json \ "lastcords" \ "x").as[Int] should be (4)
      (json \ "lastcords" \ "y").as[Int] should be (0)
    }

    "check valid moves correctly" in {
      val board = List(king)

      king.checkMove(4, 0, 4, 1, board) should be (true)
      king.checkMove(4, 0, 5, 1, board) should be (true)
      king.checkMove(4, 0, 5, 0, board) should be (true)
      king.checkMove(4, 0, 3, 0, board) should be (true)
      king.checkMove(4, 0, 3, 1, board) should be (true)

      king.checkMove(4, 0, 4, 2, board) should be (false)
      king.checkMove(4, 0, 6, 0, board) should be (false)
    }

    "check invalid moves due to other king's proximity" in {
      val otherKing = new King((5, 2), Colors.BLACK, false, (5, 2))
      val board = List(king, otherKing)

      king.checkMove(4, 0, 4, 1, board) should be (false)
      king.checkMove(4, 0, 5, 1, board) should be (false)
    }

    "check valid captures" in {
      val targetPiece = new Pawn((5, 1), Colors.BLACK, false, (5, 1))
      val board = List(king, targetPiece)

      king.checkMove(4, 0, 5, 1, board) should be (true)
    }

    "check invalid captures of same color" in {
      val samColorPiece = new Pawn((5, 1), Colors.WHITE, false, (5, 1))
      val board = List(king, samColorPiece)

      king.checkMove(4, 0, 5, 1, board) should be (false)
    }

    "check castling" in {
      val unmoved_king = new King((4, 0), Colors.WHITE, moved = false, (4, 0))
      val left_rook = new Rook((0, 0), Colors.WHITE, moved = false, (0, 0))
      val right_rook = new Rook((7, 0), Colors.WHITE, moved = false, (7, 0))
      val board = List(unmoved_king, left_rook, right_rook)

      unmoved_king.checkMove(4, 0, 2, 0, board) should be (true) // Left castling
      unmoved_king.checkMove(4, 0, 6, 0, board) should be (true) // Right castling

      val blocking_piece = new Pawn((3, 0), Colors.WHITE, false, (3, 0))
      val blocked_board = blocking_piece :: board
      unmoved_king.checkMove(4, 0, 2, 0, blocked_board) should be (false) // Blocked left castling
    }

    "not allow castling if king has moved" in {
      val moved_king = new King((4, 0), Colors.WHITE, moved = true, (4, 0))
      val rook = new Rook((0, 0), Colors.WHITE, moved = false, (0, 0))
      val board = List(moved_king, rook)

      moved_king.checkMove(4, 0, 2, 0, board) should be (false)
    }

    "not allow castling if rook has moved" in {
      val unmoved_king = new King((4, 0), Colors.WHITE, moved = false, (4, 0))
      val moved_rook = new Rook((0, 0), Colors.WHITE, moved = true, (0, 0))
      val board = List(unmoved_king, moved_rook)

      unmoved_king.checkMove(4, 0, 2, 0, board) should be (false)
    }

    "not allow moves that put the king in check" in {
      val enemy_rook = new Rook((0, 1), Colors.BLACK, false, (0, 1))
      val board = List(king, enemy_rook)

      king.checkMove(4, 0, 3, 0, board) should be (true)
    }
  }
}
package models

import chess.models.*
import chess.models.game.{Chesspiece, Colors, Rook}
import org.scalatest.wordspec.AnyWordSpec

class RookSpec extends AnyWordSpec {

  "A Rook" when {
    "created" should {
      "have correct initial coordinates and color" in {
        val rook = new Rook((3, 4), Colors.WHITE)
        assert(rook.getCords == (3, 4))
        assert(rook.getColor == Colors.WHITE)
      }
    }

    "toString method" should {
      "return '♖' for black rook" in {
        val blackRook = new Rook((0, 0), Colors.BLACK)
        assert(blackRook.toString == "♖")
      }

      "return '♜' for white rook" in {
        val whiteRook = new Rook((0, 0), Colors.WHITE)
        assert(whiteRook.toString == "♜")
      }
    }

    "getPiece method" should {
      "return ROOK" in {
        val rook = new Rook((3, 4), Colors.WHITE)
        assert(rook.getPiece == Chesspiece.ROOK)
      }
    }
  }
}

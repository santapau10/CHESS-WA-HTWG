package models

import chess.models.*
import chess.models.game.{Bishop, Chesspiece, Colors}
import org.scalatest.wordspec.AnyWordSpec

class BishopSpec extends AnyWordSpec {

  "A Bishop" when {
    "created" should {
      "have correct initial coordinates and color" in {
        val bishop = new Bishop((3, 4), Colors.WHITE)
        assert(bishop.getCords == (3, 4))
        assert(bishop.getColor == Colors.WHITE)
      }
    }

    "toString method" should {
      "return '♗' for black bishop" in {
        val blackBishop = new Bishop((0, 0), Colors.BLACK)
        assert(blackBishop.toString == "♗")
      }

      "return '♝' for white bishop" in {
        val whiteBishop = new Bishop((0, 0), Colors.WHITE)
        assert(whiteBishop.toString == "♝")
      }
    }

    "getPiece method" should {
      "return BISHOP" in {
        val bishop = new Bishop((3, 4), Colors.WHITE)
        assert(bishop.getPiece == Chesspiece.BISHOP)
      }
    }
  }
}

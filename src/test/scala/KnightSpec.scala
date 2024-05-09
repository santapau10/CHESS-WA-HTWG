import org.scalatest.wordspec.AnyWordSpec
import chess.models._

class KnightSpec extends AnyWordSpec {

  "A Knight" when {
    "created" should {
      "have correct initial coordinates and color" in {
        val knight = new Knight((3, 4), Colors.WHITE)
        assert(knight.getCords == (3, 4))
        assert(knight.getColor == Colors.WHITE)
      }
    }

    "toString method" should {
      "return '♘' for black knight" in {
        val blackKnight = new Knight((0, 0), Colors.BLACK)
        assert(blackKnight.toString == "♘")
      }

      "return '♞' for white knight" in {
        val whiteKnight = new Knight((0, 0), Colors.WHITE)
        assert(whiteKnight.toString == "♞")
      }
    }

    "getPiece method" should {
      "return KNIGHT" in {
        val knight = new Knight((3, 4), Colors.WHITE)
        assert(knight.getPiece == Chesspiece.KNIGHT)
      }
    }
  }
}

import org.scalatest.wordspec.AnyWordSpec
import Chess.models._

class KingSpec extends AnyWordSpec {

  "A King" when {
    "created" should {
      "have correct initial coordinates and color" in {
        val king = new King((3, 4), Colors.WHITE)
        assert(king.getCords == (3, 4))
        assert(king.getColor == Colors.WHITE)
      }
    }

    "toString method" should {
      "return '♔' for black king" in {
        val blackKing = new King((0, 0), Colors.BLACK)
        assert(blackKing.toString == "♔")
      }

      "return '♚' for white king" in {
        val whiteKing = new King((0, 0), Colors.WHITE)
        assert(whiteKing.toString == "♚")
      }
    }

    "getPiece method" should {
      "return KING" in {
        val king = new King((3, 4), Colors.WHITE)
        assert(king.getPiece == Chesspiece.KING)
      }
    }
  }
}

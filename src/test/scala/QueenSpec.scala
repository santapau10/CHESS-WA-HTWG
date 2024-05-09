import org.scalatest.wordspec.AnyWordSpec
import Chess.models._

class QueenSpec extends AnyWordSpec {

  "A Queen" when {
    "created" should {
      "have correct initial coordinates and color" in {
        val queen = new Queen((3, 4), Colors.WHITE)
        assert(queen.getCords == (3, 4))
        assert(queen.getColor == Colors.WHITE)
      }
    }

    "toString method" should {
      "return '♕' for black queen" in {
        val blackQueen = new Queen((0, 0), Colors.BLACK)
        assert(blackQueen.toString == "♕")
      }

      "return '♛' for white queen" in {
        val whiteQueen = new Queen((0, 0), Colors.WHITE)
        assert(whiteQueen.toString == "♛")
      }
    }

    "getPiece method" should {
      "return QUEEN" in {
        val queen = new Queen((3, 4), Colors.WHITE)
        assert(queen.getPiece == Chesspiece.QUEEN)
      }
    }
  }
}

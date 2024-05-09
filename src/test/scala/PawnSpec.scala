import org.scalatest.wordspec.AnyWordSpec
import chess.models._

class PawnSpec extends AnyWordSpec {

  "A Pawn" when {
    "created" should {
      "have correct initial coordinates and color" in {
        val pawn = new Pawn((3, 4), Colors.WHITE)
        assert(pawn.getCords == (3, 4))
        assert(pawn.getColor == Colors.WHITE)
      }
    }

    "toString method" should {
      "return '♙' for black pawn" in {
        val blackPawn = new Pawn((0, 0), Colors.BLACK)
        assert(blackPawn.toString == "♙")
      }

      "return '♟' for white pawn" in {
        val whitePawn = new Pawn((0, 0), Colors.WHITE)
        assert(whitePawn.toString == "♟")
      }
    }

    "getPiece method" should {
      "return PAWN" in {
        val pawn = new Pawn((3, 4), Colors.WHITE)
        assert(pawn.getPiece == Chesspiece.PAWN)
      }
    }
  }
}

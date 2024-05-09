package models

import chess.models.Board
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class BoardSpec extends AnyWordSpec {

  "A Board" when {
    "moving a piece from a valid position to another valid position" should {
      "return a list of pieces with the piece moved to the new position" in {
        val initialBoard = new Board(8)
        val initialPieces = initialBoard.setupBoard

        val x1 = 1
        val y1 = 0
        val x2 = 3
        val y2 = 3

        val updatedPieces = initialBoard.movePieces(x1, y1, x2, y2, initialPieces)

        val movedPiece = updatedPieces.find(piece => piece.getCords == (x2, y2))
        movedPiece.isDefined shouldBe true

        val originalPiece = updatedPieces.find(piece => piece.getCords == (x1, y1))
        originalPiece.isEmpty shouldBe true
      }
    }

    "moving a piece from an invalid position" should {
      "return null" in {
        val initialBoard = new Board(8)
        val initialPieces = initialBoard.setupBoard

        val x1 = 10
        val y1 = 10
        val x2 = 3
        val y2 = 3

        val updatedPieces = initialBoard.movePieces(x1, y1, x2, y2, initialPieces)

        updatedPieces shouldBe null
      }
    }
  }
}

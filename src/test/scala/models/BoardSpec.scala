package models

import chess.models.game.Board
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class BoardSpec extends AnyWordSpec {

  "Board" should {
    "correctly move pieces on the board" in {
      val board = new Board(8)
      val initialPieces = board.setupBoard

      // Test moving a piece that exists
      val updatedPieces = board.movePieces(0, 1, 0, 3, initialPieces)
      updatedPieces.length shouldBe initialPieces.length
      updatedPieces should not be initialPieces
      updatedPieces.exists(piece => piece.getCords == (0, 3)) shouldBe true

      // Test moving a piece that doesn't exist
      val invalidMove = board.movePieces(10, 10, 12, 12, initialPieces)
      invalidMove shouldBe null // Assuming returning null for invalid move

      // You can add more scenarios to cover other cases
    }

    "correctly generate string representation of the board" in {
      val board = new Board(8)
      val initialPieces = board.setupBoard

      val boardString = board.updateField(initialPieces)
      // Add assertions to verify the correctness of the generated boardString
      // You can check if it contains the expected pieces at their correct positions
    }

    "generate correct field representation for a given position" in {
      val board = new Board(8)
      val initialPieces = board.setupBoard

      val fieldRepresentation = board.checkFieldR(0, 0, initialPieces)
      // Add assertions to verify the correctness of the generated fieldRepresentation
      // You can check if it matches the expected representation for a given position
    }
  }
}

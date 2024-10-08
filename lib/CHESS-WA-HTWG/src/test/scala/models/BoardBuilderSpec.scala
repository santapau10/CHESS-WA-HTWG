package models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import chess.models.*
import chess.models.game.{Chesspiece, Colors, PiecesFactory}
import play.api.libs.json.{JsValue, Json}


class BoardBuilderSpec extends AnyWordSpec with Matchers {

  "A Board" should {
    "have correct size" in {
      val board8 = new Board_equal_8(8)
      board8.getSize() should be(8)

      val board7 = new Board_smaller_8(7)
      board7.getSize() should be(7)

      val board6 = new Board_smaller_8(6)
      board6.getSize() should be(6)

      val board5 = new Board_smaller_8(5)
      board5.getSize() should be(5)

      val board4 = new Board_smaller_8(4)
      board4.getSize() should be(4)

      val board3 = new Board_smaller_8(3)
      board3.getSize() should be(3)

      val board2 = new Board_smaller_8(2)
      board2.getSize() should be(2)
    }

    "return correct setup board" in {
      val board8 = new Board_equal_8(8)
      val setup8 = board8.getSetupBoard
      setup8.size should be(32)
      setup8.count(_.getColor == Colors.WHITE) should be(16)
      setup8.count(_.getColor == Colors.BLACK) should be(16)

      val board7 = new Board_smaller_8(7)
      val setup7 = board7.getSetupBoard
      setup7.size should be(28)

      val board6 = new Board_smaller_8(6)
      val setup6 = board6.getSetupBoard
      setup6.size should be(24)

      val board5 = new Board_smaller_8(5)
      val setup5 = board5.getSetupBoard
      setup5.size should be(20)

      val board4 = new Board_smaller_8(4)
      val setup4 = board4.getSetupBoard
      setup4.size should be(16)

      val board3 = new Board_smaller_8(3)
      val setup3 = board3.getSetupBoard
      setup3.size should be(6)

      val board2 = new Board_smaller_8(2)
      val setup2 = board2.getSetupBoard
      setup2.size should be(4)
    }

    "move pieces correctly" in {
      val board = new Board_equal_8(8)
      val initialSetup = board.getSetupBoard
      val movedPieces = board.movePieces(0, 1, 0, 3, initialSetup) // Move white pawn

      movedPieces.find(_.getCords == (0, 3)).get.getPiece should be(Chesspiece.PAWN)
      movedPieces.find(_.getCords == (0, 1)) should be(None)

      // Test moving to an occupied square
      val capturedPieces = board.movePieces(0, 0, 0, 6, initialSetup) // Move white rook to capture black pawn
      capturedPieces.find(_.getCords == (0, 6)).get.getPiece should be(Chesspiece.ROOK)
      capturedPieces.find(_.getCords == (0, 6)).get.getColor should be(Colors.WHITE)

      // Test moving a non-existent piece
      val nonExistentMove = board.movePieces(4, 4, 5, 5, initialSetup)
      nonExistentMove should be(null)
    }

    "pre-move pieces correctly" in {
      val board = new Board_equal_8(8)
      val initialSetup = board.getSetupBoard
      val premovedPieces = board.preMovePieces(0, 1, 0, 3, initialSetup) // Pre-move white pawn

      premovedPieces.find(_.getCords == (0, 3)).get.getPiece should be(Chesspiece.PAWN)
      premovedPieces.find(_.getCords == (0, 3)).get.isMoved should be(false)
      premovedPieces.find(_.getCords == (0, 1)) should be(None)

      // Test pre-moving to an occupied square
      val capturedPieces = board.preMovePieces(0, 0, 0, 6, initialSetup) // Pre-move white rook to capture black pawn
      capturedPieces.find(_.getCords == (0, 6)).get.getPiece should be(Chesspiece.ROOK)
      capturedPieces.find(_.getCords == (0, 6)).get.getColor should be(Colors.WHITE)
      capturedPieces.find(_.getCords == (0, 6)).get.isMoved should be(false)

      // Test pre-moving a non-existent piece
      val nonExistentMove = board.preMovePieces(4, 4, 5, 5, initialSetup)
      nonExistentMove should be(null)
    }

    "promote piece correctly" in {
      val board = new Board_equal_8(8)
      val initialSetup = board.getSetupBoard
      val promotedSetup = board.promotePiece(Chesspiece.QUEEN, initialSetup)

      promotedSetup.last.getPiece should be(Chesspiece.QUEEN)
    }

    "delete piece correctly" in {
      val board = new Board_equal_8(8)
      val initialSetup = board.getSetupBoard
      val updatedSetup = board.deletePiece(0, 0, initialSetup)

      updatedSetup.find(_.getCords == (0, 0)) should be(None)
      updatedSetup.size should be(initialSetup.size - 1)

      // Test deleting a non-existent piece
      an [IllegalArgumentException] should be thrownBy board.deletePiece(4, 4, initialSetup)
    }

    "generate correct field representation" in {
      val board = new Board_equal_8(8)
      val field = board.updateField(board.getSetupBoard)

      field should include("|")
      field should include("+")
      field should include("♝")
      field should include("♔")
      field should include("a")
      field should include("8")
    }

    "generate first line correctly" in {
      val board = new Board_equal_8(8)
      val firstLine = board.firstLineR(0)

      firstLine should include("a")
      firstLine should include("h")
      firstLine.length should be(40) // 5 spaces per column for 8 columns
    }

    "generate correct XML representation" in {
      val board8 = new Board_equal_8(8)
      val xml8 = board8.toXml()

      (xml8 \\ "type").text.trim should be("Board_equal_8")
      (xml8 \\ "size").text.trim.toInt should be(8)

      val board7 = new Board_smaller_8(7)
      val xml7 = board7.toXml()

      xml7.label should be("game")
      (xml7 \\ "size").text.trim.toInt should be(7)
    }

    "generate correct JSON representation" in {
      val board8 = new Board_equal_8(8)
      val json8 = board8.toJSON()

      (json8 \ "className").as[String] should be("Board_equal_8")
      (json8 \ "size").as[Int] should be(8)

      val board7 = new Board_smaller_8(7)
      val json7 = board7.toJSON()

      (json7 \ "className").as[String] should be("Board_smaller_8")
      (json7 \ "size").as[Int] should be(7)
    }

    "check field correctly" in {
      val board = new Board_equal_8(8)
      val initialSetup = board.getSetupBoard
      val fieldRepresentation = board.checkFieldR(0, 0, initialSetup)

      fieldRepresentation should include("♝")
      fieldRepresentation should include("♔")
      fieldRepresentation should include("|")
    }

    "handle line push correctly for smaller boards" in {
      val board5 = new Board_smaller_8(5)
      val setup5 = board5.getSetupBoard

      setup5.exists(_.getCords == (4, 0)) should be(true)
      setup5.exists(_.getCords == (4, 4)) should be(true)
    }
  }
}
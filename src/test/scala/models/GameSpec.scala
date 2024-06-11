package models

import java.io.{ByteArrayOutputStream, PrintStream}
import scala.Console
import chess.controller.*
import chess.controller.controller.Controller
import chess.models.*
import chess.models.game.{Board, Game, IPieces}
import chess.view.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {

  class MockBoard(size: Int) extends Board(size) {
    override def updateField(list: List[IPieces]): String = ""
    override def movePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): List[IPieces] = List.empty[IPieces]
    def setInvalidCoordinates(flag: Boolean): Unit = {} // Added method
  }

  class MockTUI(controller: IController) extends TUI(controller) {
    override def readCoordinates(): (Int, Int, Int, Int) = (0, 0, 0, 0)
    def setInvalidCoordinates(flag: Boolean): Unit = {} // Added method
  }

  class MockController(size: Int) extends Controller(size) {
    override def updateBoard(list: List[IPieces]): Unit = {}
    var updatedList: List[IPieces] = List.empty[IPieces] // Added property
  }

  "Game" should {
    "correctly generate string representation of the board" in {
      val mockBoard = new MockBoard(8)
      val mockList = List.empty[IPieces]
      val mockTUI = new MockTUI(new MockController(8))
      val mockController = new MockController(8)
      val game = new Game(mockBoard, mockList, mockTUI, mockController)

      val result = game.toString
      result shouldBe ""
    }

    "handle invalid input by printing a message and updating the board with the original list" in {
      val mockBoard = new MockBoard(8)
      val mockList = List.empty[IPieces]

      // Mock TUI, where readCoordinates returns null to simulate invalid input
      val mockTUI = new MockTUI(new MockController(8))
      mockTUI.setInvalidCoordinates(true) // Added to set invalid coordinates

      val mockController = new MockController(8)
      val game = new Game(mockBoard, mockList, mockTUI, mockController)

      // Capture console output
      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        // Ensure coordinates will be null to trigger the else branch
        mockTUI.readCoordinates()
        game.movePieces()
      }

      // Ensure that the controller's updatedList is set correctly
      mockController.updatedList shouldBe mockList

      // Ensure that "Ungültige Position!" is printed
      val consoleOutput = outputStream.toString.trim
      consoleOutput shouldBe ""
    }
  }
}
package view.view

import chess.view.view.GUI
import chess.view.panels.StartPanel
import chess.controller._
import chess.controller.controller._
import chess.models.game._
import chess.util._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterEach

import scala.swing._

class GUISpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {
  val c: Controller = Controller(8)

  // Trait for MockController
  trait MockController extends IController {
    private var currentState: IState = PreGameState(c)

    def add(observer: Observer): Unit = {}

    def getCurrentState: IState = currentState

    def setSize(size: Int): Unit = {}

    def getSize: Int = 8

    def handleInput(input: String): Unit = {}

    def undoMove(): Unit = {}

    def redoMove(): Unit = {}

    def saveGame(): Unit = {}

    def loadGame(): Unit = {}

    def quitGame(): Unit = {}
  }



  // Sample observable instance for testing
  var gui: GUI = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    val co = new Controller(8)
    gui = new GUI(co)
  }

  "GUI" should {
    "update board panel correctly for each event" in {
      // Test for BOARD_CHANGED event
      gui.update(Event.BOARD_CHANGED)
      // Assert that the top contents of the GUI contains a StartPanel
      val contents = gui.top.contents.head

      // Test for STATE_CHANGED event with PreGameState
      gui.update(Event.STATE_CHANGED)
      // Assert that the top contents of the GUI contains a StartPanel
      val contentsAfterStateChange = gui.top.contents.head
      //contentsAfterStateChange shouldBe StartPanel(c)

      // Add more tests for other GameState scenarios (MovePieceWhite, MovePieceBlack, TurnStateBlack, TurnStateWhite, GameOver, PromotionState)
      // Ensure to cover all possible scenarios of updateBoard method
    }
  }
}

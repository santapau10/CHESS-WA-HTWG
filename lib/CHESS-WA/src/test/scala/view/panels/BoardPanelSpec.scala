package view.panels


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterEach

import scala.swing._
import scala.swing.event._
import java.awt.Color
import java.io.File
import javax.swing.ImageIcon
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.FileChooser.{Result, SelectionMode}
import scala.xml.XML
import scala.util.Using
import play.api.libs.json.Json

import chess.view.panels._
import chess.controller._
import chess.controller.controller.Controller
import chess.models._
import chess.models.game.{Colors, Game}
import chess.util._

class BoardPanelSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {

  val b: IBoardBuilder = new Board_equal_8(8)
  var controller: chess.controller.controller.Controller = Controller(8)
  var game: chess.models.game.Game = new Game(b, b.getSetupBoard)

  override def beforeEach(): Unit = {
    controller = new chess.controller.controller.Controller(8) {
      val currentGame: chess.models.IGame = new Game(b, b.getSetupBoard)

      override def getGame: chess.models.IGame = currentGame
      override def handleAction(action: chess.controller.IAction): Unit = ???
      override def getCurrentState: chess.controller.IState = ???
      override def save(): Unit = ???
    }
    game = new chess.models.game.Game(b, b.getSetupBoard)
  }

  "BoardPanel" should {

    "initialize correctly" in {
      val panel = new BoardPanel(8, 8, 50, controller)
      // Test initialization code here
    }

    "handle filePicker correctly" in {
      val panel = new BoardPanel(8, 8, 50, controller)

      // Mock FileChooser behavior
      val mockFileChooser = new FileChooser {
        def showOpenDialog(parent: Component): Result.Value = Result.Approve
        override def selectedFile: File = new File("test.xml")
      }
    }

    "handle button clicks and actions correctly" in {
      val panel = new BoardPanel(8, 8, 50, controller)
      // Test button click handling code here
    }

    "reset button colors and icons correctly" in {
      val panel = new BoardPanel(8, 8, 50, controller)
      // Test button reset code here
    }
  }
}

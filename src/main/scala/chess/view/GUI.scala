package chess.view

import chess.view.panels.*
import chess.controller.*
import chess.controller.controller.{PreGameState, TurnStateBlack, TurnStateWhite}
import chess.util.*

import scala.swing.*
import javax.swing.ImageIcon

class GUI(controller: IController, boardSize: Int) extends SimpleSwingApplication with Observer {

  controller.add(this)

  override def update(event: Event): Unit = {
    event match {
      case Event.BOARD_CHANGED =>
        updateBoard()
      case Event.INPUT =>

      case Event.STATE_CHANGED =>
        updateBoard()
      case _ =>
        println("update ohne passende event")
    }
  }

  val s: Int = boardSize

  // Main frame definition as a lazy value
  lazy val top: MainFrame = new MainFrame {
    title = "Chess"

    // Define the BoardPanel with the controller passed to its constructor
    val boardPanel = new StartPanel(controller)

    // Add the BoardPanel to the center of the frame
    contents = new BorderPanel {
      layout(boardPanel) = BorderPanel.Position.Center
    }

    // Set the size of the frame
    size = new Dimension(600, 600)

    // Center the frame on the screen
    centerOnScreen()
  }

  // Entry point for the application
  def run(): Unit = {
    // Start the GUI application
    main(Array())
  }

  def updateBoard(): Unit = {
    controller.getCurrentState match {
      case _: PreGameState =>
        val oldSize = top.size
        val newBoardPanel = new StartPanel(controller) // Erstellen eines neuen StartPanels
        top.contents = newBoardPanel // Setzen des StartPanels als Inhalt des MainFrame
        top.size = oldSize
        top.visible = true // Sicherstellen, dass das Fenster sichtbar ist

      case _: TurnStateWhite | _: TurnStateBlack =>
        val oldSize = top.size
        val scalledSize = oldSize.height/(s * 2)
        val newBoardPanel = new BoardPanel(s + 2, s + 3, scalledSize, controller) // Erstellen eines neuen BoardPanels
        top.contents = new BorderPanel {
          layout(newBoardPanel) = BorderPanel.Position.Center // Platzieren des BoardPanels in der Mitte
        }
        top.size = oldSize
        top.visible = true // Sicherstellen, dass das Fenster sichtbar ist
    }
  }
}

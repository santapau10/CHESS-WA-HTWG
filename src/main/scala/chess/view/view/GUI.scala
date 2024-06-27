package chess.view.view

import chess.controller.*
import chess.controller.controller.{MovePieceBlack, MovePieceWhite, PreGameState, TurnStateBlack, TurnStateWhite}
import chess.util.*
import chess.view.IGUI
import chess.view.panels.*

import javax.swing.ImageIcon
import scala.swing.*
import com.google.inject.Inject

class GUI @Inject() (controller: IController) extends Frame with Observer with IGUI {

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

  val s: Int = controller.getSize

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
    open() // Öffnen Sie das Fenster explizit
  }

  def updateBoard(): Unit = {
    val currentSize = top.contents.head.size
    controller.getCurrentState match {
      case _: PreGameState =>
        val newBoardPanel = new StartPanel(controller) {preferredSize = currentSize}
        top.contents = newBoardPanel // Nur die Inhalte aktualisieren, nicht den gesamten Panel

      case _: TurnStateWhite | _: TurnStateBlack | _: MovePieceWhite | _: MovePieceBlack =>
        val scalledSize = top.size.height / (s * 2)
        val newBoardPanel = new BoardPanel(s + 2, s + 3, scalledSize, controller) {
          preferredSize = currentSize
        }
        top.contents = new BorderPanel {
          layout(newBoardPanel) = BorderPanel.Position.Center
        }
    }
  }
}
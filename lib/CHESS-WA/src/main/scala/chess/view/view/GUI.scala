package chess.view.view

import chess.controller.*
import chess.controller.controller.{GameOver, MovePieceBlack, MovePieceWhite, PreGameState, PromotionState, TurnStateBlack, TurnStateWhite}
import chess.util.*
import chess.view.IGUI
import chess.view.panels.*

import javax.swing.{ImageIcon, JLayeredPane}
import scala.swing.*
import com.google.inject.Inject

class GUI @Inject()(controller: IController) extends Frame with Observer with IGUI {

  controller.add(this)

  override def update(event: Event): Unit = {
    event match {
      case Event.BOARD_CHANGED =>
        updateBoard()
      case Event.INPUT =>
      case Event.STATE_CHANGED =>
        updateBoard()
      case _ =>
    }
  }

  val s: Int = controller.getSize

  lazy val top: MainFrame = new MainFrame {
    title = "Chess"

    val boardPanel = new StartPanel(controller)

    contents = new BorderPanel {
      layout(boardPanel) = BorderPanel.Position.Center
    }

    size = new Dimension(600, 600)

    centerOnScreen()
    open()
  }

  def updateBoard(): Unit = {
    val currentSize = top.contents.head.size
    controller.getCurrentState match {
      case _: PreGameState =>
        val newBoardPanel = new StartPanel(controller) { preferredSize = currentSize }
        top.contents = newBoardPanel

      case _: MovePieceWhite | _: MovePieceBlack | _: TurnStateBlack | _: TurnStateWhite =>
        val scalledSize = top.size.height / (s * 2)
        val newBoardPanel = new BoardPanel(s + 2, s + 3, scalledSize, controller) {
          preferredSize = currentSize
        }
        top.contents = new BorderPanel {
          layout(newBoardPanel) = BorderPanel.Position.Center
        }

      case _: GameOver =>
        val newBoardPanel = new GameOverPanel(controller) { preferredSize = currentSize }
        top.contents = newBoardPanel

      case _: PromotionState =>
        val newBoardPanel = new PromotionPanel(controller) { preferredSize = currentSize }
        top.contents = newBoardPanel
      case _ =>
    }
  }
}

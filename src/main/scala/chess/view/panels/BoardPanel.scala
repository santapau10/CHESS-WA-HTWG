package chess.view.panels

import scala.swing.*
import scala.swing.event.*
import java.awt.Color
import javax.swing.ImageIcon
import chess.models.*
import chess.controller.*
import chess.controller.controller.{InvalidAction, MovePiecesBlack, MovePiecesWhite, TurnStateBlack, TurnStateWhite}
import chess.models.game.Colors
import chess.models.IPieces


// Erweiterte Button-Klasse, die ein Tupel von Koordinaten akzeptiert
class ChessButton(coords: (Int, Int)) extends Button {
  // Methode zum Abrufen der Koordinaten des Buttons
  def getCords: (Int, Int) = coords
}

class BoardPanel(rows: Int, cols: Int, dimensionSize: Int = 50, controller: IController) extends GridPanel(rows, cols) {
  private var clicks: Option[IPieces] = None // Initialisieren der Option mit None

  super.rows = rows
  super.columns = cols
  val backgroundColor = new Color(200,200,200)

  // Initialize the board with labels and numbers
  val emptyLabel = new Label("") {
    opaque = true
    background = backgroundColor
  }
  contents += emptyLabel
  addLabels(('A' to ('A' + cols - 4).toChar).toList)
  addNumbers(1, cols - 1)

  private def addEmptyLabels(max:Int , n: Int = 0): Unit = {
    if (n <= max) {
      val label = new Label("") {
        opaque = true
        background = backgroundColor
      }
      contents += label
      addEmptyLabels(max, n + 1)
    }
  }

  // Method to add labels
  private def addLabels(alphabet: List[Char], n: Int = 0): Unit = alphabet match {
    case Nil =>
    case letter :: tail =>
      val label = new Label(letter.toString) {
        opaque = true
        background = backgroundColor
      }
      contents += label
      addLabels(tail, n + 1)
  }

  // Method to add numbers and buttons
  private def addNumbers(start: Int, end: Int ): Unit = {
    if (start <= end) {
      if (start == 1) {
        contents += new Label("") {
          opaque = true
          background = backgroundColor
        }
        addNumbers(start + 1, end)
      } else if (start == end) {
        addEmptyLabels(end - 1)
      } else {
        contents += new Label("") {
          opaque = true
          background = backgroundColor
        }
        addButtonsRow(start, 1, end)
        val label = new Label((start - 1).toString) {
          opaque = true
          background = backgroundColor
        }
        contents += label

        addNumbers(start + 1, end)
      }
    }
  }

  // Method to add a row of buttons
  private def addButtonsRow(n: Int, i: Int, s: Int): Unit = {
    if (i < s - 1) {
      val button = new ChessButton((i - 1, n - 2)) // Erstellen eines ChessButton mit Koordinaten-Tupel
      val foundPiece = controller.getGame.getBoardList().find(p => p.getCords.equals(button.getCords))
      val path = foundPiece match {
        case Some(piece) => piece.getIconPath
        case None => ""
      }
      if (path.nonEmpty) {
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance((dimensionSize * 0.8).toInt, (dimensionSize * 0.8).toInt, java.awt.Image.SCALE_SMOOTH))
        button.icon = scaledIcon
      }
      if ((n + i) % 2 == 0) {
        button.background = Color.WHITE
      } else {
        button.background = new Color(99, 176, 199)
      }

      // Hinzufügen des ActionListeners für den ChessButton
      button.reactions += {
        case ButtonClicked(_) =>
          val foundPiece = controller.getGame.getBoardList().find(p => p.getCords.equals(button.getCords))
          if (foundPiece.nonEmpty && clicks.isEmpty) // Überprüfen, ob clicks leer ist
            clicks = foundPiece
          else if (foundPiece.isEmpty && clicks.isDefined) { // Überprüfen, ob foundPiece leer ist und clicks nicht
            val targetCoords = button.getCords
            val sourceCoords = clicks.get.getCords // Abrufen der Koordinaten aus der Option


            clicks match {
              case Some(piece) =>
                controller.getCurrentState match {
                  case _: TurnStateWhite =>
                    if (piece.getColor == Colors.WHITE) {
                      controller.handleAction(MovePiecesWhite(sourceCoords._1, sourceCoords._2, targetCoords._1, targetCoords._2))
                    } else {
                      controller.handleAction(InvalidAction("wrong color"))
                    }
                  case _: TurnStateBlack =>
                    if (piece.getColor == Colors.BLACK) {
                      controller.handleAction(MovePiecesBlack(sourceCoords._1, sourceCoords._2, targetCoords._1, targetCoords._2))
                    } else {
                      controller.handleAction(InvalidAction("wrong color"))
                    }
                  case _ =>
                    controller.handleAction(InvalidAction("balls"))
                }
              case None =>
            }


            clicks = None // Zurücksetzen der Option
          } else if (foundPiece.contains(clicks)) // Überprüfen, ob foundPiece und clicks gleich sind
            clicks = None // Zurücksetzen der Option
      }

      contents += button
      addButtonsRow(n, i + 1, s)
    }
  }
}

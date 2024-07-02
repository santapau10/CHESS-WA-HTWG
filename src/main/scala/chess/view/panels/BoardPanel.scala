package chess.view.panels

import chess.models.*
import chess.controller.*
import chess.controller.controller.{MovePieceBlack, MovePieceWhite, TurnStateBlack, TurnStateWhite}
import chess.models.game.{Colors,PiecesFactory}
import chess.models.IPieces
import chess.util.{Event, Observable}

import scala.swing.*
import scala.swing.event.*
import java.awt.Color
import java.io.File
import javax.swing.ImageIcon
import scala.annotation.tailrec
import javax.swing.filechooser.FileNameExtensionFilter
import scala.io.Source
import scala.swing.FileChooser.{Result, SelectionMode}
import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, FileChooser, Orientation}
import scala.xml.XML
import scala.util.Using
import play.api.libs.json.Json


class ChessButton(coords: (Int, Int), defaultColor: Color) extends Button {
  def getCords: (Int, Int) = coords
  def getDefaultColor: Color = defaultColor
}
class BoardPanel(rows: Int, cols: Int, dimensionSize: Int = 50, controller: IController) extends GridPanel(rows, cols){

  super.rows = rows
  super.columns = cols
  private val backgroundColor = new Color(200,200,200)
  private val selectedButton = new Color(16,78,139)
  private val moveableButton = new Color(21,120,110)


  // Initialize the board with labels and numbers
  private val emptyLabel = new Label("") {
    opaque = true
    background = backgroundColor
  }
  contents += emptyLabel
  addLabels(('A' to ('A' + cols - 4).toChar).toList)
  addNumbers(1, cols - 1)
  @tailrec
  private def addEmptyLabels(max:Int, n: Int = 0): Unit = {
    if (n <= max) {
      if ((n == (max / 2) && max % 2 != 0) || (n == (max / 2) - 1 && max % 2 == 0)) {
        val button = new Button()
        button.border = null
        button.background = backgroundColor
        val path = "/buttons/undo.png"
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
        button.icon = scaledIcon
        contents += button
        button.reactions += {
          case ButtonClicked(_) =>
            controller.handleAction(UndoAction())
        }
        addEmptyLabels(max, n + 1)
      } else if ((n == (max / 2) - 2 && max % 2 == 0) || (n == (max / 2) - 1 && max % 2 != 0)) {
        val button = new Button()
        button.border = null
        button.background = backgroundColor
        val path = "/buttons/import.png"
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
        button.icon = scaledIcon
        contents += button
        button.reactions += {
          case ButtonClicked(_) => filePicker match
            case Some(file) =>
              try
                val extension = file.getName.split("\\.").lastOption.getOrElse("")
                extension.toLowerCase match
                  case "xml" =>
                    val xml = XML.loadFile(file)
                    controller.handleAction(LoadXmlAction(xml))
                  case "json" =>
                    val content = Using(Source.fromFile(file))(_.mkString).getOrElse("")
                    val json = Json.parse(content)
                    controller.handleAction(LoadJsonAction(json))
                  case _ =>
                    println("Unsupported file format")
              catch case e: Exception => e.printStackTrace()
            case None =>
        }
        addEmptyLabels(max, n + 1)
      } else if ((n == (max / 2) + 2 && max % 2 == 0) || (n == (max / 2) + 2 && max % 2 != 0)) {
        val button = new Button()
        button.border = null
        button.background = backgroundColor
        val path = "/buttons/export.png"
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
        button.icon = scaledIcon
        contents += button
        button.reactions += {
        case ButtonClicked(_) =>
        controller.save()
        }
        addEmptyLabels(max, n + 1)
      } else if ((n == (max/2) +1 && max % 2 != 0) || (n == (max / 2) + 1 && max % 2 == 0)) {
        val button = new Button()
        button.border = null
        button.background = backgroundColor
        val path = "/buttons/redo.png"
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
        button.icon = scaledIcon
        contents += button
        button.reactions += {
          case ButtonClicked(_) =>
            controller.handleAction(RedoAction())
        }
        addEmptyLabels(max, n + 1)
      } else {
        val label = new Label("") {
          opaque = true
          background = backgroundColor
        }
        contents += label
        addEmptyLabels(max, n + 1)
      }
    }
  }
  @tailrec
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
  @tailrec
  private def addNumbers(start: Int, end: Int): Unit = {
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
  @tailrec
  private def addButtonsRow(n: Int, i: Int, s: Int): Unit = {
    if (i < s - 1) {

      val background = if ((n + i) % 2 == 0) {
        Color.WHITE
      } else {
        new Color(99, 176, 199)
      }

      val button = new ChessButton((i - 1, n - 2), background) // Erstellen eines ChessButton mit Koordinaten-Tupel
      button.background = button.getDefaultColor
      val foundPiece = controller.getGame.getBoardList.find(p => p.getCords.equals(button.getCords))
      val path = foundPiece match {
        case Some(piece) => piece.getIconPath
        case None => ""
      }
      if (path.nonEmpty) {
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
        
        button.icon = scaledIcon
        button.opaque = true
      }

      button.reactions += {
        case ButtonClicked(b) =>
          resetButtonColors()
          button.background = Color.YELLOW
          val foundPiece = controller.getGame.getBoardList.find(p => p.getCords.equals(button.getCords))
          val coords = button.getCords

          
          foundPiece match {
            case Some(piece) =>
              controller.getCurrentState match {
                case _: TurnStateWhite =>
                  for (c <- contents) {
                    c match {
                      case b: ChessButton =>
                        val updateList = controller.getGame.getBoardList.filterNot(p => p.getCords == foundPiece.get.getCords).filterNot(p => b.getCords == p.getCords) :+ PiecesFactory().addPiece(foundPiece.get.getPiece, b.getCords, foundPiece.get.getColor, foundPiece.get.isMoved)
                        if (foundPiece.get.checkMove(coords._1, coords._2, b.getCords._1, b.getCords._2, controller.getGame.getBoardList) && !controller.getGame.isKingInCheck(updateList, foundPiece.get.getColor)) {
                          b.background = moveableButton
                        }
                      case _ =>
                    }
                  }
                  if (piece.getColor == Colors.WHITE) {
                    controller.handleAction(StartMovePiecesWhite(coords._1, coords._2))
                  } else {
                    controller.handleAction(CancelMoveWhite())
                  }
                case _: TurnStateBlack =>
                  for (c <- contents) {
                    c match {
                      case b: ChessButton =>
                        val updateList = controller.getGame.getBoardList.filterNot(p => p.getCords == foundPiece.get.getCords).filterNot(p => b.getCords == p.getCords) :+ PiecesFactory().addPiece(foundPiece.get.getPiece, b.getCords, foundPiece.get.getColor, foundPiece.get.isMoved)
                        if (foundPiece.get.checkMove(coords._1, coords._2, b.getCords._1, b.getCords._2, controller.getGame.getBoardList) && !controller.getGame.isKingInCheck(updateList, foundPiece.get.getColor)) {
                          b.background = moveableButton
                        }
                      case _ =>
                    }
                  }
                  if (piece.getColor == Colors.BLACK) {
                    controller.handleAction(StartMovePiecesBlack(coords._1, coords._2))
                  } else {
                    controller.handleAction(CancelMoveBlack())
                  }
                case _: MovePieceBlack =>
                  if (coords._1 == controller.getCurrentState.getColumn && coords._2 == controller.getCurrentState.getRow) {
                    controller.handleAction(CancelMoveBlack())
                  } else if (piece.getColor == Colors.WHITE) {
                    controller.handleAction(MovePiecesBlack(controller.getCurrentState.getColumn, controller.getCurrentState.getRow, coords._1, coords._2))
                  } else {
                    controller.handleAction(StartMovePiecesBlack(coords._1,coords._2))
                    for (c <- contents) {
                      c match {
                        case b: ChessButton =>
                          val updateList = controller.getGame.getBoardList.filterNot(p => p.getCords == foundPiece.get.getCords).filterNot(p => b.getCords == p.getCords) :+ PiecesFactory().addPiece(foundPiece.get.getPiece, b.getCords, foundPiece.get.getColor, foundPiece.get.isMoved)
                          if (foundPiece.get.checkMove(coords._1, coords._2, b.getCords._1, b.getCords._2, controller.getGame.getBoardList) && !controller.getGame.isKingInCheck(updateList, foundPiece.get.getColor)) {
                            b.background = moveableButton
                          }
                        case _ =>
                      }
                    }
                  }
                case _: MovePieceWhite =>
                  if (coords._1 == controller.getCurrentState.getColumn && coords._2 == controller.getCurrentState.getRow) {
                    controller.handleAction(CancelMoveWhite())
                  } else if (piece.getColor == Colors.BLACK) {
                    controller.handleAction(MovePiecesWhite(controller.getCurrentState.getColumn, controller.getCurrentState.getRow, coords._1, coords._2))
                  } else {
                    controller.handleAction(StartMovePiecesWhite(coords._1,coords._2))
                    for (c <- contents) {
                      c match {
                        case b: ChessButton =>
                          val updateList = controller.getGame.getBoardList.filterNot(p => p.getCords == foundPiece.get.getCords).filterNot(p => b.getCords == p.getCords) :+ PiecesFactory().addPiece(foundPiece.get.getPiece, b.getCords, foundPiece.get.getColor, foundPiece.get.isMoved)
                          if (foundPiece.get.checkMove(coords._1, coords._2, b.getCords._1, b.getCords._2, controller.getGame.getBoardList) && !controller.getGame.isKingInCheck(updateList, foundPiece.get.getColor)) {
                            b.background = moveableButton
                          }
                        case _ =>
                      }
                    }
                  }
                case _ =>
                  controller.handleAction(InvalidAction("balls"))
              }
            case None =>
              controller.getCurrentState match {
                case _: TurnStateWhite =>
                  controller.handleAction(CancelMoveWhite())
                case _: TurnStateBlack =>
                  controller.handleAction(CancelMoveBlack())
                case _: MovePieceBlack =>
                  controller.handleAction(MovePiecesBlack(controller.getCurrentState.getColumn, controller.getCurrentState.getRow, coords._1, coords._2))
                case _: MovePieceWhite =>
                  controller.handleAction(MovePiecesWhite(controller.getCurrentState.getColumn, controller.getCurrentState.getRow, coords._1, coords._2))
                case _ =>
                  controller.handleAction(InvalidAction("balls"))
              }
          }
      }
      contents += button
      addButtonsRow(n, i + 1, s)
    }
  }

  private def resetButtonColors(): Unit = {
    for (c <- contents) {
      c match {
        case b: ChessButton => b.background = b.getDefaultColor
        case _ =>
      }
    }
  }


  private def filePicker: Option[File] =
    val fileChooser = FileChooser()
    fileChooser.fileSelectionMode = SelectionMode.FilesOnly
    fileChooser.fileFilter = FileNameExtensionFilter("XML/JSON files", "xml", "json")
    val result = fileChooser.showOpenDialog(null)
    if result == Result.Approve then Some(fileChooser.selectedFile) else None
}

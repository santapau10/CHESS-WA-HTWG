package chess.view

import java.awt.Color
import scala.swing.{Color, *}
import scala.swing.event.*
import javax.swing.ImageIcon
import scala.util.Try

object GUI extends SimpleSwingApplication {
  val dimensionSize = 600
  val s = 8 // Größe des Schachbretts
  val alphabet = ('A' to ('A' + s - 1).toChar).toList
  val color = new Color(220, 220, 220)

  // Panel für das Schachbrett
  val boardPanel = new GridPanel(s + 1, s + 1)

  def top: Frame = new MainFrame {
    title = "Schachbrett"

    // Set the background color of the MainFrame
    background = color // Light gray background color

    // Erste Zeile (Buchstaben)
    addLabels(alphabet)

    // Erste Spalte (Zahlen)
    addNumbers(1, s)

    // Haupt-Layout
    contents = new BorderPanel {
      layout(boardPanel) = BorderPanel.Position.Center
      border = Swing.EmptyBorder(0, 30, 30, 0) // Rand um das GridPanel
      // Set the background color of the BorderPanel
      background = color // Ensure the border panel matches the frame background color
    }

    // GUI-Eigenschaften
    resizable = false
    size = new Dimension(dimensionSize, dimensionSize)
    centerOnScreen()
  }

  def addLabels(alphabet: List[Char], n: Int = 0): Unit = alphabet match {
    case Nil =>
    case letter :: tail =>
      val label = new Label(letter.toString) {
        opaque = true
        background = color // Set background color to blue
      }
      boardPanel.contents += label
      addLabels(tail, n + 1)
  }


  def addNumbers(start: Int, end: Int): Unit = {
    if (start <= end) {
      if (start == 1) {
        boardPanel.contents += new Label("") {
          opaque = true
          background = color // Set background color to blue
        }
        addButtonsRow(start, 1, end)
        addNumbers(start + 1, end)
      } else {
        val label = new Label((start - 1).toString) {
          opaque = true
          background = color // Set background color to blue
        }
        boardPanel.contents += label
        addButtonsRow(start, 1, end)
        addNumbers(start + 1, end)
      }
    } else {
      boardPanel.contents += new Label((start - 1).toString) {
        opaque = true
        background = color // Set background color to blue
      }
    }
  }


  def addButtonsRow(n: Int, i: Int, s: Int): Unit = {
    if (i <= s) {
      val button = new Button() {
        override def paintComponent(g: Graphics2D): Unit = {
          super.paintComponent(g)
        }
      }

      if ((n + i) % 2 == 0) {
        button.background = Color.WHITE
      } else {
        val path = "/black/Knight.png"
        val ic = new ImageIcon(getClass.getResource(path))
        val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance((dimensionSize * 0.08).toInt, (dimensionSize * 0.08).toInt, java.awt.Image.SCALE_SMOOTH))
        button.background = new Color(99, 176, 199)
        button.icon = scaledIcon
      }
      boardPanel.contents += button
      addButtonsRow(n, i + 1, s)
    }
  }

  def loadImageIcon(path: String): Option[ImageIcon] = {
    Try {
      val stream = getClass.getResourceAsStream(path)
      require(stream != null, s"Das Bild unter dem Pfad '$path' konnte nicht gefunden werden.")
      val image = javax.imageio.ImageIO.read(stream)
      require(image != null, s"Das Bild unter dem Pfad '$path' konnte nicht geladen werden: Das Image-Objekt ist null.")
      new ImageIcon(image)
    }.toOption
  }
}

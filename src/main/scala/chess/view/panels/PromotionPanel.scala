package chess.view.panels

import chess.controller.{IController, PromoteToBishopAction, PromoteToKnightAction, PromoteToQueenAction, PromoteToRookAction}

import java.awt.Font
import java.awt.FontFormatException
import java.io.IOException
import java.io.InputStream
import javax.swing.{ImageIcon, JButton, JLabel, JPanel}
import java.awt.{Color, Graphics, Graphics2D, RenderingHints}
import javax.swing.border.EmptyBorder
import scala.swing.*
import scala.swing.BorderPanel.Position.*
import scala.swing.event.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class RoundedButton extends JButton {
  setContentAreaFilled(false)
  setFocusPainted(false)
  setBorder(new EmptyBorder(0, 0, 0, 0))

  override def paintComponent(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setColor(new Color(0, 170, 204))
    g2.fillRoundRect(0, 0, getWidth, getHeight, 30, 30)
    super.paintComponent(g)
  }
}

class PromotionPanel(controller: IController) extends BorderPanel {

  val dimensionSize = 50

  // Methode zum Laden eines benutzerdefinierten Fonts
  def loadCustomFont(path: String, size: Float): Font = {
    try {
      val stream: InputStream = getClass.getResourceAsStream(path)
      val baseFont = Font.createFont(Font.TRUETYPE_FONT, stream)
      baseFont.deriveFont(size)
    } catch {
      case e: FontFormatException => throw new RuntimeException(e)
      case e: IOException => throw new RuntimeException(e)
    }
  }

  val customFont = loadCustomFont("/fonts/font.ttf", 60f)

  private val button_knight = new RoundedButton

  val path_knight = "/white/Knight.png"
  val ic_knight = new ImageIcon(getClass.getResource(path_knight))
  val scaledIcon_knight = new ImageIcon(ic_knight.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_knight.setIcon(scaledIcon_knight)


  button_knight.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      println("Knight selected")
      controller.handleAction(PromoteToKnightAction())
    }
  })

  private val button_rook = new RoundedButton

  val path_rook = "/white/Rook.png"
  val ic_rook = new ImageIcon(getClass.getResource(path_rook))
  val scaledIcon_rook = new ImageIcon(ic_rook.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_rook.setIcon(scaledIcon_rook)

  button_rook.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      println("Rook selected")
      controller.handleAction(PromoteToRookAction())
    }
  })

  private val button_bishop = new RoundedButton

  val path_bishop = "/white/Bishop.png"
  val ic_bishop = new ImageIcon(getClass.getResource(path_bishop))
  val scaledIcon_bishop = new ImageIcon(ic_bishop.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_bishop.setIcon(scaledIcon_bishop)

  button_bishop.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      controller.handleAction(PromoteToBishopAction())
    }
  })

  private val button_queen = new RoundedButton

  val path_queen = "/white/Queen.png"
  val ic_queen = new ImageIcon(getClass.getResource(path_queen))
  val scaledIcon_queen = new ImageIcon(ic_queen.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_queen.setIcon(scaledIcon_queen)

  button_queen.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      println("Queen selected")
      controller.handleAction(PromoteToQueenAction())
    }
  })

  private val gridPanel = new GridPanel(rows0 = 2, cols0 = 2) {
    hGap = 10
    vGap = 10

    contents += Component.wrap(button_rook)
    contents += Component.wrap(button_bishop)
    contents += Component.wrap(button_queen)
    contents += Component.wrap(button_knight)
  }

  private val promotionLabel = new Label("Promotion") {
    font = customFont
    horizontalAlignment = Alignment.Center
  }

  private val labelPanel = new BorderPanel {
    layout(promotionLabel) = Center
    border = Swing.EmptyBorder(0, 0, 60, 0)  // Adds space between label and gridPanel
  }

  private val mainPanel = new BorderPanel {
    layout(labelPanel) = North
    layout(gridPanel) = Center
  }

  private val paddingPanel = new BorderPanel {
    layout(mainPanel) = Center
    border = Swing.EmptyBorder(60, 120, 120, 120)
  }

  layout(paddingPanel) = Center
}

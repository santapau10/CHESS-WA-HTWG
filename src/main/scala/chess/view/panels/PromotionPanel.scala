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

class RoundedButton(icon: ImageIcon) extends JButton(icon) {
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

  val customFont = loadCustomFont("/fonts/font2.ttf", 60f)

  private val button_knight = new RoundedButton(new ImageIcon(getClass.getResource("/white/Knight.png").getPath))
  val icon_knight = new ImageIcon(button_knight.getIcon.asInstanceOf[ImageIcon].getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_knight.setIcon(icon_knight)
  button_knight.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      println("Knight selected")
      controller.handleAction(PromoteToKnightAction())
    }
  })

  private val button_rook = new RoundedButton(new ImageIcon(getClass.getResource("/white/Rook.png").getPath))
  val icon_rook = new ImageIcon(button_rook.getIcon.asInstanceOf[ImageIcon].getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_rook.setIcon(icon_rook)
  button_rook.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      println("Rook selected")
      controller.handleAction(PromoteToRookAction())
    }
  })

  private val button_bishop = new RoundedButton(new ImageIcon(getClass.getResource("/white/Bishop.png").getPath))
  val icon_bishop = new ImageIcon(button_bishop.getIcon.asInstanceOf[ImageIcon].getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_bishop.setIcon(icon_bishop)
  button_bishop.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      controller.handleAction(PromoteToBishopAction())
    }
  })

  private val button_queen = new RoundedButton(new ImageIcon(getClass.getResource("/white/Queen.png").getPath))
  val icon_queen = new ImageIcon(button_queen.getIcon.asInstanceOf[ImageIcon].getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))
  button_queen.setIcon(icon_queen)
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
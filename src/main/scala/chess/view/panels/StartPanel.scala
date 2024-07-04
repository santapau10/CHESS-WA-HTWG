package chess.view.panels

import chess.controller.{IController, StartGame}

import scala.swing.*
import scala.swing.event.*
import javax.swing.ImageIcon
import java.awt.geom.Ellipse2D
import java.awt.{Color, Dimension, Font, FontFormatException, Insets}
import java.io.{File, IOException}


class RoundButton(icon: ImageIcon) extends Button {
  super.icon = icon
  maximumSize = new Dimension(50, 50)
  borderPainted = false
  focusPainted = false 
  opaque = false 

  override def paintComponent(g: Graphics2D): Unit = {
    val diameter = Math.min(size.width, size.height)
    val x = (size.width - diameter) / 2
    val y = (size.height - diameter) / 2
    
    g.setColor(new Color(239, 239, 239))
    g.fillOval(x, y, diameter, diameter)

    if (icon != null) {
      val iconX = x + (diameter - icon.getIconWidth) / 2
      val iconY = y + (diameter - icon.getIconHeight) / 2
      icon.paintIcon(peer, g, iconX, iconY)
    }
  }
  
  override def preferredSize: Dimension = {
    new Dimension(50, 50)
  }
}

class StartPanel(controller: IController) extends GridBagPanel {

  val dimensionSize = 190

  val ic = new ImageIcon(getClass.getResource("/buttons/start.png"))
  val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))

  val button = new RoundButton(scaledIcon)

  // Lade die Schriftart aus dem angegebenen Pfad
  var customFont: Font = new Font("Serif", Font.PLAIN, 60) // Fallback-Font
  try {
    val fontStream = getClass.getResourceAsStream("/fonts/font.ttf")
    customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(60f)
  } catch {
    case e: FontFormatException => e.printStackTrace()
    case e: IOException => e.printStackTrace()
  }

  val startGameLabel = new Label("Start Game") {
    font = customFont
  }

  // Layout für das Label
  private val gbcLabel = new Constraints
  gbcLabel.gridx = 0
  gbcLabel.gridy = 0
  gbcLabel.insets = new Insets(10, 50, 20, 50) // Verringert den oberen Abstand und erhöht den unteren Abstand
  layout(startGameLabel) = gbcLabel

  // Layout für den Button
  private val gbcButton = new Constraints
  gbcButton.gridx = 0
  gbcButton.gridy = 1
  gbcButton.insets = new Insets(20, 50, 50, 50) // Erhöht den oberen Abstand
  layout(button) = gbcButton

  listenTo(button)
  reactions += {
    case ButtonClicked(`button`) =>
      controller.handleAction(StartGame())
  }
}

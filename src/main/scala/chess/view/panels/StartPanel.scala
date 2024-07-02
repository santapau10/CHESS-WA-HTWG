package chess.view.panels

import chess.controller.{IController, StartGame}


import scala.swing.*
import scala.swing.event.*
import javax.swing.ImageIcon
import java.awt.geom.Ellipse2D
import java.awt.{Color, Dimension, Insets}

class RoundButton(icon: ImageIcon) extends Button {
  super.icon = icon
  maximumSize = new Dimension(50, 50)
  borderPainted = false
  private val backgroundColor = new Color(239,239,239)

  override def paintComponent(g: Graphics2D): Unit = {
    val diameter = Math.min(size.width, size.height)
    val x = (size.width - diameter) / 2
    val y = (size.height - diameter) / 2

    g.setColor(backgroundColor)
    g.fill(new Ellipse2D.Double(x, y, diameter, diameter))

    if (icon != null) {
      val iconX = x + (diameter - icon.getIconWidth) / 2
      val iconY = y + (diameter - icon.getIconHeight) / 2
      icon.paintIcon(peer, g, iconX, iconY)
    }
  }
}

class StartPanel(controller: IController) extends GridBagPanel {

  val dimensionSize = 190

  val ic = new ImageIcon(getClass.getResource("/buttons/start.png"))
  val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))

  val button = new RoundButton(scaledIcon)

  private val gbc = new Constraints
  gbc.gridx = 0
  gbc.gridy = 0
  gbc.insets = new Insets(50, 50, 50, 50)
  layout(button) = gbc

  listenTo(button)
  reactions += {
    case ButtonClicked(`button`) =>
      controller.handleAction(StartGame())
  }
}

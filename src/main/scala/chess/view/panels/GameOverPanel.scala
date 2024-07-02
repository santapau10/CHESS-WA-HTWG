package chess.view.panels

import chess.controller.{IController, RestartGameAction, StartGame}

import scala.swing.*
import scala.swing.event.*
import javax.swing.ImageIcon
import java.awt.geom.Ellipse2D
import java.awt.{Color, Dimension, Insets}


class GameOverPanel(controller: IController) extends GridBagPanel {

  val dimensionSize = 190
  private val backgroundColor = new Color(200, 200, 200)

  val ic = new ImageIcon(getClass.getResource("/buttons/restart.png"))
  val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))

  val button = new RoundButton(scaledIcon)
  button.border = null
  button.background = backgroundColor
  private val gbc = new Constraints
  gbc.gridx = 0
  gbc.gridy = 0
  gbc.insets = new Insets(50, 50, 50, 50)
  layout(button) = gbc

  listenTo(button)
  reactions += {
    case ButtonClicked(`button`) =>
      controller.handleAction(RestartGameAction())
  }
}

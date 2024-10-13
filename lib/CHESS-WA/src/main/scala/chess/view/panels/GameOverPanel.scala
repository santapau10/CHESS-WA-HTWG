package chess.view.panels

import chess.controller.{IController, RestartGameAction}

import scala.swing.*
import scala.swing.event.*
import javax.swing.ImageIcon
import java.awt.geom.Ellipse2D
import java.awt.{Color, Dimension, Font, FontFormatException, Insets}
import java.io.{File, IOException}

class GameOverPanel(controller: IController) extends GridBagPanel {

  val dimensionSize = 190

  private val backgroundColor = new Color(200, 200, 200, 100)

  val ic = new ImageIcon(getClass.getResource("/buttons/restart.png"))
  val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))

  val button = new RoundButton(scaledIcon)
  button.border = null
  button.background = backgroundColor

  // Lade die Schriftart aus dem angegebenen Pfad
  var customFont: Font = new Font("Serif", Font.PLAIN, 60) // Fallback-Font
  try {
    val fontStream = getClass.getResourceAsStream("/fonts/font.ttf")
    customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(60f)
  } catch {
    case e: FontFormatException => e.printStackTrace()
    case e: IOException => e.printStackTrace()
  }

  val gameOverLabel = new Label("Game Over") {
    font = customFont
  }

  // Layout für das Label
  private val gbcLabel = new Constraints
  gbcLabel.gridx = 0
  gbcLabel.gridy = 0
  gbcLabel.insets = new Insets(0, 50, 20, 50) // Verringert den oberen Abstand um 10, erhöht den unteren Abstand um 10
  layout(gameOverLabel) = gbcLabel

  // Layout für den Button
  private val gbcButton = new Constraints
  gbcButton.gridx = 0
  gbcButton.gridy = 1
  gbcButton.insets = new Insets(30, 50, 50, 50) // Erhöht den oberen Abstand um 10
  layout(button) = gbcButton

  listenTo(button)
  reactions += {
    case ButtonClicked(`button`) =>
      controller.handleAction(RestartGameAction())
  }
}

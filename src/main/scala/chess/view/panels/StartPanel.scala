package chess.view.panels

import scala.swing.*
import scala.swing.event.*
import chess.controller.*
import chess.controller.controller.StartGame

import javax.swing.ImageIcon
import java.awt.geom.Ellipse2D
import java.awt.{Color, Dimension, Insets}

// Benutzerdefinierte Button-Klasse für einen runden Button
class RoundButton(icon: ImageIcon) extends Button {
  // Setzen des Icons und der Größe des Buttons
  super.icon = icon
  maximumSize = new Dimension(50, 50)
  borderPainted = false // Unsichtbarer Rand

  // Methode zum Zeichnen des runden Buttons
  override def paintComponent(g: Graphics2D): Unit = {
    val diameter = Math.min(size.width, size.height) // Bestimmen des Durchmessers basierend auf der kleineren Dimension
    val x = (size.width - diameter) / 2
    val y = (size.height - diameter) / 2

    // Zeichnen des runden Buttons
    g.setColor(Color.WHITE)
    g.fill(new Ellipse2D.Double(x, y, diameter, diameter))

    // Zeichnen des Icons in der Mitte des Buttons
    if (icon != null) {
      val iconX = x + (diameter - icon.getIconWidth) / 2
      val iconY = y + (diameter - icon.getIconHeight) / 2
      icon.paintIcon(peer, g, iconX, iconY)
    }
  }
}

class StartPanel(controller: IController) extends GridBagPanel {

  val dimensionSize = 190 // Neue Größe für das Bild

  // Laden des Bildes und Skalieren auf die gewünschte Größe
  val ic = new ImageIcon(getClass.getResource("/buttons/start.png"))
  val scaledIcon = new ImageIcon(ic.getImage.getScaledInstance(dimensionSize, dimensionSize, java.awt.Image.SCALE_SMOOTH))

  // Erstellen des runden Buttons mit dem skalierten Icon
  val button = new RoundButton(scaledIcon)

  // Konfiguration für das Layout
  val gbc = new Constraints
  gbc.gridx = 0
  gbc.gridy = 0
  gbc.insets = new Insets(50, 50, 50, 50) // Abstand vom Rand des Panels

  // Hinzufügen des Buttons in die Mitte des GridBagPanels
  layout(button) = gbc

  // Event-Handler für den Button-Klick
  listenTo(button)
  reactions += {
    case ButtonClicked(`button`) => // Wenn der Start-Button geklickt wird
      controller.handleAction(StartGame())
    // Hier können Sie die Aktionen hinzufügen, die beim Klicken des Start-Buttons ausgeführt werden sollen
  }
}

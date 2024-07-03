package chess.controller.controller

import chess.controller.IController
import chess.module._
import chess.view._
import chess.view.view._
import com.google.inject.Guice

object EnvironmentUtil {
  def isHeadless: Boolean = {
    val headlessProperty = System.getProperty("java.awt.headless")
    headlessProperty != null && headlessProperty.equalsIgnoreCase("true")
  }
}
object Main extends App {
  private val injector = Guice.createInjector(new ChessModule)
  val controller = injector.getInstance(classOf[IController])
  if (!EnvironmentUtil.isHeadless) {
    val gui = GUI(controller)
    gui.top.open()
  } else {
    println("Running in headless mode, GUI will not be started.")
  }
  TUI(controller)
}

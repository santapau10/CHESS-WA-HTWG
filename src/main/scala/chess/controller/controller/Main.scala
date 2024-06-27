package chess.controller.controller

import chess.controller.IController
import chess.module._
import chess.view._
import chess.view.view._
import com.google.inject.Guice

object Main extends App {
  private val injector = Guice.createInjector(new ChessModule)
  val controller = injector.getInstance(classOf[IController])
  private val gui = GUI(controller)
  gui.top.open()
  TUI(controller)
}
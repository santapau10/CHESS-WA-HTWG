package chess.controller.controller

import chess.module._
import chess.view._
import chess.view.view.*

import com.google.inject.Guice

object Main extends App {
  // Create a Guice injector with ChessModule
  private val injector = Guice.createInjector(new ChessModule)

  // Retrieve instances of GUI and start the application
  val gui = injector.getInstance(classOf[GUI])
  gui.run()
}
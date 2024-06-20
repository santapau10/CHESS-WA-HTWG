package chess.module

import chess.controller.controller.*
import chess.controller.IController
import chess.view.*
import chess.view.view.{GUI, TUI}
import com.google.inject.AbstractModule

class ChessModule extends AbstractModule {
  override def configure(): Unit = {
    // Bind interfaces to concrete implementations
    bind(classOf[IController]).to(classOf[Controller])
    bind(classOf[IGUI]).to(classOf[GUI])

    // Example binding for boardSize (adjust as per your application's requirements)
    bind(classOf[Integer]).toInstance(8) // Example value, replace with your actual board size
  }
}

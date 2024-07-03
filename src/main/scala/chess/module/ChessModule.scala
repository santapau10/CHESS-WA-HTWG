package chess.module

import chess.controller.controller.Controller
import chess.controller.IController
import chess.view.*
import chess.view.view.*
import com.google.inject.AbstractModule

class ChessModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[IController]).to(classOf[Controller])
    bind(classOf[Integer]).toInstance(5)
  }
}
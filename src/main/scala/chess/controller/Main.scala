package chess.controller

import chess.view.TUI
import chess.models.*
import scala.language.postfixOps

object Main {
  def main(args: Array[String]): Unit = {
    val size = 11
    val controller: Controller = new Controller(size)
    controller.initGame()
  }
}
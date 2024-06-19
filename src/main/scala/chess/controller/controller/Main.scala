package chess.controller.controller

import chess.controller.IController
import chess.models.*
import chess.view.TUI

import scala.language.postfixOps

object Main {
  def main(args: Array[String]): Unit = {
    val size = 6
    val controller: IController = Controller(size)
    controller.initGame()
  }
}
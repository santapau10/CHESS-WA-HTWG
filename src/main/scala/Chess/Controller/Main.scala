package Chess.Controller

import Chess.View.*
import Chess.models.*
import scala.language.postfixOps

object Main {
  def main(args: Array[String]): Unit = {
    val size = 8
    val controller: Controller = new Controller(size)
    controller.initGame()
  }
}
package Chess.models

import Chess.View.*
import Chess.Controller.*

class Game(board: Board, list: List[Pieces], tui: TUI, controller: Controller) {
  private def toStringBoard(): String = {
    board.updateField(list)
  }
  def movePieces():Unit = {
    val coordinates = tui.readCoordinates()
    if (coordinates != null) {
      val (x1, y1, x2, y2) = coordinates
      val RList = board.movePieces(x1, y1, x2, y2, list)
      if (RList != null) {
        //println("succes")
        //printBoard(board, RList)
        controller.updateBoard(RList)
      } else {
        println("Ungültige Position!")
        controller.updateBoard(list)
      }
    }
  }
  override def toString: String = {
    toStringBoard()
  }
}

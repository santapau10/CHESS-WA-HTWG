package chess.models

import chess.view.*
import chess.controller.*

class Game(board: BoardBuilder, list: List[Pieces], tui: TUI, controller: Controller) {
  private def toStringBoard: String = {
    board.updateField(list)
  }
  def movePieces(l1: Int, n1: Int, l2: Int, n2: Int):Unit = {
    val RList = board.movePieces(l1, n1, l2, n2, list)
    if (RList != null) {
      controller.updateBoard(RList)
    } else {
      println("invalid position!")
      controller.updateBoard(list)
    }
  }
  override def toString: String = {
    toStringBoard
  }

  def getBoardList(): List[Pieces] = {
    list
  }
}

package chess.models.game

import chess.controller.*
import chess.models.{IBoardBuilder, IGame, IPieces}
import chess.view.*


class Game(board: IBoardBuilder, list: List[IPieces], tui: TUI, controller: IController) extends IGame {
  private def toStringBoard: String = {
    board.updateField(list)
  }
  override def movePieces(l1: Int, n1: Int, l2: Int, n2: Int):Unit = {
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

  override def getBoardList(): List[IPieces] = {
    list
  }
}
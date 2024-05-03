package Chess.Controller

import Chess.models.*
import Chess.Util.*
import Chess.View.TUI

case class Controller(size: Int) extends Observable:
  val b: Board = new Board(size)
  val tui: TUI = new TUI(this)
  var game: Game = new Game(new Board(size), b.setupBoard, tui, this)
  add(tui)
  def updateBoard(list: List[Pieces]): Unit =
    game = new Game(b, list, tui, this)
    notifyObservers(Event.BOARD_CHANGED)

  def boardToString(): String = {
    game.toString()
  }
  def movePieces(): Unit = {
    game.movePieces()
  }
  def initGame(): Unit = {
    notifyObservers(Event.BOARD_CHANGED)
  }
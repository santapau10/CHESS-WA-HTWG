package chess.controller

import chess.models.*
import chess.util.*
import chess.view.TUI

case class Controller(size: Int) extends Observable:
  val b: Board = new Board(size)
  var tui: TUI = new TUI(this)
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
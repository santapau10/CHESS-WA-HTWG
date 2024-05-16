package chess.controller

import chess.models.*
import chess.util.*
import chess.view.TUI

case class Controller(size: Int) extends Observable:
  val b: Board = new Board(size)
  var tui: TUI = new TUI(this)
  var game: Game = new Game(new Board(size), b.setupBoard, tui, this)
  var currentState: State = new GameState(this)
  add(tui)
  def updateBoard(list: List[Pieces]): Unit =
    game = new Game(b, list, tui, this)
    notifyObservers(Event.BOARD_CHANGED)

  def boardToString(): String = {
    game.toString()
  }
  def changeState(state: State): Unit = {
    currentState = state
  }
  def initGame(): Unit = {
    notifyObservers(Event.BOARD_CHANGED)
  }
  def handleAction(action: IAction): Unit = {
    action match {
      case MovePiecesAction(l1, n1, l2, n2) =>
        game.movePieces(l1, n1, l2, n2)
      case InputAction() =>
        notifyObservers(Event.COORDINATE_INPUT)
      case _ =>
    }
  }
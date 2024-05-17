package chess.controller

import chess.models.*
import chess.util.*
import chess.view.TUI

case class Controller(size: Int) extends Observable:
  val b: Board = new Board(size)
  val tui: TUI = new TUI(this)
  var game: Game = new Game(new Board(size), b.setupBoard, tui, this)
  private val undoManager = new UndoManager
  var currentState: State = GameState(this)
  def snapshot: Snapshot = Snapshot(game, currentState)
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
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
      case InputAction() =>
        notifyObservers(Event.COORDINATE_INPUT)
      case UndoAction() =>
        undoManager.undoCommand()
      case RedoAction() =>
        undoManager.redoCommand()
      case _ =>
    }
  }
  def restoreSnapshot(snapshot: Snapshot): Unit =
    game = snapshot.getGame()
    currentState = snapshot.getState()
    notifyObservers(Event.BOARD_CHANGED)

  def movePieces(l1: Int, n1: Int, l2: Int, n2: Int): Unit = {
    game.movePieces(l1, n1, l2, n2)
  }
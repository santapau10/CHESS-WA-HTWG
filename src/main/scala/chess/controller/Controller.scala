package chess.controller

import chess.models.*
import chess.util.*
import chess.view.TUI

case class Controller(size: Int) extends Observable:
  val b: BoardBuilder = if (size == 8) {
    new Board_equal_8(8)
  }
  else if (size < 8 && size > 0) {
    null
  }
  else if (size > 8) {
    null
  }
  else {
    throw new IllegalArgumentException("invalid size")
  }
  val tui: TUI = new TUI(this)
  var game: Game = new Game(b, b.getSetupBoard(), tui, this)
  private val undoManager = new UndoManager
  var currentState: State = PreGameState(this)
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
    notifyObservers(Event.STATE_CHANGED)
  }
  def initGame(): Unit = {
    notifyObservers(Event.STATE_CHANGED)
    tui.read
  }
  def handleAction(action: IAction): Unit = {
    action match {
      case MovePiecesAction(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
      case InputAction() =>
        notifyObservers(Event.INPUT)
      case UndoAction() =>
        undoManager.undoCommand()
      case RedoAction() =>
        undoManager.redoCommand()
      case StartGame() =>
        undoManager.executeCommand(ChangeStateCommand(GameState(this), this))
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
  def printState(): Unit ={
    currentState.print()
  }

  def actionFromInput(s: String): IAction = {
    currentState.actionFromInput(s)
  }
package chess.controller

import chess.models.*
import chess.util.*
import chess.view.*

import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class Controller(size: Int) extends Observable:
  val b: BoardBuilder = if (size == 8) {
    new Board_equal_8(8)
  }
  else if (size < 8 && size > 0) {
    new Board_smaller_8(size)
  }
  else if (size > 8) {
    new Board_bigger_8(size)
  }
  else {
    throw new IllegalArgumentException("invalid size")
  }
  val tui: TUI = new TUI(this)
  var game: Game = new Game(b, b.getSetupBoard(), tui, this)
  private val undoManager = new UndoManager
  var currentState: State = PreGameState(this)
  def snapshot: Snapshot = Snapshot(game, currentState)
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
    val gui = new GUI(this)
    gui.top.visible = true
    notifyObservers(Event.STATE_CHANGED)
    val tuiThread = new Thread(new Runnable {
      def run(): Unit = {
        tui.read
      }
    })
    tuiThread.start()
  }


  def handleAction(action: IAction): Unit = {
    action match {
      case MovePiecesWhite(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
        undoManager.executeCommand(ChangeStateCommand(TurnStateBlack(this), this))
      case MovePiecesBlack(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
        undoManager.executeCommand(ChangeStateCommand(TurnStateWhite(this), this))
      case InputAction() =>
        notifyObservers(Event.INPUT)
      case UndoAction() =>
        undoManager.undoCommand()
      case RedoAction() =>
        undoManager.redoCommand()
      case StartGame() =>
        undoManager.executeCommand(ChangeStateCommand(TurnStateWhite(this), this))
      case _ =>
    }
  }
  def restoreSnapshot(snapshot: Snapshot): Unit =
    game = snapshot.getGame()
    currentState = snapshot.getState()
    notifyObservers(Event.STATE_CHANGED)

  def movePieces(l1: Int, n1: Int, l2: Int, n2: Int): Unit = {
    game.movePieces(l1, n1, l2, n2)

  }
  def printState(): Unit ={
    currentState.print()
  }

  def actionFromInput(s: String): IAction = {
    currentState.actionFromInput(s)
  }

  def getGame(): Game = {
    game
  }
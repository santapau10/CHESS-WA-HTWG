package chess.controller.controller

import chess.controller.*
import chess.models.game.Game
import chess.models.*
import chess.util.*
import chess.view.*
import chess.view.view.{GUI, TUI}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.google.inject.Inject


case class Controller @Inject() (size: Int) extends IController with Observable:
  private val b: IBoardBuilder = size match {
    case 8 => new Board_equal_8(8)
    case s if s < 8 && s > 0 => new Board_smaller_8(s)
    case s if s > 8 => new Board_bigger_8(s)
    case _ => throw new IllegalArgumentException("invalid size")
  }


  var game: IGame = new Game(b, b.getSetupBoard, this)
  private val undoManager: IUndoManager = new UndoManager
  private var currentState: IState = PreGameState(this)

  override def snapshot: ISnapshot = Snapshot(game, currentState)

  override def getSize: Int = {
    this.size
  }

  override def updateBoard(list: List[IPieces]): Unit =
    game = new Game(b, list, this)

  override def boardToString(): String = {
    game.toString
  }

  override def changeState(state: IState): Unit = {
    currentState = state
    notifyObservers(Event.STATE_CHANGED)
  }

  override def initGame(): Unit = {
    notifyObservers(Event.STATE_CHANGED)
  }

  override def handleAction(action: IAction): Unit = {
    action match {
      case MovePiecesWhite(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
        undoManager.executeCommand(ChangeStateCommand(TurnStateBlack(this), this))
      case MovePiecesBlack(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
        undoManager.executeCommand(ChangeStateCommand(controller.TurnStateWhite(this), this))
      case InputAction() =>
        notifyObservers(Event.INPUT)
      case UndoAction() =>
        getCurrentState match {
          case _: TurnStateWhite | _: TurnStateBlack | _: MovePieceWhite | _: MovePieceBlack =>
            undoManager.undoCommand()
          case _ =>
        }
        undoManager.undoCommand()
      case RedoAction() =>
        getCurrentState match {
          case _: TurnStateWhite | _: TurnStateBlack | _: MovePieceWhite | _: MovePieceBlack =>
            undoManager.redoCommand()
          case _ =>
        }
        undoManager.redoCommand()
      case StartGame() =>
        undoManager.executeCommand(ChangeStateCommand(controller.TurnStateWhite(this), this))
      case StartMovePiecesBlack(column1, row1) =>
        changeState(MovePieceBlack(this, column1, row1))
      case StartMovePiecesWhite(column1, row1) =>
        changeState(MovePieceWhite(this, column1, row1))
      case CancelMoveWhite() =>
        changeState(TurnStateWhite(this))
      case CancelMoveBlack() =>
        changeState(TurnStateBlack(this))
      case _ =>
    }
  }

  override def restoreSnapshot(snapshot: ISnapshot): Unit =
    game = snapshot.getGame
    currentState = snapshot.getState
    notifyObservers(Event.STATE_CHANGED)

  override def movePieces(l1: Int, n1: Int, l2: Int, n2: Int): Unit = {
    game.movePieces(l1, n1, l2, n2)
  }

  override def printState(): Unit = {
    currentState.print()
  }

  override def actionFromInput(s: String): IAction = {
    currentState.actionFromInput(s)
  }

  override def getGame: IGame = {
    game
  }

  override def add(s: Observer): Unit = {
    super.add(s)
  }

  override def getCurrentState: IState = {
    this.currentState
  }
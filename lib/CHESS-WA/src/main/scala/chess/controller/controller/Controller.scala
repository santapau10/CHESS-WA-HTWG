package chess.controller.controller

import chess.controller.*
import chess.models.game.{Chesspiece, Colors, Game, Rook}
import chess.models.*
import chess.util.*
import chess.util.Event.STATE_CHANGED
import chess.view.*
import chess.view.view.{GUI, TUI}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.google.inject.Inject

import java.nio.file.{Files, Paths}
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.xml.{Elem, PrettyPrinter, Utility, XML}
import play.api.libs.json.{JsValue, Json}

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

case class Controller @Inject() (size: Int) extends IController with Observable:
  private val b: IBoardBuilder = size match {
    case 8                   => new Board_equal_8(8)
    case s if s < 8 && s > 0 => new Board_smaller_8(s)
    case s if s > 8          => new Board_bigger_8(s)
    case _ => throw new IllegalArgumentException("invalid size")
  }

  var game: IGame = new Game(b, b.getSetupBoard)
  private val undoManager: IUndoManager = new UndoManager
  private var currentState: IState = PreGameState(this)

  override def snapshot: ISnapshot = Snapshot(game, currentState)

  override def getSize: Int = {
    this.size
  }

  override def updateBoard(list: List[IPieces]): Unit =
    game = new Game(b, list)

  override def boardToString(): String = {
    game.toString
  }
  override def boardToStringOnline(): String = {
    game.toStringOnline
  }

  override def notifyObservers(event: Event): Unit =
    super.notifyObservers(event)
  override def changeState(state: IState): Unit = {
    currentState = state
    notifyObservers(Event.STATE_CHANGED)
  }
  def changeStateWithoutGUIChange(state: IState): Unit = {
    currentState = state
    notifyObservers(Event.UPDATE_TUI)
  }

  override def initGame(): Unit = {
    game = new Game(b, b.getSetupBoard)
    notifyObservers(Event.STATE_CHANGED)
  }

  override def handleAction(action: IAction): Unit = {
    action match {
      case MovePiecesWhite(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
        if (
          game.getBoardList.last.getPiece == Chesspiece.PAWN && game.getBoardList.last.getCords._2 == size - 1
        ) {
          undoManager.executeCommand(
            ChangeStateCommand(PromotionState(this), this)
          )
        } else if (game.isKingInCheckmate(game.getBoardList, Colors.BLACK)) {
          changeState(GameOver(this))
        } else {
          getCurrentState match {
            case _: MovePieceWhite =>
              handleAction(CancelMoveWhite())
              undoManager.executeCommand(
                ChangeStateCommand(TurnStateBlack(this), this)
              )
            case _: MovePieceBlack =>
              handleAction(CancelMoveBlack())
              undoManager.executeCommand(
                ChangeStateCommand(TurnStateWhite(this), this)
              )
            case _ =>
          }
        }
      case MovePiecesBlack(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
        if (
          game.getBoardList.last.getPiece == Chesspiece.PAWN && game.getBoardList.last.getCords._2 == 0
        ) {
          undoManager.executeCommand(
            ChangeStateCommand(PromotionState(this), this)
          )
        } else if (game.isKingInCheckmate(game.getBoardList, Colors.WHITE)) {
          changeState(GameOver(this))
        } else {
          getCurrentState match {
            case _: MovePieceWhite =>
              undoManager.executeCommand(
                ChangeStateCommand(TurnStateBlack(this), this)
              )
            case _: MovePieceBlack =>
              undoManager.executeCommand(
                ChangeStateCommand(TurnStateWhite(this), this)
              )
            case _ =>
          }
        }
      case InputAction() =>
        notifyObservers(Event.INPUT)
      case UndoAction() =>
        if (undoManager.canUndo) {
          getCurrentState match {
            case _: TurnStateWhite | _: MovePieceWhite | _: TurnStateBlack |
                _: MovePieceBlack =>
              undoManager.undoCommand()
              undoManager.undoCommand()
              getCurrentState match
                case _: MovePieceWhite =>
                  handleAction(CancelMoveWhite())
                case _: MovePieceBlack =>
                  handleAction(CancelMoveBlack())
                case _ =>
            case _ =>
              undoManager.undoCommand()
          }
        }
      case RestartGameAction() =>
        undoManager.executeCommand(RestartCommand(this))

      case PromoteToBishopAction() =>
        undoManager.executeCommand(PromotionCommand(Chesspiece.BISHOP, this))
      case PromoteToQueenAction() =>
        undoManager.executeCommand(PromotionCommand(Chesspiece.QUEEN, this))
      case PromoteToRookAction() =>
        undoManager.executeCommand(PromotionCommand(Chesspiece.ROOK, this))
      case PromoteToKnightAction() =>
        undoManager.executeCommand(PromotionCommand(Chesspiece.KNIGHT, this))

      case RedoAction() =>
        if (undoManager.canRedo) {
          getCurrentState match {
            case _: TurnStateWhite | _: MovePieceWhite =>
              undoManager.redoCommand()
              undoManager.redoCommand()

            case _: TurnStateBlack | _: MovePieceBlack =>
              undoManager.redoCommand()
              undoManager.redoCommand()
            case _ =>
              undoManager.redoCommand()
          }
        }

        notifyObservers(STATE_CHANGED)
      case StartGame() =>
        undoManager.executeCommand(
          ChangeStateCommand(controller.TurnStateWhite(this), this)
        )
      case StartMovePiecesBlack(column1, row1) =>
        changeStateWithoutGUIChange(MovePieceBlack(this, column1, row1))
      case StartMovePiecesWhite(column1, row1) =>
        changeStateWithoutGUIChange(MovePieceWhite(this, column1, row1))
      case CancelMoveWhite() =>
        changeState(TurnStateWhite(this))
      case CancelMoveBlack() =>
        changeState(TurnStateBlack(this))
      case LoadXmlAction(node) =>
        undoManager.executeCommand(LoadXmlCommand(this, node))
      case LoadJsonAction(json) =>
        undoManager.executeCommand(LoadJsonCommand(this, json))
      case _ =>
    }
  }

  override def save(): Unit =
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
    val date = formatter.format(LocalDateTime.now)
    val name = s"progress_$date"
    saveXml(name)
    saveJson(name)

  private def saveXml(name: String): Unit =
    val xmlSnapshot = snapshot.toXml
    val hash =
      Snapshot.hash(Utility.trim(XML.loadString(xmlSnapshot.toString)).toString)
    // println(hash)
    val xml =
      <progress>
        <hash>
          {hash}
        </hash>{xmlSnapshot}
      </progress>
    XML.save(
      name + ".xml",
      XML.loadString(PrettyPrinter(100, 2).format(xml)),
      "UTF-8",
      true,
      null
    )

  private def saveJson(name: String): Unit =
    val jsonSnapshot = snapshot.toJson
    val hash = Snapshot.hash(Json.stringify(jsonSnapshot))
    val json = Json.obj(
      "hash" -> hash,
      "snapshot" -> jsonSnapshot
    )
    Files.write(
      Paths.get(name + ".json"),
      Json.prettyPrint(json).getBytes(StandardCharsets.UTF_8)
    )

  override def restoreSnapshot(snapshot: ISnapshot): Unit =
    game = snapshot.getGame
    currentState = snapshot.getState
    notifyObservers(Event.STATE_CHANGED)

  override def movePieces(l1: Int, n1: Int, l2: Int, n2: Int): Unit = {
    val RList = game.getBoard.movePieces(l1, n1, l2, n2, game.getBoardList)
    if (RList != null) {
      updateBoard(RList)
    } else {
      println("invalid position!")
      updateBoard(game.getBoardList)
    }
  }

  override def enPassantPiece(
      l1: Int,
      n1: Int,
      l2: Int,
      n2: Int,
      defeated_x: Int,
      defeated_y: Int
  ): Unit = {
    val RList = game.getBoard
      .movePieces(l1, n1, l2, n2, game.getBoardList)
      .filterNot(p => p.getCords == (defeated_x, defeated_y))
    if (RList != null) {
      updateBoard(RList)
    } else {
      println("invalid position!")
      updateBoard(game.getBoardList)
    }
  }

  override def longCastling(
      l1: Int,
      n1: Int,
      l2: Int,
      n2: Int,
      rook_x1: Int,
      rook_y1: Int,
      rook_x2: Int
  ): Unit = {
    // Move the king to the new position
    val kingMovedList =
      game.getBoard.movePieces(l1, n1, l2, n2, game.getBoardList)

    // Move the rook to the new position
    val RList = kingMovedList.map { p =>
      if (p.getCords == (rook_x1, rook_y1))
        new Rook(
          (rook_x2, rook_y1),
          p.getColor,
          moved = true,
          (rook_x1, rook_y1)
        )
      else p
    }

    if (RList != null) {
      updateBoard(RList)
    } else {
      println("invalid position!")
      updateBoard(game.getBoardList)
    }
  }

  override def shortCastling(
      l1: Int,
      n1: Int,
      l2: Int,
      n2: Int,
      rook_x1: Int,
      rook_y1: Int,
      rook_x2: Int
  ): Unit = {
    // Move the king to the new position
    val kingMovedList =
      game.getBoard.movePieces(l1, n1, l2, n2, game.getBoardList)

    // Move the rook to the new position
    val RList = kingMovedList.map { p =>
      if (p.getCords == (rook_x1, rook_y1))
        new Rook(
          (rook_x2, rook_y1),
          p.getColor,
          moved = true,
          (rook_x1, rook_y1)
        )
      else p
    }

    if (RList != null) {
      updateBoard(RList)
    } else {
      println("invalid position!")
      updateBoard(game.getBoardList)
    }
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

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
    case 8 => new Board_equal_8(8)
    case s if s < 8 && s > 0 => new Board_smaller_8(s)
    case s if s > 8 => new Board_bigger_8(s)
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

  override def notifyObservers(event: Event): Unit = super.notifyObservers(event)
  override def changeState(state: IState): Unit = {
    currentState = state
    notifyObservers(Event.STATE_CHANGED)
  }
  def changeStateWithoutGUIChange(state: IState): Unit = {
    currentState = state
    notifyObservers(Event.UPDATE_TUI)
  }

  override def initGame(): Unit = {
    notifyObservers(Event.STATE_CHANGED)
  }

  override def handleAction(action: IAction): Unit = {
    action match {
      case MovePiecesWhite(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
      case MovePiecesBlack(l1, n1, l2, n2) =>
        undoManager.executeCommand(MovePiecesCommand(this, l1, n1, l2, n2))
      case InputAction() =>
        notifyObservers(Event.INPUT)
      case UndoAction() =>
        getCurrentState match {
          case _: TurnStateWhite | _: MovePieceWhite =>
            undoManager.undoCommand()
            undoManager.undoCommand()
            handleAction(CancelMoveBlack())
          case _: TurnStateBlack | _: MovePieceBlack =>
            undoManager.undoCommand()
            undoManager.undoCommand()
            handleAction(CancelMoveWhite())
          case _ =>
            undoManager.undoCommand()
        }

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
        changeStateWithoutGUIChange(MovePieceBlack(this, column1, row1))
      case StartMovePiecesWhite(column1, row1) =>
        changeStateWithoutGUIChange(MovePieceWhite(this, column1, row1))
      case CancelMoveWhite() =>
        changeState(TurnStateWhite(this))
      case CancelMoveBlack() =>
        changeState(TurnStateBlack(this))
      case LoadXmlAction(node) => undoManager.executeCommand(LoadXmlCommand(this, node))
      case LoadJsonAction(json) => undoManager.executeCommand(LoadJsonCommand(this, json))  
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
    val hash = Snapshot.hash(Utility.trim(XML.loadString(xmlSnapshot.toString)).toString)
    //println(hash)
    val xml =
      <progress>
        <hash>
          {hash}
        </hash>{xmlSnapshot}
      </progress>
    XML.save(name + ".xml", XML.loadString(PrettyPrinter(100, 2).format(xml)), "UTF-8", true, null)

  private def saveJson(name: String): Unit =
    val jsonSnapshot = snapshot.toJson
    val hash = Snapshot.hash(Json.stringify(jsonSnapshot))
    val json = Json.obj(
      "hash" -> hash,
      "snapshot" -> jsonSnapshot
    )
    Files.write(Paths.get(name + ".json"), Json.prettyPrint(json).getBytes(StandardCharsets.UTF_8))


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
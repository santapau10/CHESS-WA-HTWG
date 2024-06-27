package chess.controller

import chess.models.{IGame, IPieces}
import chess.util.{Event, Observer}


import play.api.libs.json.{JsValue, Json}
import java.security.MessageDigest
import scala.xml.{Elem, Node}

trait IController:

  def save(): Unit
  def getSize: Int
  def add(s: Observer): Unit
  def notifyObservers(event: Event): Unit
  def snapshot: ISnapshot
  def updateBoard(list: List[IPieces]): Unit
  def boardToString(): String
  def changeState(state: IState): Unit
  def initGame(): Unit
  def handleAction(action: IAction): Unit
  def restoreSnapshot(snapshot: ISnapshot): Unit
  def movePieces(l1: Int, n1: Int, l2: Int, n2: Int): Unit
  def printState(): Unit
  def actionFromInput(s: String): IAction
  def getGame: IGame
  def getCurrentState: IState

trait IAction

trait ICommand:

  def execute(): Unit = ()
  def saveSnapshot(): Unit
  def undo(): Unit

trait IState(controller: IController):
  def toXml: Elem
  def toJson: JsValue
  def getRow: Int
  def getColumn: Int
  def print(): Unit
  def actionFromInput(input: String): IAction
  def message: String

trait ISnapshot:

  def toXml: Elem
  def toJson: JsValue
  def getState: IState
  def getGame: IGame

trait IUndoManager:

  def executeCommand(command: ICommand): Unit
  def canUndo: Boolean
  def undoCommand(): Unit
  def canRedo: Boolean
  def redoCommand(): Unit
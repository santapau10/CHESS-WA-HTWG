package chess.controller

import chess.models.{IGame, IPieces}
import chess.util.Observer

trait IController:

  def add(s: Observer): Unit
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

    def print(): Unit
    def actionFromInput(input: String): IAction
    def message: String

trait ISnapshot:

  def getState(): IState
  def getGame(): IGame

trait IUndoManager:

  def executeCommand(command: ICommand): Unit
  def canUndo: Boolean
  def undoCommand(): Unit
  def canRedo: Boolean
  def redoCommand(): Unit
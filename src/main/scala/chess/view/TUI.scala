package chess.view

import chess.util.*
import chess.controller.*
import chess.controller.controller.InvalidAction
import chess.models.IPieces

import scala.io.StdIn
import scala.util.matching.Regex

class TUI(controller: IController) extends Observer {
  controller.add(this)
  override def update(event: Event): Unit = {
    event match {
      case Event.BOARD_CHANGED => println(controller.boardToString())
        //notifyObservers(Event.INPUT)
        controller.printState()
      case Event.INPUT =>
        read()
      case Event.STATE_CHANGED =>
        controller.printState()
      case _ =>
    }
  }
  def read(): Unit = {
    controller.handleAction(actionFromInput)
    read()
  }
  def actionFromInput: IAction ={
    controller.actionFromInput(StdIn.readLine().toLowerCase) match
      case InvalidAction(msg) =>
        println(msg)
        actionFromInput
      case action =>
        action
  }
}
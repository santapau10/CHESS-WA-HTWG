package chess.view.view

import chess.controller.*
import chess.models.IPieces
import chess.view.ITUI
import chess.util.*

import scala.io.StdIn
import scala.util.matching.Regex
import com.google.inject.Inject


class TUI @Inject() (controller: IController) extends Observer with ITUI {
  controller.add(this)
  println("Welcome to CHESS!")
  controller.printState()
  read()
  override def update(event: Event): Unit = {
    event match {
      case Event.BOARD_CHANGED => println(controller.boardToString())
        //notifyObservers(Event.INPUT)
        controller.printState()
      case Event.INPUT =>
        read()
      case Event.STATE_CHANGED =>
        controller.printState()
      case Event.UPDATE_TUI =>
        controller.printState()
      case _ =>
    }
  }
  override def read(): Unit = {
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
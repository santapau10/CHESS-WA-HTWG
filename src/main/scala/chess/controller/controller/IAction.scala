package chess.controller.controller

import chess.controller.IAction

case class UpdateBoardAction() extends IAction

case class NoAction() extends IAction

case class InputAction() extends IAction

case class UndoAction() extends  IAction

case class RedoAction() extends  IAction

case class SaveSnapshot() extends  IAction

case class StartGame() extends IAction

case class InvalidAction(msg: String) extends IAction

case class MovePiecesWhite(letter1: Int, number1: Int, letter2: Int, number2: Int) extends IAction

case class MovePiecesBlack(letter1: Int, number1: Int, letter2: Int, number2: Int) extends IAction
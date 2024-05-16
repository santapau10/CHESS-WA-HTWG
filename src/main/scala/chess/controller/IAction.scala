package chess.controller

trait IAction



case class UpdateBoardAction() extends IAction

case class MovePiecesAction(letter1: Int, number1: Int, letter2: Int, number2: Int) extends IAction

case class NoAction() extends IAction

case class InputAction() extends IAction

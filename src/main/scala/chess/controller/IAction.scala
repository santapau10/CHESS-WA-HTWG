package chess.controller
import play.api.libs.json.JsValue

import scala.xml.Node

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

case class StartMovePiecesBlack(column1: Int, row1: Int) extends IAction

case class StartMovePiecesWhite(column1: Int, row1: Int) extends IAction

case class CancelMoveWhite() extends IAction

case class CancelMoveBlack() extends IAction

case class LoadXmlAction(node: Node) extends IAction

case class LoadJsonAction(json: JsValue) extends IAction

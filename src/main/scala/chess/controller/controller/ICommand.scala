package chess.controller.controller

import chess.controller.{controller, *}
import chess.models.game.{Chesspiece, Colors, PiecesFactory}
import chess.models.IPiecesFactory
import play.api.libs.json.{JsValue, Json}

import scala.xml.{Node, Utility}

class Command(controller: IController) extends ICommand:
  private var snapshot: Option[ISnapshot] = None

  override def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)

  override def undo(): Unit = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  override def execute(): Unit = ()

class MovePiecesCommand(controller: IController, l1: Int, n1: Int, l2: Int, n2: Int) extends Command(controller) {
  override def execute(): Unit = {
    val mPiece = controller.getGame.getBoardList.find(p => p.getCords._1 == l1 && p.getCords._2 == n1)
    val updatedList = controller.getGame.getBoardList.filterNot(p => p.getCords == mPiece.get.getCords).filterNot(p => p.getCords == (l2, n2)) :+ PiecesFactory().addPiece(mPiece.get.getPiece, (l2, n2), mPiece.get.getColor, mPiece.get.isMoved, (l1, n1))
    mPiece match {
      case Some(p) =>
        if (p.checkMove(l1, n1, l2, n2, controller.getGame.getBoardList) && !controller.getGame.isKingInCheck(updatedList, p.getColor)) {
          // Prüfen, ob der Zug ein enPassant Zug war
          if (p.getPiece == Chesspiece.PAWN && (l2 == p.getCords._1 + 1 || l2 == p.getCords._1 - 1)
            && !controller.getGame.getBoardList.exists(p => p.getCords == (l2, n2))) {
            if (p.getColor == Colors.WHITE) {
              controller.enPassantPiece(l1, n1, l2, n2, l2, n2 - 1)
            } else {
              controller.enPassantPiece(l1, n1, l2, n2, l2, n2 + 1)
            }
          }
          // Prüfen, ob der Zug ein Short Castling Zug war
          else if (p.getPiece == Chesspiece.KING && math.abs(l2 - l1) == 2 && l2 > l1) {
            // Short Castling Bedingungen prüfen
            val rookX1 = 7
            val rookY1 = n1
            val rookX2 = l2 - 1
            if (controller.getGame.getBoardList.exists(r => r.getCords == (rookX1, rookY1) && r.getPiece == Chesspiece.ROOK && !r.isMoved)) {
              controller.shortCastling(l1, n1, l2, n2, rookX1, rookY1, rookX2)
            }
          }
          // Prüfen, ob der Zug ein Long Castling Zug war
          else if (p.getPiece == Chesspiece.KING && math.abs(l2 - l1) == 2 && l2 < l1) {
            // Long Castling Bedingungen prüfen
            val rookX1 = 0
            val rookY1 = n1
            val rookX2 = l2 + 1
            if (controller.getGame.getBoardList.exists(r => r.getCords == (rookX1, rookY1) && r.getPiece == Chesspiece.ROOK && !r.isMoved)) {
              controller.longCastling(l1, n1, l2, n2, rookX1, rookY1, rookX2)
            }
          } else {
            // falls nicht, einfach normal bewegen
            controller.movePieces(l1, n1, l2, n2)
          }
        } else if (p.getColor == Colors.WHITE) {
          controller.handleAction(CancelMoveWhite())
        } else if (p.getColor == Colors.BLACK) {
          controller.handleAction(CancelMoveBlack())
        } else {
          throw IllegalStateException("invalid Color")
        }
      case None =>
    }
  }
}


class ChangeStateCommand(state: IState, c: IController) extends Command(c):
  override def execute(): Unit = {
    c.changeState(state)
  }

case class LoadXmlCommand(controller: IController, node: Node) extends Command(controller):

  override def execute(): Unit =
    val hash = (node \ "hash").text
    val xmlSnapshot = (node \ "snapshot").head
    if hash == " " + Snapshot.hash(Utility.trim(xmlSnapshot).toString) + " " then // neu = alt -> ladet
      controller.restoreSnapshot(Snapshot.fromXml(xmlSnapshot, controller))
    else
      println(hash)
      println(Snapshot.hash(Utility.trim(xmlSnapshot).toString))
      println("Invalid XML progress file!")

case class LoadJsonCommand(controller: IController, json: JsValue) extends Command(controller):

  override def execute(): Unit =
    val hash = (json \ "hash").get.as[String]
    val jsonSnapshot = (json \ "snapshot").get
    if hash == Snapshot.hash(Json.stringify(jsonSnapshot)) then
      controller.restoreSnapshot(Snapshot.fromJson(jsonSnapshot, controller))
    else
      println("Invalid JSON progress file!")

class RestartCommand(c: IController) extends Command(c):
  override def execute(): Unit = {
    c.initGame()
    c.changeState(TurnStateWhite(c))
  }
class PromotionCommand(chesspiece: Chesspiece, c: IController) extends Command(c):
  override def execute(): Unit = {
    c.updateBoard(c.getGame.getBoard.promotePiece(chesspiece, c.getGame.getBoardList))
    if(c.getGame.getBoardList.last.getColor == Colors.WHITE) c.changeState(TurnStateBlack(c)) else c.changeState(TurnStateWhite(c))
  }
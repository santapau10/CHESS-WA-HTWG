package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}



class Pawn(cords: (Int, Int), color: Colors, moved: Boolean, last_cords: (Int,Int)) extends IPieces {

  override def isMoved: Boolean = moved
  override def getLastCords: (Int, Int) = last_cords
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.PAWN
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♙" else "♟"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Pawn.png" else "/white/Pawn.png"

  override def toXml: Elem = {
    <pawn>
      <cords>
        <x>
          {cords._1}
        </x>
        <y>
          {cords._2}
        </y>
      </cords>
      <color>
        {color.toString}
      </color>
      <moved>
        {moved.toString}
      </moved>
      <lastcords>
        <x>
          {last_cords._1}
        </x>
        <y>
          {last_cords._2}
        </y>
      </lastcords>
    </pawn>
  }

  override def toJson: JsValue = {
    val baseJson = Json.obj(
      "cords" -> Json.obj(
        "x" -> cords._1,
        "y" -> cords._2
      ),
      "color" -> color.toString,
      "moved" -> moved.toString,
      "piece" -> getPiece.toString,
      "lastcords" -> Json.obj(
        "x" -> last_cords._1,
        "y" -> last_cords._2
      )
    )
    baseJson
  }

  def checkMove(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): Boolean = {
    val piece = list.find(p => p.getCords == (x1, y1))

    if (piece.isEmpty) return false

    val color = piece.get.getColor
    val isWhite = color == Colors.WHITE
    val direction = if (isWhite) 1 else -1

    val isForwardMove = (isWhite && y2 > y1) || (!isWhite && y2 < y1)
    if (!isForwardMove) return false

    val oneStepForward = x1 == x2 && y2 == y1 + direction && list.forall(p => p.getCords != (x2, y2))

    if (x1 == x2 && !isMoved && y2 == y1 + 2 * direction && list.forall(p => p.getCords != (x2, y2) && p.getCords != (x2, y1 + direction))) {
      return true
    }

    val captureMove = math.abs(x2 - x1) == 1 && y2 == y1 + direction && list.exists(p => p.getCords == (x2, y2) && p.getColor != color)
    val enPassantPiece: IPieces = if (list.last.getCords == (x1 + 1, y1) || list.last.getCords == (x1 - 1, y1)) list.last else null

    enPassantPiece match
      case p: IPieces =>
        if (isWhite && p.getColor == Colors.BLACK && p.getLastCords == (p.getCords._1, p.getCords._2 + 2) && x2 == p.getCords._1 && y2 == p.getCords._2 + 1) {
          return true
        } else if (!isWhite && p.getColor == Colors.WHITE && p.getLastCords == (p.getCords._1, p.getCords._2 - 2) && x2 == p.getCords._1 && y2 == p.getCords._2 - 1) {
          return true
        }

      case null =>
    if (oneStepForward || captureMove) {
      return true
    }
    false
  }
}

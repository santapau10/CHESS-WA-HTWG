package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}



class King(cords: (Int, Int), color: Colors, moved: Boolean, last_cords: (Int,Int)) extends IPieces {
  override def getLastCords: (Int, Int) = last_cords
  override def isMoved: Boolean = moved
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.KING
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♔" else "♚"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/King.png" else "/white/King.png"

  override def toXml: Elem = {
    <king>
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
    </king>
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
    val isValidKingMove = math.abs(x2 - x1) <= 1 && math.abs(y2 - y1) <= 1

    if (!isValidKingMove) return false

    val targetSquare = list.find(p => p.getCords == (x2, y2))
    val targetSquareValid = targetSquare.forall(_.getColor != list.find(p => p.getCords == (x1, y1)).get.getColor)

    if (!targetSquareValid) return false

    val updatedList = list.filterNot(p => p.getCords == (x1, y1)).filterNot(p => p.getCords == (x2, y2)) :+ new King((x2, y2), list.find(p => p.getCords == (x1, y1)).get.getColor, list.find(p => p.getCords == (x1, y1)).get.isMoved, (x1,x2))

    !isKingInCheck(x2, y2, updatedList)
  }

  def isKingInCheck(kingX: Int, kingY: Int, list: List[IPieces]): Boolean = {
    list.exists(p => p.getColor != list.find(k => k.getCords == (kingX, kingY)).get.getColor && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, list))
  }
}

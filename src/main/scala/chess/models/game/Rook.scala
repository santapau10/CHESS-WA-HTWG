package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}



class Rook(cords: (Int, Int), color: Colors, moved: Boolean, last_cords: (Int,Int)) extends IPieces {
  override def getLastCords: (Int, Int) = last_cords
  override def isMoved: Boolean = moved
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.ROOK
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♖" else "♜"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Rook.png" else "/white/Rook.png"

  override def toXml: Elem = {
    <rook>
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
    </rook>
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
    val isHorizontalMove = y1 == y2
    val isVerticalMove = x1 == x2

    if (isHorizontalMove || isVerticalMove) {
      val xStep = if (x2 > x1) 1 else if (x2 < x1) -1 else 0
      val yStep = if (y2 > y1) 1 else if (y2 < y1) -1 else 0

      val positionsInPath = for (i <- 1 until math.max(math.abs(x2 - x1), math.abs(y2 - y1)))
        yield (x1 + i * xStep, y1 + i * yStep)

      val pathClear = positionsInPath.forall { case (x, y) =>
        !list.exists(p => p.getCords == (x, y))
      }

      val targetSquare = list.find(p => p.getCords == (x2, y2))
      val targetSquareValid = targetSquare.forall(_.getColor != list.find(p => p.getCords == (x1, y1)).get.getColor)

      pathClear && targetSquareValid
    } else {
      false
    }
  }

}

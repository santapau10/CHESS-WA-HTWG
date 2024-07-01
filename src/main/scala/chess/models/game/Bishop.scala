package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}



class Bishop(cords: (Int, Int), color: Colors, moved: Boolean) extends IPieces {
  override def isMoved: Boolean = moved
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.BISHOP
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♗" else "♝"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Bishop.png" else "/white/Bishop.png"

  override def toXml: Elem = {
    <bishop>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
      <moved>{moved.toString}</moved>
    </bishop>
  }

  override def toJson: JsValue = {
    val baseJson = Json.obj(
      "cords" -> Json.obj(
        "x" -> cords._1,
        "y" -> cords._2
      ),
      "color" -> color.toString,
      "moved" -> moved.toString,
      "piece" -> getPiece.toString
    )
    baseJson
  }

  def checkMove(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): Boolean = {
    if (math.abs(x2 - x1) == math.abs(y2 - y1)) {
      val xStep = if (x2 > x1) 1 else -1
      val yStep = if (y2 > y1) 1 else -1

      val positionsInPath = for (i <- 1 until math.abs(x2 - x1)) yield (x1 + i * xStep, y1 + i * yStep)

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

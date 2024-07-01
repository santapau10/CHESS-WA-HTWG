package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}



class Knight(cords: (Int, Int), color: Colors, moved: Boolean) extends IPieces {
  override def isMoved: Boolean = moved
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.KNIGHT
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♘" else "♞"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Knight.png" else "/white/Knight.png"

  override def toXml: Elem = {
    <knight>
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
    </knight>
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
    val knightMoves = List((2, 1), (2, -1), (-2, 1), (-2, -1), (1, 2), (1, -2), (-1, 2), (-1, -2))

    val isValidKnightMove = knightMoves.contains((x2 - x1, y2 - y1))

    if (isValidKnightMove) {
      val targetSquare = list.find(p => p.getCords == (x2, y2))
      val targetSquareValid = targetSquare.forall(_.getColor != list.find(p => p.getCords == (x1, y1)).get.getColor)

      targetSquareValid
    } else {
      false
    }
  }

}

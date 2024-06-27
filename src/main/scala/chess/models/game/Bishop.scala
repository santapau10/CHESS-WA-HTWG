package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}



class Bishop(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.BISHOP
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♗" else "♝"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Bishop.png" else "/white/Bishop.png"

  def toXml: Elem = {
    <bishop>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
    </bishop>
  }

  def toJson: JsValue = {
    val baseJson = Json.obj(
      "cords" -> Json.obj(
        "x" -> cords._1,
        "y" -> cords._2
      ),
      "color" -> color.toString,
      "piece" -> getPiece.toString
    )
    baseJson
  }
}

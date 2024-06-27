package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

object Bishop {
  implicit val writes: Writes[Bishop] = Json.writes[Bishop]
  implicit val reads: Reads[Bishop] = Json.reads[Bishop]

  def fromXML(node: Node): Bishop = {
    val x = (node \ "cords" \ "x").text.toInt
    val y = (node \ "cords" \ "y").text.toInt
    val color = Colors.withName((node \ "color").text)
    new Bishop((x, y), color)
  }

  def fromJSON(json: JsValue): Bishop = {
    json.as[Bishop]
  }
}

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

  def toJson: JsValue = Json.toJson(this)
}

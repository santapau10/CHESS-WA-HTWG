package chess.models.game

import chess.models.{IPieces, Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

// JSON serializers and deserializers using Play JSON
object Queen {
  implicit val writes: Writes[Queen] = Json.writes[Queen]
  implicit val reads: Reads[Queen] = Json.reads[Queen]

  def fromXML(node: Node): Queen = {
    val x = (node \ "cords" \ "x").text.toInt
    val y = (node \ "cords" \ "y").text.toInt
    val color = Colors.withName((node \ "color").text)
    new Queen((x, y), color)
  }

  def fromJSON(json: JsValue): Queen = {
    json.as[Queen]
  }
}

case class Queen(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.QUEEN
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♕" else "♛"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Queen.png" else "/white/Queen.png"

  def toXml: Elem = {
    <queen>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
    </queen>
  }

  def toJson: JsValue = Json.toJson(this)
}

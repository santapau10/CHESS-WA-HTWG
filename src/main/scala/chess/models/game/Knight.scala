package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

object Knight {
  implicit val writes: Writes[Knight] = Json.writes[Knight]
  implicit val reads: Reads[Knight] = Json.reads[Knight]

  def fromXML(node: Node): Knight = {
    val x = (node \ "cords" \ "x").text.toInt
    val y = (node \ "cords" \ "y").text.toInt
    val color = Colors.withName((node \ "color").text)
    new Knight((x, y), color)
  }

  def fromJSON(json: JsValue): Knight = {
    json.as[Knight]
  }
}

class Knight(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.KNIGHT
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♘" else "♞"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Knight.png" else "/white/Knight.png"

  def toXml: Elem = {
    <knight>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
    </knight>
  }

  def toJson: JsValue = Json.toJson(this)
}

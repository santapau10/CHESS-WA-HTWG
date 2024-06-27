package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

object Rook {
  implicit val writes: Writes[Rook] = Json.writes[Rook]
  implicit val reads: Reads[Rook] = Json.reads[Rook]

  def fromXML(node: Node): Rook = {
    val x = (node \ "cords" \ "x").text.toInt
    val y = (node \ "cords" \ "y").text.toInt
    val color = Colors.withName((node \ "color").text)
    new Rook((x, y), color)
  }

  def fromJSON(json: JsValue): Rook = {
    json.as[Rook]
  }
}

class Rook(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.ROOK
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♖" else "♜"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Rook.png" else "/white/Rook.png"

  def toXml: Elem = {
    <rook>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
    </rook>
  }

  def toJson: JsValue = Json.toJson(this)
}

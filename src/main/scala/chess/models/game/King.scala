package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

object King {
  implicit val writes: Writes[King] = Json.writes[King]
  implicit val reads: Reads[King] = Json.reads[King]

  def fromXML(node: Node): King = {
    val x = (node \ "cords" \ "x").text.toInt
    val y = (node \ "cords" \ "y").text.toInt
    val color = Colors.withName((node \ "color").text)
    new King((x, y), color)
  }

  def fromJSON(json: JsValue): King = {
    json.as[King]
  }
}

class King(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.KING
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♔" else "♚"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/King.png" else "/white/King.png"

  def toXml: Elem = {
    <king>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
    </king>
  }

  def toJson: JsValue = Json.toJson(this)
}

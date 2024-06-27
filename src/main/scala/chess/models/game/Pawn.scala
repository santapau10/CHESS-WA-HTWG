package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

object Pawn {
  implicit val writes: Writes[Pawn] = Json.writes[Pawn]
  implicit val reads: Reads[Pawn] = Json.reads[Pawn]

  def fromXML(node: Node): Pawn = {
    val x = (node \ "cords" \ "x").text.toInt
    val y = (node \ "cords" \ "y").text.toInt
    val color = Colors.withName((node \ "color").text)
    new Pawn((x, y), color)
  }

  def fromJSON(json: JsValue): Pawn = {
    json.as[Pawn]
  }
}

class Pawn(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.PAWN
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♙" else "♟"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Pawn.png" else "/white/Pawn.png"

  def toXml: Elem = {
    <pawn>
      <cords>
        <x>{cords._1}</x>
        <y>{cords._2}</y>
      </cords>
      <color>{color.toString}</color>
    </pawn>
  }

  def toJson: JsValue = Json.toJson(this)
}

package chess.models.game

import chess.models.*
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

// JSON serializers and deserializers using Play JSON


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

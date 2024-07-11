package models.game

import chess.models.game.*

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}
import play.api.libs.json.{JsValue, Json, Writes, Reads}
import scala.xml.{Elem, Node}

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.xml.XML

class KnightSpec extends AnyWordSpec with Matchers {

  val knight = new Knight((4, 4), Colors.WHITE, moved = false, last_cords = (4, 4))
  val blackKnight = new Knight((4, 4), Colors.BLACK, moved = true, last_cords = (3, 3))

  "Knight" in {
    knight.getCords should be ((4, 4))
  }

  "have correct last coordinates" in {
    knight.getLastCords should be ((4, 4))
    blackKnight.getLastCords should be ((3, 3))
  }

  "report correct moved status" in {
    knight.isMoved should be (false)
    blackKnight.isMoved should be (true)
  }

  "have correct color" in {
    knight.getColor should be (Colors.WHITE)
    blackKnight.getColor should be (Colors.BLACK)
  }

  "return correct piece type" in {
    knight.getPiece should be (Chesspiece.KNIGHT)
  }

  "have correct string representation" in {
    knight.toString should be ("♞")
    blackKnight.toString should be ("♘")
  }

  "have correct icon path" in {
    knight.getIconPath should be ("/white/Knight.png")
    blackKnight.getIconPath should be ("/black/Knight.png")
  }

  "convert to correct XML" in {
    val xml = knight.toXml
    (xml \\ "cords" \ "x").text.trim.toInt should be (4)
    (xml \\ "cords" \ "y").text.trim.toInt should be (4)
    (xml \\ "color").text.trim should be ("WHITE")
    (xml \\ "moved").text.trim.toBoolean should be (false)
    (xml \\ "lastcords" \ "x").text.trim.toInt should be (4)
    (xml \\ "lastcords" \ "y").text.trim.toInt should be (4)
  }

  "convert to correct JSON" in {
    val json = knight.toJson
    (json \ "cords" \ "x").as[Int] should be (4)
    (json \ "cords" \ "y").as[Int] should be (4)
    (json \ "color").as[String] should be ("WHITE")
    (json \ "moved").as[String].toBoolean should be (false)
    (json \ "piece").as[String] should be ("KNIGHT")
    (json \ "lastcords" \ "x").as[Int] should be (4)
    (json \ "lastcords" \ "y").as[Int] should be (4)
  }

  "validate correct knight moves" in {
    val pieces = List(knight)
    knight.checkMove(4, 4, 6, 5, pieces) should be (true)
    knight.checkMove(4, 4, 5, 6, pieces) should be (true)
    knight.checkMove(4, 4, 2, 5, pieces) should be (true)
    knight.checkMove(4, 4, 2, 3, pieces) should be (true)
    knight.checkMove(4, 4, 3, 2, pieces) should be (true)
    knight.checkMove(4, 4, 5, 2, pieces) should be (true)
    knight.checkMove(4, 4, 6, 3, pieces) should be (true)
  }

  "invalidate incorrect knight moves" in {
    val pieces = List(knight)
    knight.checkMove(4, 4, 5, 5, pieces) should be (false)
    knight.checkMove(4, 4, 4, 5, pieces) should be (false)
    knight.checkMove(4, 4, 4, 4, pieces) should be (false)
  }

  "invalidate moves to squares occupied by same color pieces" in {
    val anotherWhitePiece = new Knight((6, 5), Colors.WHITE, moved = false, last_cords = (6, 5))
    val pieces = List(knight, anotherWhitePiece)
    knight.checkMove(4, 4, 6, 5, pieces) should be (false)
  }

  "validate moves to squares occupied by opposite color pieces" in {
    val blackPiece = new Knight((6, 5), Colors.BLACK, moved = false, last_cords = (6, 5))
    val pieces = List(knight, blackPiece)
    knight.checkMove(4, 4, 6, 5, pieces) should be (true)
  }
}
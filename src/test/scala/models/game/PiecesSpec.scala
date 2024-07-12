package models.game

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsValue, Json}
import scala.xml.XML
import chess.models.game._
import chess.models.IPieces


class PiecesSpec extends AnyWordSpec with Matchers {

  "PiecesFactory" should {
    val factory = new PiecesFactory()

    "create correct pieces for all types and colors" in {
      val pieces = List(
        (Chesspiece.PAWN, "Pawn"),
        (Chesspiece.KNIGHT, "Knight"),
        (Chesspiece.BISHOP, "Bishop"),
        (Chesspiece.ROOK, "Rook"),
        (Chesspiece.QUEEN, "Queen"),
        (Chesspiece.KING, "King")
      )

      for {
        (pieceType, className) <- pieces
        color <- List(Colors.WHITE, Colors.BLACK)
        moved <- List(true, false)
      } {
        val piece = factory.addPiece(pieceType, (0, 0), color, moved, (1, 1))
        piece.getClass.getSimpleName should be(className)
        piece.getCords should be((0, 0))
        piece.getColor should be(color)
        piece.isMoved should be(moved)
        piece.getLastCords should be((1, 1))
      }
    }
  }

  "PiecesFactory object" should {
    "create all piece types from valid XML" in {
      val pieces = List(
        ("pawn", classOf[Pawn]),
        ("knight", classOf[Knight]),
        ("bishop", classOf[Bishop]),
        ("rook", classOf[Rook]),
        ("queen", classOf[Queen]),
        ("king", classOf[King])
      )

      for {
        (pieceType, pieceClass) <- pieces
        color <- List("WHITE", "BLACK")
        moved <- List("true", "false")
      } {
        val xml =
          <piece>
            <cords>
              <x>0</x>
              <y>1</y>
            </cords>
            <color>{color}</color>
            <moved>{moved}</moved>
            <lastcords>
              <x>0</x>
              <y>0</y>
            </lastcords>
          </piece>.copy(label = pieceType)

        val piece = PiecesFactory.fromXml(xml)
        piece.getClass should be(pieceClass)
        piece.getCords should be((0, 1))
        piece.getColor should be(Colors.valueOf(color))
        piece.isMoved should be(moved.toBoolean)
        piece.getLastCords should be((0, 1))
      }
    }

    "create all piece types from valid JSON" in {
      val pieces = List(
        "PAWN", "KNIGHT", "BISHOP", "ROOK", "QUEEN", "KING"
      )

      for {
        pieceType <- pieces
        color <- List("WHITE", "BLACK")
        moved <- List("true", "false")
      } {
        val json = Json.parse(s"""
          {
            "piece": "$pieceType",
            "cords": {"x": 1, "y": 0},
            "color": "$color",
            "moved": "$moved",
            "lastcords": {"x": 1, "y": 1}
          }
        """)

        val piece = PiecesFactory.fromJson(json)
        piece.getClass.getSimpleName.toUpperCase should be(pieceType)
        piece.getCords should be((1, 0))
        piece.getColor should be(Colors.valueOf(color))
        piece.isMoved should be(moved.toBoolean)
        piece.getLastCords should be((1, 1))
      }
    }

    "throw exception for invalid piece type in XML" in {
      val xml = <invalidPiece></invalidPiece>
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromXml(xml)
    }

    "throw exception for missing coordinates in XML" in {
      val xml = <pawn><color>WHITE</color><moved>false</moved></pawn>
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromXml(xml)
    }

    "throw exception for invalid color in XML" in {
      val xml =
        <pawn>
          <cords><x>0</x><y>1</y></cords>
          <color>INVALID</color>
          <moved>false</moved>
          <lastcords><x>0</x><y>0</y></lastcords>
        </pawn>
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromXml(xml)
    }

    "throw exception for invalid moved value in XML" in {
      val xml =
        <pawn>
          <cords><x>0</x><y>1</y></cords>
          <color>WHITE</color>
          <moved>invalid</moved>
          <lastcords><x>0</x><y>0</y></lastcords>
        </pawn>
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromXml(xml)
    }

    "throw exception for missing last coordinates in XML" in {
      val xml =
        <pawn>
          <cords><x>0</x><y>1</y></cords>
          <color>WHITE</color>
          <moved>true</moved>
        </pawn>
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromXml(xml)
    }

    "throw exception for invalid piece type in JSON" in {
      val json = Json.parse("""{"piece": "INVALID"}""")
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromJson(json)
    }

    "throw exception for missing coordinates in JSON" in {
      val json = Json.parse("""{"piece": "PAWN", "color": "WHITE", "moved": "false"}""")
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromJson(json)
    }

    "throw exception for invalid color in JSON" in {
      val json = Json.parse("""
        {
          "piece": "PAWN",
          "cords": {"x": 0, "y": 1},
          "color": "INVALID",
          "moved": "false",
          "lastcords": {"x": 0, "y": 0}
        }
      """)
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromJson(json)
    }

    "throw exception for invalid moved value in JSON" in {
      val json = Json.parse("""
        {
          "piece": "PAWN",
          "cords": {"x": 0, "y": 1},
          "color": "WHITE",
          "moved": "invalid",
          "lastcords": {"x": 0, "y": 0}
        }
      """)
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromJson(json)
    }

    "throw exception for missing last coordinates in JSON" in {
      val json = Json.parse("""
        {
          "piece": "PAWN",
          "cords": {"x": 0, "y": 1},
          "color": "WHITE",
          "moved": "true"
        }
      """)
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromJson(json)
    }

    "throw exception for invalid coordinate values in XML" in {
      val xml =
        <pawn>
          <cords><x>invalid</x><y>1</y></cords>
          <color>WHITE</color>
          <moved>true</moved>
          <lastcords><x>0</x><y>0</y></lastcords>
        </pawn>
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromXml(xml)
    }

    "throw exception for invalid coordinate values in JSON" in {
      val json = Json.parse("""
        {
          "piece": "PAWN",
          "cords": {"x": "invalid", "y": 1},
          "color": "WHITE",
          "moved": "true",
          "lastcords": {"x": 0, "y": 0}
        }
      """)
      an [IllegalArgumentException] should be thrownBy PiecesFactory.fromJson(json)
    }
  }
}
package chess.models

import chess.models.*
import chess.models.game.{Chesspiece, Colors, PiecesFactory}
import play.api.libs.json.{JsValue, Json, Reads, Writes}
import scala.xml.{Elem, Node}

import scala.xml._

import scala.xml.Node

abstract class Board(size: Int) extends IBoardBuilder {
  val h2Line = "__"
  val hLine = "_"
  private val vLine = "|"
  private val space = " "
  val nextLVLine = "|\n"
  private val nextLPlus = "+\n"
  private val nextL = "\n"
  private val plus = "+"
  private val minus = "-"
  private val edge = s"$plus${minus * 4}"
  private val edgeField = s"${edge * this.size}$nextLPlus"


  override def getSize(): Int = size

  override def getSetupBoard: List[IPieces] ={
    List.empty
  }

  override def movePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): List[IPieces] = {
    list.find(piece => piece.getCords == (x1, y1)) match {
      case Some(foundPiece) =>
        val pf: IPiecesFactory = new PiecesFactory()
        val movedPiece = pf.addPiece(foundPiece.getPiece, (x2, y2), foundPiece.getColor, true, (x1,y1))
        list.find(piece => piece.getCords == (x2, y2)) match {
          case Some(foundOldPiece) =>
            val updatedBoard = list.filter(piece => piece != foundPiece && piece != foundOldPiece) :+ movedPiece
            updatedBoard
          case None =>
            val updatedBoard = list.filter(piece => piece != foundPiece) :+ movedPiece
            updatedBoard
        }
      case None =>
        println("Piece not found at the specified Chess.models.coordinates.")
        null
    }
  }
  override def promotePiece(chesspiece: Chesspiece, list: List[IPieces]): List[IPieces] = {
    list.filterNot(p => p.getCords == list.last.getCords) :+ PiecesFactory().addPiece(chesspiece, list.last.getCords, list.last.getColor, list.last.isMoved, list.last.getLastCords)
  }
  def preMovePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): List[IPieces] = {
    list.find(piece => piece.getCords == (x1, y1)) match {
      case Some(foundPiece) =>
        val pf: IPiecesFactory = new PiecesFactory()
        val movedPiece = pf.addPiece(foundPiece.getPiece, (x2, y2), foundPiece.getColor, false, (x1,x2))
        list.find(piece => piece.getCords == (x2, y2)) match {
          case Some(foundOldPiece) =>
            val updatedBoard = list.filter(piece => piece != foundPiece && piece != foundOldPiece) :+ movedPiece
            updatedBoard
          case None =>
            val updatedBoard = list.filter(piece => piece != foundPiece) :+ movedPiece
            updatedBoard
        }
      case None =>
        println("Piece not found at the specified Chess.models.coordinates.")
        null
    }
  }
  override def deletePiece (x1: Int, y1: Int, list: List[IPieces]): List[IPieces] = {
    list.find(piece => piece.getCords == (x1,y1)) match
      case Some(foundPiece) =>
        list.filter(piece => piece != foundPiece)
      case None =>
        throw new IllegalArgumentException("invalid coordinates for the deletion prozess")
  }
  override def checkFieldR(x: Int, y: Int, list: List[IPieces]): String = {
    val sr = new StringBuilder
    sr.append(s"$vLine$space")
    list.find(piece => piece.getCords == (x, y)) match {
      case Some(foundPiece) => sr.append(s"${foundPiece.toString}${space * 2}")
      case None => sr.append(space * 3)
    }
    if (x < size - 1) {
      sr.append(checkFieldR(x + 1, y, list))
    } else if (y < size - 1) {
      sr.append(s"$vLine${space * 2}${y + 1}$nextL$edgeField")
      sr.append(checkFieldR(0, y + 1, list))
    }
    sr.toString()
  }

  override def updateField(list: List[IPieces]): String = {
    val sr = new StringBuilder
    sr.append(firstLineR(0))
    sr.append("\n")
    sr.append(edgeField)
    sr.append(checkFieldR(0, 0, list))
    sr.append(s"$vLine${space * 2}${this.size}$nextL$edgeField")
    sr.toString()
  }

  override def firstLineR(index: Int): String = {
    val sr = new StringBuilder
    if (index < this.size) {
      sr.append(s"${space * 2}${('a'.toInt + index).toChar}${space * 2}")
      sr.append(firstLineR(index + 1))
      sr.toString()
    } else {
      sr.toString()
    }
  }
}

class Board_equal_8(size: Int) extends Board(size: Int) {

  override def toXml(): Elem = <game>
    <type>Board_equal_8</type>
    <size>
      {size}
    </size>
  </game>



  override def toJSON(): JsValue = {
    val json = Json.obj(
      "className" -> this.getClass.getSimpleName,
      "size" -> size
    )
    Json.toJson(json)
  }

  override def getSetupBoard: List[IPieces] = {
    val pf: IPiecesFactory = new PiecesFactory()
    val a = List(
      pf.addPiece(Chesspiece.ROOK, (0, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.KNIGHT, (1, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.BISHOP, (2, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.QUEEN, (3, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.KING, (4, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.BISHOP, (5, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.KNIGHT, (6, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.ROOK, (7, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (0, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (1, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (2, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (3, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (4, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (5, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (6, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (7, 1), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.ROOK, (0, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.KNIGHT, (1, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.BISHOP, (2, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.QUEEN, (3, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.KING, (4, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.BISHOP, (5, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.KNIGHT, (6, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.ROOK, (7, 7), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (0, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (1, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (2, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (3, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (4, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (5, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (6, 6), Colors.BLACK, false, (-1, -1)),
      pf.addPiece(Chesspiece.PAWN, (7, 6), Colors.BLACK, false, (-1, -1))
    )
    a
  }
}
class Board_smaller_8(size: Int) extends Board(size: Int) {

  override def toXml(): Elem = <game>
    {getClass.getSimpleName}
    <size>
      {size}
    </size>
  </game>

  override def toJSON(): JsValue = {
    val json = Json.obj(
      "className" -> this.getClass.getSimpleName,
      "size" -> size
    )
    Json.toJson(json)
  }

  override def getSetupBoard: List[IPieces] = {
    val pf: IPiecesFactory = new PiecesFactory()
    val a = List(
      pf.addPiece(Chesspiece.ROOK, (0, 0), Colors.WHITE, false, (-1, -1)), // 6
      pf.addPiece(Chesspiece.KNIGHT, (1, 0), Colors.WHITE, false, (-1, -1)), // 2
      pf.addPiece(Chesspiece.BISHOP, (2, 0), Colors.WHITE, false, (-1, -1)), // 4
      pf.addPiece(Chesspiece.QUEEN, (3, 0), Colors.WHITE, false, (-1, -1)), // 1
      pf.addPiece(Chesspiece.KING, (4, 0), Colors.WHITE, false, (-1, -1)),
      pf.addPiece(Chesspiece.BISHOP, (5, 0), Colors.WHITE, false, (-1, -1)), // 5
      pf.addPiece(Chesspiece.KNIGHT, (6, 0), Colors.WHITE, false, (-1, -1)), // 3
      //pf.addPiece(Chesspiece.ROOK, (7, 0), Colors.WHITE, (-1, -1)), // 7

      pf.addPiece(Chesspiece.PAWN, (0, 1), Colors.WHITE, false, (-1, -1)), // 6
      pf.addPiece(Chesspiece.PAWN, (1, 1), Colors.WHITE, false, (-1, -1)), // 3
      pf.addPiece(Chesspiece.PAWN, (2, 1), Colors.WHITE, false, (-1, -1)), // 4
      pf.addPiece(Chesspiece.PAWN, (3, 1), Colors.WHITE, false, (-1, -1)), // 3
      pf.addPiece(Chesspiece.PAWN, (4, 1), Colors.WHITE, false, (-1, -1)), // 3
      pf.addPiece(Chesspiece.PAWN, (5, 1), Colors.WHITE, false, (-1, -1)), // 5
      pf.addPiece(Chesspiece.PAWN, (6, 1), Colors.WHITE, false, (-1, -1)), // 3
      //pf.addPiece(Chesspiece.PAWN, (7, 1), Colors.WHITE, (-1, -1)), // 7

      pf.addPiece(Chesspiece.ROOK, (0, 6), Colors.BLACK, false, (-1, -1)), // 6
      pf.addPiece(Chesspiece.KNIGHT, (1, 6), Colors.BLACK, false, (-1, -1)), // 2
      pf.addPiece(Chesspiece.BISHOP, (2, 6), Colors.BLACK, false, (-1, -1)), // 4
      pf.addPiece(Chesspiece.QUEEN, (3, 6), Colors.BLACK, false, (-1, -1)), // 1
      pf.addPiece(Chesspiece.KING, (4, 6), Colors.BLACK, false, (-1, -1)), // 1
      pf.addPiece(Chesspiece.BISHOP, (5, 6), Colors.BLACK, false, (-1, -1)), // 5
      pf.addPiece(Chesspiece.KNIGHT, (6, 6), Colors.BLACK, false, (-1, -1)), // 3
      //pf.addPiece(Chesspiece.ROOK, (7, size - 1), Colors.BLACK, (-1, -1)), // 7

      pf.addPiece(Chesspiece.PAWN, (0, 5), Colors.BLACK, false, (-1, -1)), // 6
      pf.addPiece(Chesspiece.PAWN, (1, 5), Colors.BLACK, false, (-1, -1)), // 3
      pf.addPiece(Chesspiece.PAWN, (2, 5), Colors.BLACK, false, (-1, -1)), // 4
      pf.addPiece(Chesspiece.PAWN, (3, 5), Colors.BLACK, false, (-1, -1)), // 3
      pf.addPiece(Chesspiece.PAWN, (4, 5), Colors.BLACK, false, (-1, -1)), // 3
      pf.addPiece(Chesspiece.PAWN, (5, 5), Colors.BLACK, false, (-1, -1)), // 5
      pf.addPiece(Chesspiece.PAWN, (6, 5), Colors.BLACK, false, (-1, -1)) // 3
      //pf.addPiece(Chesspiece.PAWN, (7, size - 2), Colors.BLACK, (-1, -1)) // 7
    )

    if (size < 7) {
      val a1 = a.filter(piece => (0,6) != piece.getCords && (6,1) != piece.getCords && (6,5) != piece.getCords)
      val a2 = linePushR(5, 0, 1, 0, -1, 0, a1)
      val a3 = linePushR(5, 0, 0, 5, 0, -1, a2)
      val a4 = linePushR(5, 0, 1, 6, -1, -1, a3)
      if (size < 6) {
        val b1 = preMovePieces(5,0,4,0, a4)
        val b2 = b1.filter(piece => (5,1) != piece.getCords && (5,4) != piece.getCords && (4,5) != piece.getCords)
        val b3 = linePushR(4, 0, 0, 4, 0, -1, b2)
        val b4 = linePushR(3, 0, 0, 5, 0, -1, b3)
        val b5 = preMovePieces(5,5,4,4, b4)
        if (size < 5) {
          val c1 = b5.filter(piece => (4,1) != piece.getCords && (4,3) != piece.getCords && (1,4) != piece.getCords)
          val c2 = linePushR(2, 0, 2, 0, -1, 0, c1)
          val c3 = linePushR(3, 0, 0, 3, 0, -1, c2)
          val c4 = preMovePieces(0,4,0,3, c3)
          val c5 = linePushR(2, 0, 2, 4, -1, -1, c4)
          if (size < 4) {
            val d1 = c5.filter(piece => (0,1) != piece.getCords && (1,1) != piece.getCords && (2,1) != piece.getCords && (3,0) != piece.getCords && (3,1) != piece.getCords && (3,2) != piece.getCords && (3,3) != piece.getCords)
            val d2 = linePushR(2, 0, 0, 3, 0, -1, d1)
            if (size < 3) {
              val e1 = d2.filter(piece => (0,2) != piece.getCords)
              val e2 = linePushR(1, 0, 1, 0, -1, 0, e1)
              val e3 = linePushR(1, 0, 1, 2, -1, -1, e2)
              if (size < 2) {
                val f = preMovePieces(1,0,0,0, e3)
                return f.filter(piece => (0,1) != piece.getCords && (1,1) != piece.getCords)
              }
              return e3
            }
            return d2
          }
          return c5
        }
        return b5
      }
      return a4
    }
    a
  }
  private def linePushR(limit: Int, counter: Int, x: Int, y: Int, xFactor: Int, yFactor: Int, list: List[IPieces]): List[IPieces] = {
    if (counter <= limit)
      val newList: List[IPieces] = preMovePieces(x, y, x + xFactor, y + yFactor, list)
      linePushR(limit, counter + 1, x + 1, y, xFactor, yFactor, newList)
    else
      list
  }
}
class Board_bigger_8(size: Int) extends Board(size: Int) {

  override def toXml(): Elem = <game>
    {getClass.getSimpleName}
    <size>
      {size}
    </size>
  </game>

  override def toJSON(): JsValue = {
    val json = Json.obj(
      "className" -> this.getClass.getSimpleName,
      "size" -> size
    )
    Json.toJson(json)
  }


  override def getSetupBoard: List[IPieces] = {
    val pf: IPiecesFactory = new PiecesFactory()
    if (size % 2 == 0 && size > 8) {
      val h = size / 2
      val i = size - 8
      val a = List(
        pf.addPiece(Chesspiece.ROOK, (h - 4, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.KING, (h, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.ROOK, (h + 3, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 4, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 3, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 2, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 1, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 1, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 2, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 3, 1), Colors.WHITE, false, (-1, -1)),

        pf.addPiece(Chesspiece.ROOK, (h - 4, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.KING, (h, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.ROOK, (h + 3, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 4, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 3, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 2, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 1, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 1, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 2, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 3, size - 2), Colors.BLACK, false, (-1, -1))
      )
      a
    } else {
      val h = (size - 1) / 2
      val i = size - 9
      val a = List(
        pf.addPiece(Chesspiece.ROOK, (h - 4, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.KING, (h, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.ROOK, (h + 3, 0), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 4, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 3, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 2, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 1, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 1, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 2, 1), Colors.WHITE, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 3, 1), Colors.WHITE, false, (-1, -1)),

        pf.addPiece(Chesspiece.ROOK, (h - 4, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.KING, (h, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.ROOK, (h + 3, size - 1), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 4, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 3, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 2, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h - 1, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 1, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 2, size - 2), Colors.BLACK, false, (-1, -1)),
        pf.addPiece(Chesspiece.PAWN, (h + 3, size - 2), Colors.BLACK, false, (-1, -1))
      )
      a
    }
  }

}



  object Board {

    def fromJson(json: JsValue): IBoardBuilder = {
      val className = (json\ "className").as[String]
      val size = (json \ "size").as[Int]

      className match {
        case "Board_bigger_8" => new Board_bigger_8(size)
        case "Board_equal_8" => new Board_equal_8(size)
        case "Board_smaller_8" => new Board_smaller_8(size)
        case _ => throw new IllegalArgumentException(s"Unsupported board type: $className")
      }
    }

    def fromXml(xmlNode: Node): IBoardBuilder = {
      val className = (xmlNode \\ "type").text.trim
      val s = (xmlNode \\ "size").text.trim
      className match {
        case "Board_bigger_8" => Board_bigger_8(s.toInt)
        case "Board_equal_8" => Board_equal_8(s.toInt)
        case "Board_smaller_8" => Board_smaller_8(s.toInt)
        case _ => throw new IllegalArgumentException(s"Unsupported board type: $className")
      }
    }
  }






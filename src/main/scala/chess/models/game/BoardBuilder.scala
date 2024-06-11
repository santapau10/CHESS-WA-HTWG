package chess.models.game

import chess.models.*

abstract class Board(size: Int) extends IBoardBuilder {
  val h2Line = "__"
  val hLine = "_"
  val vLine = "|"
  val space = " "
  val nextLVLine = "|\n"
  val nextLPlus = "+\n"
  val plus = "+"
  val minus = "-"
  val edge = plus + minus * 4
  val edgefield = edge * this.size + nextLPlus

  override def getSetupBoard(): List[IPieces] ={
    List.empty
  }

  override def movePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): List[IPieces] = {
    list.find(piece => piece.getCords == (x1, y1)) match {
      case Some(foundPiece) =>
        val pf: IPiecesFactory = new PiecesFactory
        val movedPiece = pf.addPiece(foundPiece.getPiece, (x2, y2), foundPiece.getColor)
        val updatedBoard = list.filter(piece => piece != foundPiece) :+ movedPiece
        updatedBoard
      case None =>
        println("Piece not found at the specified Chess.models.coordinates.")
        return null
    }
  }

  override def checkFieldR(x: Int, y: Int, list: List[IPieces]): String = {
    val sr = new StringBuilder
    sr.append(vLine + space)
    list.find(piece => piece.getCords == (x, y)) match {
      case Some(foundPiece) => sr.append(foundPiece.toString + space * 2)
      case None => sr.append(space * 3)
    }
    if (x < size - 1) {
      sr.append(checkFieldR(x + 1, y, list))
    } else if (y < size - 1) {
      sr.append(vLine + space * 2 + (y + 1) + "\n" + edgefield)
      sr.append(checkFieldR(0, y + 1, list))
    }
    sr.toString()
  }

  override def updateField(list: List[IPieces]): String = {
    val sr = new StringBuilder
    sr.append(firstLineR(0))
    sr.append("\n")
    sr.append(edgefield)
    sr.append(checkFieldR(0, 0, list))
    sr.append(vLine + space * 2 + (this.size) + "\n" + edgefield)
    sr.toString()
  }

  override def firstLineR(index: Int): String = {
    val sr = new StringBuilder
    if (index < this.size) {
      sr.append(space * 2 + ('a'.toInt + index).toChar + space * 2)
      sr.append(firstLineR(index + 1))
      sr.toString()
    } else {
      sr.toString()
    }
  }
}

class Board_equal_8(size: Int) extends Board(size: Int) {
  override def getSetupBoard(): List[IPieces] = {
    val pf: IPiecesFactory = new PiecesFactory
    val a = List(
      pf.addPiece(Chesspiece.ROOK, (0, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.KNIGHT, (1, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.BISHOP, (2, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.QUEEN, (3, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.KING, (4, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.BISHOP, (5, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.KNIGHT, (6, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.ROOK, (7, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (0, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (1, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (2, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (3, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (4, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (5, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (6, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (7, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.ROOK, (0, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.KNIGHT, (1, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.BISHOP, (2, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.QUEEN, (3, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.KING, (4, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.BISHOP, (5, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.KNIGHT, (6, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.ROOK, (7, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (0, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (1, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (2, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (3, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (4, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (5, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (6, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (7, 6), Colors.BLACK)
    )
    a
  }
}
class Board_smaller_8(size: Int) extends Board(size: Int) {
  override def getSetupBoard(): List[IPieces] = {
    val pf: IPiecesFactory = new PiecesFactory
    val a = List(
      pf.addPiece(Chesspiece.ROOK, (0, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.KNIGHT, (1, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.BISHOP, (2, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.QUEEN, (3, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.KING, (4, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.BISHOP, (5, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.KNIGHT, (6, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.ROOK, (7, 0), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (0, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (1, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (2, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (3, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (4, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (5, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (6, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.PAWN, (7, 1), Colors.WHITE),
      pf.addPiece(Chesspiece.ROOK, (0, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.KNIGHT, (1, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.BISHOP, (2, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.QUEEN, (3, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.KING, (4, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.BISHOP, (5, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.KNIGHT, (6, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.ROOK, (7, 7), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (0, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (1, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (2, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (3, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (4, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (5, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (6, 6), Colors.BLACK),
      pf.addPiece(Chesspiece.PAWN, (7, 6), Colors.BLACK)
    )
    return a
  }
}
class Board_bigger_8(size: Int) extends Board(size: Int) {
  override def getSetupBoard(): List[IPieces] = {
    val pf: IPiecesFactory = new PiecesFactory
    if(size % 2 == 0 && size > 8) {
      val h = size / 2
      val i = size - 8
      val a = List(
        pf.addPiece(Chesspiece.ROOK, (h - 4, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.KING, (h, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.ROOK, (h + 3, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 4, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 3, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 2, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 1, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h + 1, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h + 2, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h + 3, 1), Colors.WHITE),

        pf.addPiece(Chesspiece.ROOK, (h - 4, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.KING, (h, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.ROOK, (h + 3, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 4, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 3, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 2, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 1, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h + 1, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h + 2, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h + 3, size - 2), Colors.BLACK)
      )
      a
    } else {
      val h = (size - 1) / 2
      val i = size - 9
      val a = List(
        pf.addPiece(Chesspiece.ROOK, (h - 4, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.KING, (h, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.ROOK, (h + 3, 0), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 4, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 3, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 2, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h - 1, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h + 1, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h + 2, 1), Colors.WHITE),
        pf.addPiece(Chesspiece.PAWN, (h + 3, 1), Colors.WHITE),

        pf.addPiece(Chesspiece.ROOK, (h - 4, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.KNIGHT, (h - 3, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.BISHOP, (h - 2, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.QUEEN, (h - 1, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.KING, (h, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.BISHOP, (h + 1, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.KNIGHT, (h + 2, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.ROOK, (h + 3, size - 1), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 4, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 3, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 2, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h - 1, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h + 1, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h + 2, size - 2), Colors.BLACK),
        pf.addPiece(Chesspiece.PAWN, (h + 3, size - 2), Colors.BLACK)

      )
      a
    }
  }
}


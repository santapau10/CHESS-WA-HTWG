package chess.models

object Board {
  def Board(size: Int): Unit = new Board(size)
}

  val h2Line = "__"
  val hLine = "_"
  val vLine = "|"
  val space = " "
  val nextLVLine = "|\n"
  val nextLPlus = "+\n"
  val plus = "+"
  val minus = "-"
  val edge = plus + minus * 4

class Board(size: Int) {
  def movePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[Pieces]): List[Pieces] = {
    list.find(piece => piece.getCords == (x1, y1)) match {
      case Some(foundPiece) =>
        val movedPiece = Pieces.addPiece(foundPiece.getPiece, (x2,y2), foundPiece.getColor)
        val updatedBoard = list.filter(piece => piece != foundPiece) :+ movedPiece
        updatedBoard
      case None =>
        println("Piece not found at the specified Chess.models.coordinates.")
        return null
    }
  }

  val edgefield = edge * size + nextLPlus
  def checkFieldR(x: Int, y: Int, list: List[Pieces]): String = {
    val sr = new StringBuilder
    sr.append(vLine + space)
    list.find(piece => piece.getCords == (x, y)) match {
      case Some(foundPiece) => sr.append(foundPiece.toString + space*2)
      case None => sr.append(space * 3)
    }
    if(x < size - 1){
      sr.append(checkFieldR(x+1,y, list))
    } else if(y < size - 1) {
      sr.append(vLine + space * 2 + (y+1) + "\n" + edgefield)
      sr.append(checkFieldR(0,y+1, list))
    }
    sr.toString()
  }
  def updateField(list: List[Pieces]): String = {
    val sr = new StringBuilder
    sr.append(space * 2 + "a" + space * 2)
    sr.append(space * 2 + "b" + space * 2)
    sr.append(space * 2 + "c" + space * 2)
    sr.append(space * 2 + "d" + space * 2)
    sr.append(space * 2 + "e" + space * 2)
    sr.append(space * 2 + "f" + space * 2)
    sr.append(space * 2 + "g" + space * 2)
    sr.append(space * 2 + "h" + space * 2)
    sr.append("\n")
    sr.append(edgefield)
    sr.append(checkFieldR(0,0, list))
    sr.append(vLine + space * 2 + (size) + "\n" + edgefield)
    sr.toString()
  }
  val setupBoard: List[Pieces] = {
  val startingPositions = List(
    Pieces.addPiece(Chesspiece.ROOK, (0, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.KNIGHT, (1, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.BISHOP, (2, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.QUEEN, (3, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.KING, (4, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.BISHOP, (5, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.KNIGHT, (6, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.ROOK, (7, 0), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (0, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (1, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (2, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (3, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (4, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (5, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (6, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.PAWN, (7, 1), Colors.WHITE),
    Pieces.addPiece(Chesspiece.ROOK, (0, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.KNIGHT, (1, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.BISHOP, (2, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.QUEEN, (3, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.KING, (4, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.BISHOP, (5, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.KNIGHT, (6, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.ROOK, (7, 7), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (0, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (1, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (2, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (3, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (4, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (5, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (6, 6), Colors.BLACK),
    Pieces.addPiece(Chesspiece.PAWN, (7, 6), Colors.BLACK)
  )
  startingPositions
  }
}

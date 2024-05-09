package chess.models
trait Pieces {
  def getColor: Colors
  def getPiece: Chesspiece
  def getCords: (Int, Int)
  def toString: String
}
enum Colors:
  case WHITE, BLACK

enum Chesspiece:
  case KING, PAWN, QUEEN, BISHOP, KNIGHT, ROOK

object Pieces {
  def addPiece(chesspiece: Chesspiece, cords: (Int, Int), color: Colors): Pieces = chesspiece match {
    case Chesspiece.PAWN => new Pawn(cords, color)
    case Chesspiece.KNIGHT => new Knight(cords, color)
    case Chesspiece.BISHOP => new Bishop(cords, color)
    case Chesspiece.ROOK => new Rook(cords, color)
    case Chesspiece.QUEEN => new Queen(cords, color)
    case Chesspiece.KING => new King(cords, color)
  }
}
package chess.models.game

import chess.models.*

enum Colors:
  case WHITE, BLACK

enum Chesspiece:
  case KING, PAWN, QUEEN, BISHOP, KNIGHT, ROOK

class PiecesFactory extends IPiecesFactory {
  override def addPiece(chesspiece: Chesspiece, cords: (Int, Int), color: Colors): IPieces = chesspiece match {
    case Chesspiece.PAWN => new Pawn(cords, color)
    case Chesspiece.KNIGHT => new Knight(cords, color)
    case Chesspiece.BISHOP => new Bishop(cords, color)
    case Chesspiece.ROOK => new Rook(cords, color)
    case Chesspiece.QUEEN => new Queen(cords, color)
    case Chesspiece.KING => new King(cords, color)
  }
}


package chess.models.game

import chess.models.IPieces
import chess.models.game.{Chesspiece, Colors}

class Pawn(cords: (Int, Int), color: Colors) extends IPieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.PAWN
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♙" else "♟"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Pawn.png" else "/white/Pawn.png"
}
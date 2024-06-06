package chess.models

class King(cords: (Int, Int), color: Colors) extends Pieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.KING
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♔" else "♚"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/King.png" else "/white/King.png"
}
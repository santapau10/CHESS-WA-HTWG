package chess.models

class Bishop(cords: (Int, Int), color: Colors) extends Pieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.BISHOP
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♗" else "♝"

  override def getIconPath: String = if (color == Colors.BLACK) "/black/Bishop.png" else "/white/Bishop.png"
}
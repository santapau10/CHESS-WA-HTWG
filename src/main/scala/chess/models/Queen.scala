package chess.models

class Queen(cords: (Int, Int), color: Colors) extends Pieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.QUEEN
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♕" else "♛"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Queen.png" else "/white/Queen.png"
}
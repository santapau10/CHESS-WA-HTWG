package chess.models

class Knight(cords: (Int, Int), color: Colors) extends Pieces {
  override def getColor: Colors = color
  override def getPiece: Chesspiece = Chesspiece.KNIGHT
  override def getCords: (Int, Int) = cords
  override def toString: String = if (color == Colors.BLACK) "♘" else "♞"
  override def getIconPath: String = if (color == Colors.BLACK) "/black/Knight.png" else "/white/Knight.png"
}
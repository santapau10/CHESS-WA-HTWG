package chess.models

import chess.models.game.{Chesspiece, Colors}
import chess.models.IPieces


trait IBoardBuilder:

  def movePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): List[IPieces]
  def checkFieldR(x: Int, y: Int, list: List[IPieces]): String
  def updateField(list: List[IPieces]): String
  def firstLineR(index: Int): String
  def getSetupBoard(): List[IPieces]


trait IGame:

  def movePieces(l1: Int, n1: Int, l2: Int, n2: Int):Unit
  override def toString: String
  def getBoardList(): List[IPieces]

trait IPieces:

  def getColor: Colors
  def getPiece: Chesspiece
  def getCords: (Int, Int)
  def toString: String
  def getIconPath: String


trait IPiecesFactory:
  def addPiece(chesspiece: Chesspiece, cords: (Int, Int), color: Colors): IPieces

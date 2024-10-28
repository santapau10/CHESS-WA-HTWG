package chess.models

import chess.models.game.{Chesspiece, Colors}
import chess.models.IPieces

import scala.xml.{Elem, Node}
import play.api.libs.json.{JsValue, Json, Reads, Writes}

trait IBoardBuilder:
  def promotePiece(chesspiece: Chesspiece, list: List[IPieces]): List[IPieces]
  def getSize(): Int
  def toJSON(): JsValue
  def toXml(): Elem
  def movePieces(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): List[IPieces]
  def checkFieldR(x: Int, y: Int, list: List[IPieces]): String
  def deletePiece (x1: Int, y1: Int, list: List[IPieces]): List[IPieces]
  def updateField(list: List[IPieces]): String
  def firstLineR(index: Int): String
  def getSetupBoard: List[IPieces]


trait IGame:
  def isKingInCheck(checklist: List[IPieces], colors: Colors): Boolean
  def isKingInCheckmate(checklist: List[IPieces], colors: Colors): Boolean
  def getBoard: IBoardBuilder
  override def toString: String
  def toStringOnline: String
  def getBoardList: List[IPieces]
  def toJson: JsValue
  def toXml: Elem

trait IPieces:
  def getLastCords: (Int, Int)
  def isMoved: Boolean
  def checkMove(x1: Int, y1: Int, x2: Int, y2: Int, list: List[IPieces]): Boolean
  def toJson: JsValue
  def toXml: Elem
  def getColor: Colors
  def getPiece: Chesspiece
  def getCords: (Int, Int)
  def toString: String
  def getIconPath: String


trait IPiecesFactory:
  def addPiece(chesspiece: Chesspiece, cords: (Int, Int), color: Colors, moved: Boolean, last_coords: (Int, Int)): IPieces

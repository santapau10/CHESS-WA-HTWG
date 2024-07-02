package chess.models.game

import chess.controller.*
import chess.models.{Board, IBoardBuilder, IGame, IPieces}
import chess.models.game.PiecesFactory
import chess.view.*
import chess.view.view.TUI
import play.api.libs.json.{JsValue, Json, Reads, Writes}

import scala.xml.{Elem, Node}

class Game(board: IBoardBuilder, list: List[IPieces]) extends IGame {

  private def toStringBoard: String = {
    board.updateField(list)
  }
  override def getBoard: IBoardBuilder = {
    board
  }
  override def toXml: Elem = {
    <game>
      <board>
        {board.toXml()}
      </board>
      <pieces>
        {list.map(_.toXml)}
      </pieces>
    </game>
  }

  override def toJson: JsValue = Json.obj(
    "game" -> Json.obj(
      "board" -> board.toJSON(),
      "pieces" -> Json.arr(list.map(_.toJson): _*)
    )
  )

  override def toString: String = {
    toStringBoard
  }

  override def getBoardList: List[IPieces] = {
    list
  }

  override def isKingInCheck(checklist: List[IPieces], colors: Colors): Boolean = {
    val king = checklist.find(p => p.getPiece == Chesspiece.KING && p.getColor == colors)
    
    val kingX = king.get.getCords._1
    val kingY = king.get.getCords._2
    
    checklist.exists(p => (p.getColor != checklist.find(k => k.getCords == (kingX, kingY)).get.getColor) && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, checklist))
  }

  override def isKingInCheckmate(checklist: List[IPieces],color: Colors): Boolean = {
    val king = checklist.find(p => p.getPiece == Chesspiece.KING && p.getColor == color)
    val kingX = king.get.getCords._1
    val kingY = king.get.getCords._2
    if (!isKingInCheck(checklist, color)) return false
    val size = board.getSize()

    for (dx <- -1 to 1; dy <- -1 to 1 if dx != 0 || dy != 0) {
      val newX = kingX + dx
      val newY = kingY + dy
      if (newX >= 0 && newX < size && newY >= 0 && newY < size && king.get.checkMove(kingX, kingY, newX, newY, checklist)) {
        val updatedList = checklist.filterNot(p => p.getCords == king.get.getCords).filterNot(p => p.getCords == (newX, newY))
        if (!isKingInCheck(updatedList, king.get.getColor)) return false
      }
    }
    val attacker = checklist.find(p => (p.getColor != checklist.find(k => k.getCords == (kingX, kingY)).get.getColor) && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, checklist))
    if (checklist.exists(p => p.checkMove(p.getCords._1, p.getCords._2, attacker.get.getCords._1, attacker.get.getCords._2, checklist))) return false
    true
  }

  def pathBetween(x1: Int, y1: Int, x2: Int, y2: Int): List[(Int, Int)] = {
    if (x1 == x2) {
      (math.min(y1, y2) + 1 until math.max(y1, y2)).map((x1, _)).toList
    } else if (y1 == y2) {
      (math.min(x1, x2) + 1 until math.max(x1, x2)).map((_, y1)).toList
    } else if (math.abs(x2 - x1) == math.abs(y2 - y1)) {
      val xStep = if (x2 > x1) 1 else -1
      val yStep = if (y2 > y1) 1 else -1
      (1 until math.abs(x2 - x1)).map(i => (x1 + i * xStep, y1 + i * yStep)).toList
    } else {
      List()
    }
  }

}

object Game {
  def fromXml(node: Node): IGame = {
    val board = (node \ "board").headOption.map(Board.fromXml).getOrElse(
      throw new IllegalArgumentException("Missing board in XML")
    )

    val pieces = (node \ "pieces").headOption.map(_.child.flatMap { pieceNode =>
      if (pieceNode.label.nonEmpty && (pieceNode \ "cords" \ "x").nonEmpty && (pieceNode \ "cords" \ "y").nonEmpty && (pieceNode \ "color").nonEmpty) {
        Some(PiecesFactory.fromXml(pieceNode))
      } else {
        None
      }
    }.toList).getOrElse(
      throw new IllegalArgumentException("Missing pieces in XML")
    )

    new Game(board, pieces)
  }

  def fromJson(json: JsValue): IGame = {
    val board = Board.fromJson((json \ "game" \ "board").as[JsValue])
    val pieces = (json \ "game" \ "pieces").as[Seq[JsValue]].map(PiecesFactory.fromJson).toList
    new Game(board, pieces)
  }
}
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

  override def isKingInCheck(kingX: Int, kingY: Int, list: List[IPieces]): Boolean = {
    list.exists(p => p.getColor != list.find(k => k.getCords == (kingX, kingY)).get.getColor && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, list))
  }

  override def isKingInCheckmate(kingX: Int, kingY: Int, list: List[IPieces]): Boolean = {
    if (!isKingInCheck(kingX, kingY, list)) return false

    // Überprüfen, ob der König auf ein angrenzendes Feld ziehen kann
    val kingMoves = for {
      x <- kingX - 1 to kingX + 1
      y <- kingY - 1 to kingY + 1
      if (x != kingX || y != kingY) && x >= 1 && x <= 8 && y >= 1 && y <= 8
    } yield (x, y)

    if (kingMoves.exists { case (x, y) =>
      val updatedList = list.filterNot(p => p.getCords == (kingX, kingY)) :+ new King((x, y), list.find(p => p.getCords == (kingX, kingY)).get.getColor, list.find(p => p.getCords == (kingX, kingY)).get.isMoved)
      !isKingInCheck(x, y, updatedList)
    }) return false

    val attackingPieces = list.filter(p => p.getColor != list.find(k => k.getCords == (kingX, kingY)).get.getColor && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, list))

    list.filter(p => p.getColor == list.find(k => k.getCords == (kingX, kingY)).get.getColor).exists { piece =>
      attackingPieces.exists { attacker =>
        val updatedList = list.filterNot(p => p.getCords == (attacker.getCords._1, attacker.getCords._2))
        piece.checkMove(piece.getCords._1, piece.getCords._2, attacker.getCords._1, attacker.getCords._2, updatedList) &&
          !isKingInCheck(kingX, kingY, updatedList) || {
          val pathToBlock = pathBetween(attacker.getCords._1, attacker.getCords._2, kingX, kingY)
          pathToBlock.exists { case (x, y) =>
            piece.checkMove(piece.getCords._1, piece.getCords._2, x, y, list) &&
              !isKingInCheck(kingX, kingY, list.filterNot(p => p.getCords == (x, y)) :+ piece)
          }
        }
      }
    }
  }

  def pathBetween(x1: Int, y1: Int, x2: Int, y2: Int): List[(Int, Int)] = {
    if (x1 == x2) { // Vertikale Bewegung
      (math.min(y1, y2) + 1 until math.max(y1, y2)).map((x1, _)).toList
    } else if (y1 == y2) { // Horizontale Bewegung
      (math.min(x1, x2) + 1 until math.max(x1, x2)).map((_, y1)).toList
    } else if (math.abs(x2 - x1) == math.abs(y2 - y1)) { // Diagonale Bewegung
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
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
    val foundking = checklist.find(p => p.getPiece == Chesspiece.KING && p.getColor == colors)

    foundking match
      case Some(king) =>
        val kingX = king.getCords._1
        val kingY = king.getCords._2

        checklist.exists(p => (p.getColor != checklist.find(k => k.getCords == (kingX, kingY)).get.getColor) && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, checklist))

      case None => false
  }

  override def isKingInCheckmate(checklist: List[IPieces], color: Colors): Boolean = {
    val king = checklist.find(p => p.getPiece == Chesspiece.KING && p.getColor == color)
    king match {
      case Some(k) =>
        val kingX = k.getCords._1
        val kingY = k.getCords._2
        if (!isKingInCheck(checklist, color)) {
          false
        } else {
          val size = board.getSize()

          // Prüfen, ob der König sich in ein sicheres Feld bewegen kann
          val canKingEscape = (-1 to 1).exists { dx =>
            (-1 to 1).exists { dy =>
              if (dx != 0 || dy != 0) {
                val newX = kingX + dx
                val newY = kingY + dy
                if (newX >= 0 && newX < size && newY >= 0 && newY < size && k.checkMove(kingX, kingY, newX, newY, checklist)) {
                  val updatedList = checklist.filterNot(p => p.getCords == k.getCords).filterNot(p => p.getCords == (newX, newY))
                  !isKingInCheck(updatedList, k.getColor)
                } else {
                  false
                }
              } else {
                false
              }
            }
          }

          if (canKingEscape) {
            false
          } else {
            // Prüfen, ob eine andere Figur den König decken kann
            val attacker = checklist.find(p =>
              p.getColor != k.getColor && p.checkMove(p.getCords._1, p.getCords._2, kingX, kingY, checklist)
            )

            attacker match {
              case Some(attackerPiece) =>
                !checklist.exists { piece =>
                  piece.getColor == color && (0 until size).exists { dx =>
                    (0 until size).exists { dy =>
                      if (piece.checkMove(piece.getCords._1, piece.getCords._2, dx, dy, checklist)) {
                        val updatedList = checklist
                          .filterNot(p => p.getCords == piece.getCords)
                          .filterNot(p => p.getCords == (dx, dy)) :+
                          PiecesFactory().addPiece(piece.getPiece, (dx, dy), piece.getColor, piece.isMoved, piece.getLastCords)
                        !isKingInCheck(updatedList, k.getColor)
                      } else {
                        false
                      }
                    }
                  }
                }
              case None => false // Kein Angreifer gefunden, sollte nicht passieren, weil isKingInCheck true war
            }
          }
        }
      case None => false
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
  
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
}

object Game {
  def fromXml(node: Node): IGame = {
    val board = (node \ "board").headOption.map(Board.fromXml).get
    val pieces = (node \ "pieces" \ "piece").map(PiecesFactory.fromXml).toList
    new Game(board, pieces)
  }

  def fromJson(json: JsValue): IGame = {
    val board = Board.fromJson((json \ "game" \ "board").as[JsValue])
    val pieces = (json \ "game" \ "pieces").as[Seq[JsValue]].map(PiecesFactory.fromJson).toList
    new Game(board, pieces)
  }
}
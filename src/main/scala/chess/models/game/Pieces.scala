package chess.models.game


import play.api.libs.json.{JsValue, Json}
import scala.xml.{Elem, Node}

import chess.models.*

enum Colors:
  case WHITE, BLACK

enum Chesspiece:
  case KING, PAWN, QUEEN, BISHOP, KNIGHT, ROOK

class PiecesFactory extends IPiecesFactory {
  override def addPiece(chesspiece: Chesspiece, cords: (Int, Int), color: Colors): IPieces = chesspiece match {
    case Chesspiece.PAWN => new Pawn(cords, color)
    case Chesspiece.KNIGHT => new Knight(cords, color)
    case Chesspiece.BISHOP => new Bishop(cords, color)
    case Chesspiece.ROOK => new Rook(cords, color)
    case Chesspiece.QUEEN => new Queen(cords, color)
    case Chesspiece.KING => new King(cords, color)
  }
}

object PiecesFactory {

  def fromXml(xml: Node): IPieces = {
    val pieceType = xml.label.toUpperCase match {
      case "PAWN" => Chesspiece.PAWN
      case "KNIGHT" => Chesspiece.KNIGHT
      case "BISHOP" => Chesspiece.BISHOP
      case "ROOK" => Chesspiece.ROOK
      case "QUEEN" => Chesspiece.QUEEN
      case "KING" => Chesspiece.KING
      case _ => throw new IllegalArgumentException("Invalid piece type in XML")
    }

    val cords = ((xml \ "cords" \ "x").headOption, (xml \ "cords" \ "y").headOption) match {
      case (Some(xNode), Some(yNode)) => (xNode.text.toInt, yNode.text.toInt)
      case _ => throw new IllegalArgumentException("Missing or invalid coordinates in XML")
    }

    val colorStr = (xml \ "color").headOption.map(_.text.trim.toUpperCase)
      .getOrElse(throw new IllegalArgumentException("Missing or invalid color in XML"))

    val color = colorStr match {
      case "WHITE" => Colors.WHITE
      case "BLACK" => Colors.BLACK
      case _ => throw new IllegalArgumentException("Invalid color in XML")
    }

    new PiecesFactory().addPiece(pieceType, cords, color)
  }


  def fromJson(json: JsValue): IPieces = {
    val pieceType = (json \ "piece").asOpt[String].map(_.toUpperCase)
      .getOrElse(throw new IllegalArgumentException("Missing or invalid piece type in JSON"))

    val cords = ((json \ "cords" \ "x").asOpt[Int], (json \ "cords" \ "y").asOpt[Int]) match {
      case (Some(x), Some(y)) => (x, y)
      case _ => throw new IllegalArgumentException("Missing or invalid coordinates in JSON")
    }

    val colorStr = (json \ "color").asOpt[String].map(_.toUpperCase)
      .getOrElse(throw new IllegalArgumentException("Missing or invalid color in JSON"))

    val color = colorStr match {
      case "WHITE" => Colors.WHITE
      case "BLACK" => Colors.BLACK
      case _ => throw new IllegalArgumentException("Invalid color in JSON")
    }

    pieceType match {
      case "PAWN" => new Pawn(cords, color)
      case "KNIGHT" => new Knight(cords, color)
      case "BISHOP" => new Bishop(cords, color)
      case "ROOK" => new Rook(cords, color)
      case "QUEEN" => new Queen(cords, color)
      case "KING" => new King(cords, color)
      case _ => throw new IllegalArgumentException("Invalid piece type in JSON")
    }
  }

}




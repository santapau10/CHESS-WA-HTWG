package chess.models.game


import play.api.libs.json.{JsValue, Json}
import scala.xml.{Elem, Node}

import chess.models.*

enum Colors:
  case WHITE, BLACK

enum Chesspiece:
  case KING, PAWN, QUEEN, BISHOP, KNIGHT, ROOK

class PiecesFactory() extends IPiecesFactory {
  override def addPiece(chesspiece: Chesspiece, cords: (Int, Int), color: Colors, moved: Boolean, last_coords: (Int, Int)): IPieces = chesspiece match {
    case Chesspiece.PAWN => new Pawn(cords, color, moved, last_coords)
    case Chesspiece.KNIGHT => new Knight(cords, color, moved, last_coords)
    case Chesspiece.BISHOP => new Bishop(cords, color, moved, last_coords)
    case Chesspiece.ROOK => new Rook(cords, color, moved, last_coords)
    case Chesspiece.QUEEN => new Queen(cords, color, moved, last_coords)
    case Chesspiece.KING => new King(cords, color, moved, last_coords)
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
      case (Some(xNode), Some(yNode)) => (xNode.text.trim.toInt, yNode.text.trim.toInt)
      case _ => throw new IllegalArgumentException("Missing or invalid coordinates in XML")
    }

    val colorStr = (xml \ "color").headOption.map(_.text.trim.toUpperCase)
      .getOrElse(throw new IllegalArgumentException("Missing or invalid color in XML"))

    val movedStr = (xml \ "moved").headOption.map(_.text.trim)
      .getOrElse(throw new IllegalArgumentException("Missing or invalid color in XML"))

    val moved = movedStr match {
      case "true" => true
      case "false" => false
      case _ => throw new IllegalArgumentException("Invalid Boolean: moved in XML")
    }

    val color = colorStr match {
      case "WHITE" => Colors.WHITE
      case "BLACK" => Colors.BLACK
      case _ => throw new IllegalArgumentException("Invalid color in XML")
    }
    val lastcords = ((xml \ "lastcords" \ "x").headOption, (xml \ "cords" \ "y").headOption) match {
      case (Some(xNode), Some(yNode)) => (xNode.text.trim.toInt, yNode.text.trim.toInt)
      case _ => throw new IllegalArgumentException("Missing or invalid coordinates in XML")
    }

    new PiecesFactory().addPiece(pieceType, cords, color, moved, lastcords)
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

    val movedStr = (json \ "moved").asOpt[String]
      .getOrElse(throw new IllegalArgumentException("Missing or invalid color in XML"))

    val moved = movedStr match {
      case "true" => true
      case "false" => false
      case _ => throw new IllegalArgumentException("Invalid Boolean: moved in JSON")
    }
    

    val color = colorStr match {
      case "WHITE" => Colors.WHITE
      case "BLACK" => Colors.BLACK
      case _ => throw new IllegalArgumentException("Invalid color in JSON")
    }
    val lastcords = ((json \ "lastcords" \ "x").asOpt[Int], (json \ "lastcords" \ "y").asOpt[Int]) match {
      case (Some(x), Some(y)) => (x, y)
      case _ => throw new IllegalArgumentException("Missing or invalid coordinates in JSON")
    }
    

    pieceType match {
      case "PAWN" => new Pawn(cords, color, moved, lastcords)
      case "KNIGHT" => new Knight(cords, color, moved, lastcords)
      case "BISHOP" => new Bishop(cords, color, moved, lastcords)
      case "ROOK" => new Rook(cords, color, moved, lastcords)
      case "QUEEN" => new Queen(cords, color, moved, lastcords)
      case "KING" => new King(cords, color, moved, lastcords)
      case _ => throw new IllegalArgumentException("Invalid piece type in JSON")
    }
  }
}




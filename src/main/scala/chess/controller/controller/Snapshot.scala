package chess.controller.controller

import chess.controller.{ISnapshot, IState, IController}
import chess.models.IGame
import chess.models.game.Game

import play.api.libs.json.{JsValue, Json}
import java.security.MessageDigest
import scala.xml.{Elem, Node}
class Snapshot(game: IGame, state: IState) extends ISnapshot {
  override def toXml: Elem =
    <snapshot>
      {game.toXml}{state.toXml}
    </snapshot>

  override def toJson: JsValue = Json.obj(
    "game" -> game.toJson,
    "state" -> state.toJson
  )
  override def getState: IState ={this.state}
  override def getGame: IGame ={this.game}
}

object Snapshot:
  def fromXml(node: Node, controller: IController): Snapshot =
    val game = Game.fromXml((node \ "game").head)
    val state = (node \ "state").text match
      case "PreGameState" => PreGameState(controller)
      case "TurnStateWhite" => TurnStateWhite(controller)
      case "TurnStateBlack" => TurnStateBlack(controller)
      case _ => throw IllegalArgumentException("Invalid state")
    Snapshot(game, state)

  def fromJson(json: JsValue, controller: IController): Snapshot =
    val game = Game.fromJson((json \ "game").get)
    val state = (json \ "state").as[String] match
      case "PreGameState" => PreGameState(controller)
      case "TurnStateWhite" => TurnStateWhite(controller)
      case "TurnStateBlack" => TurnStateBlack(controller)
      case _ => throw IllegalArgumentException("Invalid state")
    Snapshot(game, state)

  def hash(input: String): String =
    val md = MessageDigest.getInstance("SHA-256")
    val hashBytes = md.digest(input.getBytes("UTF-8"))
    hashBytes.map("%02x".format(_)).mkString
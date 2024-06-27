package chess.controller.controller

import chess.controller.*
import play.api.libs.json.{JsValue, Json}

import scala.xml.{Node, Utility}

class Command(controller: IController) extends ICommand:
  private var snapshot: Option[ISnapshot] = None

  override def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)

  override def undo(): Unit = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  override def execute(): Unit = ()

class MovePiecesCommand(controller: IController, l1: Int, n1: Int, l2: Int, n2: Int) extends Command(controller):
  override def execute(): Unit = {
    controller.movePieces(l1, n1, l2, n2)
  }

class ChangeStateCommand(state: IState, c: IController) extends Command(c):
  override def execute(): Unit = {
    c.changeState(state)
  }

case class LoadXmlCommand(controller: IController, node: Node) extends Command(controller):

  override def execute(): Unit =
    val hash = (node \ "hash").text
    val xmlSnapshot = (node \ "snapshot").head
    if hash == " " + Snapshot.hash(Utility.trim(xmlSnapshot).toString) + " " then // neu = alt -> ladet
      controller.restoreSnapshot(Snapshot.fromXml(xmlSnapshot, controller))
    else
      println(hash)
      println(Snapshot.hash(Utility.trim(xmlSnapshot).toString))
      println("Invalid XML progress file!")

case class LoadJsonCommand(controller: IController, json: JsValue) extends Command(controller):

  override def execute(): Unit =
    val hash = (json \ "hash").get.as[String]
    val jsonSnapshot = (json \ "snapshot").get
    if hash == Snapshot.hash(Json.stringify(jsonSnapshot)) then
      controller.restoreSnapshot(Snapshot.fromJson(jsonSnapshot, controller))
    else
      println("Invalid JSON progress file!")
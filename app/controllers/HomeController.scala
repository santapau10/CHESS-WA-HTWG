package controllers

import javax.inject._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import chess.view.view.TUI
import chess.controller._
import chess.module._
import com.google.inject.Guice
import com.google.inject.Injector
import chess.controller.controller._
import org.apache.pekko.actor._
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
import org.apache.pekko.actor.ActorRef
import chess.util.Observer
import chess.util.Event
import play.libs.Json

@Singleton
class HomeController @Inject() (
                                 val controllerComponents: ControllerComponents
                               )(implicit system: ActorSystem, mat: Materializer)
  extends BaseController
    with Observer {

  val injector = Guice.createInjector(new ChessModule)
  private val controller = injector.getInstance(classOf[IController])
  private val tui = new TUI(controller)
  var websocketActor: ActorRef = null;
  var websocketActors: List[ActorRef] = List() // Lista de actores WebSocket

  /** Action to render the homepage */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("Homepage"))
  }

  /** Action to render the rules page */
  def rules() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.rules())
  }

  /** Action to start the game */
  def start() = Action { implicit request: Request[AnyContent] =>
    val output = controller.boardToString()
    println(output)
    controller.changeState(TurnStateWhite(controller))
    Ok(views.html.chesshtml(controller))
  }

  def moveLink(origin: String) = Action {
    implicit request: Request[AnyContent] =>
      controller.handleAction(
        controller.getCurrentState.actionFromInput(origin)
      )
      Ok(views.html.chesshtml(controller))
  }

  def move() = Action { implicit request: Request[AnyContent] =>
    request.body.asJson match {
      case Some(json) =>
        val origin = (json \ "origin").as[String]
        controller.handleAction(
          controller.getCurrentState.actionFromInput(origin)
        )
        Ok(views.html.chesshtml(controller))
      case None =>
        BadRequest("Invalid JSON data")
    }
  }

  def jsonGame() = Action { implicit request: Request[AnyContent] =>
    Ok(controller.getGame.toJson)
  }

  controller.add(this)

  /** WebSocket endpoint */
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      println("WebSocket connection received")
      websocketActors = out :: websocketActors
      MyWebSocketActor.props(out)
    }
  }

  /** Observer callback for game state changes */
  override def update(event: Event): Unit = {
    event match {
      case Event.STATE_CHANGED =>
        if (websocketActors != null) {
          websocketActors.foreach { actor =>
            actor ! controller.getGame.toJson.toString()
          }
        }
      case _ =>
    }
  }

  /** WebSocket actor to handle incoming messages */
  object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out))
  }

  class MyWebSocketActor(out: ActorRef) extends Actor {
    def receive = {
      case msg: String =>
        // Handle the start message
        if (msg == "start") {
          controller.changeState(TurnStateWhite(controller))  // Start the game
          out ! "Game Started"  // Notify the client
        } else {
          out ! ("I received your message: " + msg)  // Handle other messages
        }
      case msg: JsValue =>
        // Handle JSON messages
        println(msg)
        out ! msg.toString()  // Send the JSON back to the client
    }
  }
}

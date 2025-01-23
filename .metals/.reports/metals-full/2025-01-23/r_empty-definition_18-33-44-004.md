error id: `<none>`.
file://<WORKSPACE>/app/controllers/HomeController.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
package controllers

import javax.inject.*
import play.api.*
import play.api.libs.json._
import play.api.mvc.*
import chess.view.view.TUI
import chess.controller.*
import chess.module.*
import com.google.inject.Guice
import com.google.inject.Injector
<<<<<<< HEAD
import chess.controller.controller._
import org.apache.pekko.actor._
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
import org.apache.pekko.actor.ActorRef
import chess.util.Observer
import chess.util.Event
import org.apache.pekko.actor.TypedActor.self
import play.libs.Json

@Singleton
class HomeController @Inject() (
                                 val controllerComponents: ControllerComponents,
                                 implicit val system: ActorSystem,
                                 implicit val mat: Materializer
                               ) extends BaseController
  with Observer {

  val injector = Guice.createInjector(new ChessModule)
  private var controller = injector.getInstance(classOf[IController])
  private val tui = new TUI(controller)
  var websocketActors: List[ActorRef] = List() // Liste der WebSocket-Actors
=======
import chess.controller.controller.*
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {
  val injector = Guice.createInjector(new ChessModule)
  private val controller = injector.getInstance(classOf[IController])
>>>>>>> master

  private val tui = new TUI(controller)

  /** Create an Action to render an HTML page. */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("Homepage"))
  }

  /** Create an Action to render an HTML page. */
  def rules() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.rules())
  }

  /** Create an Action to render an HTML page. */
  def start() = Action { implicit request: Request[AnyContent] =>
    val output = controller.boardToString()
    print(output)
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

<<<<<<< HEAD
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
      MyWebSocketActor.props(out, self)
    }
  }

  /** Observer callback for game state changes */
  override def update(event: Event): Unit = {
    event match {
      case Event.STATE_CHANGED =>
        if (websocketActors.nonEmpty) {
          websocketActors.foreach { actor =>
            actor ! controller.getGame.toJson.toString()
          }
        }
      case _ =>
    }
  }

  /** WebSocket actor to handle incoming messages */
  object MyWebSocketActor {
    def props(out: ActorRef, parent: ActorRef) = Props(new MyWebSocketActor(out, parent))
  }

  class MyWebSocketActor(out: ActorRef, parent: ActorRef) extends Actor {
    def receive = {
      case msg: String =>
        if (msg == "start") {
          controller.changeState(TurnStateWhite(controller))
          out ! "Game Started"
        } else {
          out ! ("I received your message: " + msg)
        }
      case msg: JsValue =>
        println(msg)
        out ! msg.toString()
    }

    override def postStop(): Unit = {
      websocketActors = websocketActors.filterNot(_ == out) // Actor entfernen
      if (websocketActors.isEmpty) {
        println("No WebSocket connections remaining. Resetting controller.")
        resetController()
      }
    }
=======
  def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
>>>>>>> master
  }

  /** Resets the controller state */
  private def resetController(): Unit = {
    controller.initGame()
    println("Controller has been reset.")
  }
}

```

#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.
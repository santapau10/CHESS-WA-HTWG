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
import chess.controller.controller.*
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {
  val injector = Guice.createInjector(new ChessModule)
  private val controller = injector.getInstance(classOf[IController])

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

  def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }
}

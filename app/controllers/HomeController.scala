package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import chess.view.view.TUI
import chess.controller.IController
import chess.module._
import com.google.inject.Guice
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  /** Create an Action to render an HTML page. */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /** Create an Action to render an HTML page. */
  def chessBoard() = Action { implicit request: Request[AnyContent] =>
    // Capturar la salida de TUI usando el nuevo método
    val injector = Guice.createInjector(new ChessModule)
    val controller = injector.getInstance(classOf[IController])
    val tui = new TUI(controller)
    println("hola")
    val output = tui.getCurrentState
    Ok(views.html.chess(output)) // Pasar el resultado a la vista
  }

  def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }
}

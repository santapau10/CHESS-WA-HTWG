package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import chess.view.view.TUI
import chess.controller.IController
import chess.module._
import com.google.inject.Guice
import com.google.inject.Injector
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {
  val injector = Guice.createInjector(new ChessModule)
  private val controller = injector.getInstance(classOf[IController])

  private val tui = new TUI(controller)

  /** Create an Action to render an HTML page. */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /** Create an Action to render an HTML page. */
  def chessBoard() = Action { implicit request: Request[AnyContent] =>
    val output = tui.getCurrentState
    Ok(views.html.chess(output)) // Pasar el resultado a la vista
  }

  def move() = Action { implicit request: Request[AnyContent] =>

    // you get the coordinates of the pieces you want to move
    val postData = request.body.asFormUrlEncoded

    val originX = postData.get("originX")
    val originY = postData.get("originY")
    val destinationX = postData.get("destinationX")
    val destinationY = postData.get("destinationY")

    Ok("Good response")
  }

  def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }
}

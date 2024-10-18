package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import chess.view.view.TUI
import chess.controller.IController
import chess.module._
import com.google.inject.Guice
import com.google.inject.Injector
import chess.controller.controller
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
  def rules() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.rules())
  }

  /** Create an Action to render an HTML page. */
  def chessBoard() = Action { implicit request: Request[AnyContent] =>
    val output = tui.getCurrentState
    Ok(views.html.chess(output))
  }

  def move() = Action { implicit request: Request[AnyContent] =>
    request.body.asJson match {
      case Some(json) =>
        val originX =
          (json \ "originX").asOpt[String].flatMap(_.headOption.map(_.toLower))
        val originY = (json \ "originY").asOpt[Int]
        val destinationX = (json \ "destinationX")
          .asOpt[String]
          .flatMap(_.headOption.map(_.toLower))
        val destinationY = (json \ "destinationY").asOpt[Int]

        val originXAscii = originX.map(_.toInt - 97)
        val adjustedOriginY = originY.map(_ - 1)

        val destinationXAscii =
          destinationX.map(_.toInt - 97)
        val adjustedDestinationY =
          destinationY.map(_ - 1)

        (
          originXAscii,
          adjustedOriginY,
          destinationXAscii,
          adjustedDestinationY
        ) match {
          case (Some(ox), Some(oy), Some(dx), Some(dy)) =>
            controller.movePieces(ox, oy, dx, dy)
            Ok("Good response")
          case _ =>
            BadRequest("Invalid or missing coordinates")
        }

      case None =>
        BadRequest("Expected JSON data")
    }
  }
  def moveLink(origin: String, destination: String) = Action {
    implicit request: Request[AnyContent] =>
      val originX = origin.charAt(0).toInt - 97
      val originY: Int = origin.charAt(1).asDigit - 1
      val destinationX = destination.charAt(0).toInt - 97
      val destinationY: Int = destination.charAt(1).asDigit - 1
      controller.movePieces(originX, originY, destinationX, destinationY)
      val output = tui.getCurrentState
      Ok(views.html.chess(output))
  }

  def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }
}

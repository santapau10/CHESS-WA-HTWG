package module

import chess.controller.IController
import chess.controller.controller.Controller
import chess.module.ChessModule
import com.google.inject.{Guice, Injector}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ChessModuleSpec extends AnyWordSpec with Matchers {

  "A ChessModule" should {
    "bind IController to Controller" in {
      val injector: Injector = Guice.createInjector(new ChessModule)
      val controller: IController = injector.getInstance(classOf[IController])
      controller shouldBe a[Controller]
    }

    "bind Integer to 8" in {
      val injector: Injector = Guice.createInjector(new ChessModule)
      val boardSize: Integer = injector.getInstance(classOf[Integer])
      boardSize shouldEqual 8
    }
  }
}

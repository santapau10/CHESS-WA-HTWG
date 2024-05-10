package models

import chess.models.Coordinates
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.wordspec.AnyWordSpec

class CoordinatesSpec extends AnyWordSpec {

  "The coordinates object" when {
    "setCords method is called" should {
      "return a new coordinates object with the specified x and y values" in {
        val x = 3
        val y = 4
        val cords = Coordinates.setCords(x, y)
        cords.x shouldBe x
        cords.y shouldBe y
      }
    }
  }
}

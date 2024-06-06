package chess.models
object Coordinates {
  def setCords(x: Int, y: Int): Coordinates = new Coordinates(x, y)


}
class Coordinates(val x: Int, val y: Int):
  override def toString: String = {
    x.toString + ", " + y.toString
  }
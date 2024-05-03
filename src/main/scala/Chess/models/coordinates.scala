package Chess.models
object coordinates {
  def setCords(x: Int, y: Int): coordinates = new coordinates(x, y)
}
class coordinates(val x: Int, val y: Int)
package chess.models

object Coordinates {
  def setCords(x: Int, y: Int): Coordinates = new Coordinates(x, y)
}

class Coordinates(val x: Int, val y: Int) {
  override def toString: String = {
    x.toString + ", " + y.toString
  }

  override def equals(obj: Any): Boolean = obj match {
    case that: Coordinates => this.x == that.x && this.y == that.y
    case _ => false
  }

  override def hashCode(): Int = {
    31 * x + y
  }
}

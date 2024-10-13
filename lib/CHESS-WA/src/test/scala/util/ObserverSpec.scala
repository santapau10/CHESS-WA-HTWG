package util

import chess.util._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.collection.mutable.ListBuffer

class ObservableSpec extends AnyWordSpec with Matchers {

  // Concrete observer for testing
  class TestObserver extends Observer {
    val eventsReceived: ListBuffer[Event] = ListBuffer()

    def update(event: Event): Unit = {
      eventsReceived += event
    }
  }

  // Sample observable instance for testing
  class TestObservable extends Observable {}

  "Observable" should {
    "add and notify observers correctly" in {
      val observer = new TestObserver
      val observable = new TestObservable

      observable.add(observer)
      observable.notifyObservers(Event.BOARD_CHANGED)

      observer.eventsReceived should contain only Event.BOARD_CHANGED
    }

    "remove observers correctly" in {
      val observer = new TestObserver
      val observable = new TestObservable

      observable.add(observer)
      observable.remove(observer)
      observable.notifyObservers(Event.BOARD_CHANGED)

      observer.eventsReceived shouldBe empty
    }

    "not notify removed observers" in {
      val observer1 = new TestObserver
      val observer2 = new TestObserver
      val observable = new TestObservable

      observable.add(observer1)
      observable.add(observer2)
      observable.remove(observer1)
      observable.notifyObservers(Event.BOARD_CHANGED)

      observer1.eventsReceived shouldBe empty
      observer2.eventsReceived should contain only Event.BOARD_CHANGED
    }

    "handle multiple subscribers correctly" in {
      val observer1 = new TestObserver
      val observer2 = new TestObserver
      val observable = new TestObservable

      observable.add(observer1)
      observable.add(observer2)
      observable.notifyObservers(Event.BOARD_CHANGED)

      observer1.eventsReceived should contain only Event.BOARD_CHANGED
      observer2.eventsReceived should contain only Event.BOARD_CHANGED
    }
  }
}

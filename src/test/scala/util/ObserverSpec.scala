package util

import scala.compiletime.uninitialized
import org.scalatest.wordspec.AnyWordSpec
import chess.util.{Event, Observable, Observer}

class ObserverSpec extends AnyWordSpec {

  "An Observable" when {

    "subscribed to by an Observer" should {

      "add the Observer to its subscribers" in {
        val observable = new TestObservable()
        val observer = new TestObserver()

        observable.add(observer)

        assert(observable.subscribers.contains(observer))
      }
    }

    "unsubscribed from by an Observer" should {

      "remove the Observer from its subscribers" in {
        val observable = new TestObservable()
        val observer = new TestObserver()

        observable.add(observer)
        observable.remove(observer)

        assert(!observable.subscribers.contains(observer))
      }
    }

    "notified of an event" should {

      "notify all its subscribers" in {
        val observable = new TestObservable()
        val observer1 = new TestObserver()
        val observer2 = new TestObserver()

        observable.add(observer1)
        observable.add(observer2)

        observable.notifyObservers(Event.BOARD_CHANGED)

        assert(observer1.lastEvent == Event.BOARD_CHANGED)
        assert(observer2.lastEvent == Event.BOARD_CHANGED)
      }
    }
  }
}

class TestObservable extends Observable

class TestObserver extends Observer {
  var lastEvent: Event = uninitialized

  override def update(event: Event): Unit = {
    lastEvent = event
  }
}

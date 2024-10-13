package controller.controller

import chess.controller.ICommand
import chess.controller.controller.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers {

  class TestCommand extends ICommand:
    var executed = false
    var undone = false
    var snapshotSaved = false

    override def execute(): Unit = executed = true

    override def undo(): Unit = undone = true

    override def saveSnapshot(): Unit = snapshotSaved = true

  "An UndoManager" should {

    "execute a command and save its snapshot" in {
      val manager = UndoManager()
      val command = TestCommand()

      manager.executeCommand(command)

      command.executed shouldBe true
      command.snapshotSaved shouldBe true
      manager.canUndo shouldBe true
      manager.canRedo shouldBe false
    }

    "undo a command" in {
      val manager = UndoManager()
      val command = TestCommand()

      manager.executeCommand(command)
      manager.undoCommand()

      command.undone shouldBe true
      manager.canUndo shouldBe false
      manager.canRedo shouldBe true
    }

    "redo a command" in {
      val manager = UndoManager()
      val command = TestCommand()

      manager.executeCommand(command)
      manager.undoCommand()
      command.executed = false // reset for test
      manager.redoCommand()

      command.executed shouldBe true
      manager.canUndo shouldBe true
      manager.canRedo shouldBe false
    }

    "not undo if there are no commands" in {
      val manager = UndoManager()

      manager.undoCommand() // should print "Undo not possible"
      manager.canUndo shouldBe false
    }

    "not redo if there are no commands" in {
      val manager = UndoManager()

      manager.redoCommand() // should print "Redo not possible"
      manager.canRedo shouldBe false
    }
  }
}

package chess.controller.controller

import chess.controller.{ICommand, IUndoManager}

class UndoManager extends IUndoManager:

  private var undoStack: List[ICommand] = List()
  private var redoStack: List[ICommand] = List()

  override def executeCommand(command: ICommand): Unit =
    undoStack = command :: undoStack
    redoStack = List()
    command.saveSnapshot()
    command.execute()

  override def canUndo: Boolean = undoStack.nonEmpty

  override def undoCommand(): Unit =
    undoStack match
      case head :: stack =>
        redoStack = head :: redoStack
        undoStack = stack
        head.undo()
      case _ =>
        println("Undo not possible")

  override def canRedo: Boolean = redoStack.nonEmpty

  override def redoCommand(): Unit =
    redoStack match
      case head :: stack =>
        undoStack = head :: undoStack
        redoStack = stack
        head.execute()
      case _ =>
        println("Redo not possible")

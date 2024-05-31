package chess.controller

class UndoManager:

  private var undoStack: List[Command] = List()
  private var redoStack: List[Command] = List()

  def executeCommand(command: Command): Unit =
    undoStack = command :: undoStack
    redoStack = List()
    command.saveSnapshot()
    command.execute()

  def canUndo: Boolean = undoStack.nonEmpty

  def undoCommand(): Unit =
    undoStack match
      case head :: stack =>
        redoStack = head :: redoStack
        undoStack = stack
        head.undo()
      case _ =>
        println("Undo not possible")
  def canRedo: Boolean = redoStack.nonEmpty

  def redoCommand(): Unit =
    redoStack match
      case head :: stack =>
        undoStack = head :: undoStack
        redoStack = stack
        head.execute()
      case _ =>
        println("Redo not possible")

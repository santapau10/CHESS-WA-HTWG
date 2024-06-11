package chess.controller.controller

import chess.controller.*

class Command(controller: IController) extends ICommand:
  private var snapshot: Option[ISnapshot] = None

  override def saveSnapshot(): Unit = snapshot = Some(controller.snapshot)

  override def undo(): Unit = snapshot match
    case Some(snapshot) => controller.restoreSnapshot(snapshot)
    case None => throw IllegalStateException("No snapshot to restore")

  override def execute(): Unit = ()

class MovePiecesCommand(controller: IController, l1: Int, n1: Int, l2: Int, n2: Int) extends Command(controller):
  override def execute(): Unit = {
    controller.movePieces(l1, n1, l2, n2)
  }

class ChangeStateCommand(state: IState, c: IController) extends Command(c):
  override def execute(): Unit = {
    c.changeState(state)
  }
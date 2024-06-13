package chess.controller.controller

import chess.controller.{ISnapshot, IState}
import chess.models.IGame


class Snapshot(game: IGame, state: IState) extends ISnapshot {
  override def getState: IState ={this.state}
  override def getGame: IGame ={this.game}
}
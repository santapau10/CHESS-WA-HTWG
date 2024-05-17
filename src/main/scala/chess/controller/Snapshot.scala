package chess.controller

import chess.models.Game

class Snapshot(game: Game, state: State) {
  def getState(): State ={this.state}
  def getGame(): Game ={this.game}
}
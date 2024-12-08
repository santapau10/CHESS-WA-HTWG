<template>
  <div class="chess-board">
    <div v-for="(row, rowIndex) in board"
         :key="`row-${rowIndex}`"
         class="chess-row">
      <div v-for="(cell, colIndex) in row"
           :key="`cell-${rowIndex}-${colIndex}`"
           :class="getCellClass(rowIndex, colIndex)"
           :data-coords="`${rowIndex},${colIndex}`"
           class="chess-cell"
           @click="handleCellClick(rowIndex, colIndex)">
        <img v-if="cell.piece"
             :src="getPieceImage(cell)"
             :alt="cell.piece"
             class="piece" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChessBoard',

  data() {
    return {
      board: [], // 8x8 board representation
      basePath: '', // Will be set via props or environment variable
      websocket: null
    }
  },

  props: {
    baseImagePath: {
      type: String,
      required: true
    },
    wsUrl: {
      type: String,
      required: true,
      default: 'ws://localhost:9000/websocket'
    }
  },

  created() {
    this.basePath = this.baseImagePath
    this.initializeBoard()
    this.connectWebSocket()
  },

  destroyed() {
    if (this.websocket) {
      this.websocket.close()
    }
  },

  methods: {
    initializeBoard() {
      fetch('/jsonGame')
          .then(response => response.json())
          .then(data => {
            this.board = this.renderBoard(data.game.pieces)
          })
          .catch(error => {
            console.error('Error loading the board:', error)
            this.$emit('board-error', error)
          })
    },

    renderBoard(pieces) {
      const board = Array.from({ length: 8 }, () =>
          Array(8).fill({})
      )

      pieces.forEach(piece => {
        const { x, y } = piece.cords
        board[x][y] = {
          piece: piece.piece,
          color: piece.color.toLowerCase().trim()
        }
      })

      return board
    },

    getCellClass(rowIndex, colIndex) {
      return {
        'chess-cell': true,
        'dark-cell': (rowIndex + colIndex) % 2 === 0,
        'light-cell': (rowIndex + colIndex) % 2 !== 0
      }
    },

    getPieceImage(cell) {
      if (cell.piece && cell.color) {
        const pieceName = cell.piece.charAt(0).toUpperCase() +
            cell.piece.slice(1).toLowerCase()
        return `${this.basePath}/${cell.color}/${pieceName}.png`
      }
      return ''
    },

    handleCellClick(rowIndex, colIndex) {
      const chessNotation = this.convertToChessNotation(rowIndex, colIndex)
      this.sendMove(chessNotation)
      this.$emit('cell-click', { row: rowIndex, col: colIndex, notation: chessNotation })
    },

    convertToChessNotation(row, col) {
      const file = String.fromCharCode('a'.charCodeAt(0) + row)
      const rank = (col + 1).toString()
      return file + rank
    },

    sendMove(notation) {
      fetch('/move/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ origin: notation })
      })
          .catch(error => {
            console.error('Error sending move:', error)
            this.$emit('move-error', error)
          })
    },

    connectWebSocket() {
      this.websocket = new WebSocket(this.wsUrl)

      this.websocket.onopen = () => {
        console.log('WebSocket connected')
        this.$emit('ws-connected')
      }

      this.websocket.onclose = () => {
        console.log('WebSocket connection closed')
        this.$emit('ws-closed')
      }

      this.websocket.onerror = (error) => {
        console.error('WebSocket error:', error)
        this.$emit('ws-error', error)
      }

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)
          if (data.game?.pieces) {
            this.board = this.renderBoard(data.game.pieces)
            this.$emit('board-updated', data)
          }
        } catch (error) {
          console.error('Error processing WebSocket message:', error)
          this.$emit('ws-message-error', error)
        }
      }
    }
  }
}
</script>
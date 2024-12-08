<template>
  <div id="app">
    <ChessBoard
        baseImagePath="/assets/chess"
        wsUrl="ws://localhost:9000/websocket"
        @cell-click="handleMove"
        @board-updated="handleBoardUpdate"
        @ws-error="handleWebSocketError"
    />
  </div>
</template>

<script>
import ChessBoard from './chess.vue'

export default {
  name: 'App',
  components: {
    ChessBoard
  },
  methods: {
    handleMove({ row, col, notation }) {
      console.log(`Move made at row ${row}, col ${col}, notation: ${notation}`)
    },
    handleBoardUpdate(gameState) {
      console.log('Board updated:', gameState)
    },
    handleWebSocketError(error) {
      console.error('WebSocket error:', error)
    }
  }
}
</script>
<template>
  <div class="chess-board">
    <div v-for="(row, rowIndex) in board" :key="rowIndex" class="chess-row">
      <div
          v-for="(cell, colIndex) in row"
          :key="colIndex"
          :class="cellClass(rowIndex, colIndex)"
          :data-coords="`${rowIndex},${colIndex}`"
          class="cell"
          @click="handleCellClick(rowIndex, colIndex)"
      >
        <img v-if="cell.piece" :src="getPieceImage(cell)" alt="piece" class="piece" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChessBoard",
  props: {
    board: {
      type: Array,
      required: true,
    },
    basePath: {
      type: String,
      required: true,
    },
  },
  methods: {
    cellClass(rowIndex, colIndex) {
      return (rowIndex + colIndex) % 2 === 0 ? "dark-cell" : "light-cell";
    },
    getPieceImage(cell) {
      if (cell.piece && cell.color) {
        const pieceName = cell.piece.charAt(0).toUpperCase() + cell.piece.slice(1).toLowerCase();
        return `${this.basePath}/${cell.color}/${pieceName}.png`;
      }
      return "";
    },
    handleCellClick(rowIndex, colIndex) {
      this.$emit("cell-click", rowIndex, colIndex);
    },
  },
};
</script>

<style scoped>
.chess-board {
  display: grid;
  grid-template-rows: repeat(8, 1fr);
  grid-template-columns: repeat(8, 1fr);
  gap: 1px;
}

.cell {
  width: 100%;
  height: 100%;
}

.dark-cell {
  background-color: #769656;
}

.light-cell {
  background-color: #eeeed2;
}

.piece {
  width: 100%;
  height: 100%;
}
</style>

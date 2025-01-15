<template>
  <div class="chess-board-container">
    <div class="chess-board">
      <div v-for="(row, rowIndex) in board" :key="rowIndex">
        <div
            v-for="(cell, colIndex) in row"
            :key="colIndex"
            :class="cellClass(rowIndex, colIndex)"
            class="cell"
            @click="handleCellClick(rowIndex, colIndex)"
        >
          <img v-if="cell.piece" :src="getPieceImage(cell)" alt="piece" class="piece" />
        </div>
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
        return `${process.env.VUE_APP_BACKEND_URL}/assets/images/${cell.color}/${pieceName}.png`;


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

.chess-board-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  padding: 10px;
  box-sizing: border-box;
  overflow: hidden;
}


.chess-board {
  display: grid;
  grid-template-rows: repeat(8, 1fr);
  grid-template-columns: repeat(8, 1fr);
  border: 1px solid #333;
  box-sizing: border-box;
  width: 80vmin;
  height: 80vmin;
  max-width: 80vh;
  max-height: 80vh;
}


.cell {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.dark-cell {
  background-color: #7AB7F5;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.light-cell {
  background-color: #EFEFEF;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.dark-cell:hover {
  background-color: #659DD4;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
}

.light-cell:hover {
  background-color: #D2D2D2;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
}

.piece {
  width: 80%;
  height: 80%;
  transition: width 0.3s, height 0.3s;
}

.piece:hover {
  width: 90%;
  height: 90%;
}

@media (min-width: 800px) {
  .chess-board {
    width: 80vh;
    height: 80vh;
  }

  .chess-board .cell {
    height: 9.95vh;
    width: 9.95vh;
  }
}

@media (max-width: 800px) {
  .chess-board {
    width: 80vw;
    height: 80vw;
  }

  .chess-board .cell {
    height: 9.95vw;
    width: 9.95vw;
  }
}
</style>

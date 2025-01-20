<template>
  <div class="chess-board-container">
    <div class="nav-container">
      <button class="back-btn" @click="$emit('back-to-menu')"></button>
    </div>
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
  background-color: dimgray;
  position: relative;
}

.nav-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  background-color: transparent;
  padding: 10px;
  z-index: 1000;
}

.back-btn {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 2px solid black;
  background-image: url('@/assets/pictures/Back.png');
  background-size: cover;
  background-color: #c6c8ca;
  background-position: center;
  background-repeat: no-repeat;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  padding: 0;
  margin: 10px;
}

.back-btn:hover {
  transform: scale(1.1);
  filter: brightness(1.1);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
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
  background-color: #355760;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.light-cell {
  background-color: #c6c8ca;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.dark-cell:hover {
  background-color: #355760;
  filter: brightness(1.1);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
}

.light-cell:hover {
  background-color: #D2D2D2;
  filter: brightness(1.1);
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

  .back-btn {
    width: 40px;
    height: 40px;
  }
}

@media (min-width: 1920px) {
  .back-btn {
    width: 60px;
    height: 60px;
  }
}

@media (min-width: 2560px) {
  .back-btn {
    width: 70px;
    height: 70px;
  }
}
</style>
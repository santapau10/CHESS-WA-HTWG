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
        console.log('apiUrl   ', process.env.VUE_APP_BACKEND_URL);
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
/* Container to center the chessboard */
.chess-board-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Full height of the screen */
  width: 100vw; /* Full width of the screen */
  padding: 10px;
  box-sizing: border-box;
  overflow: hidden; /* Prevent overflow */
}

/* Chessboard itself */
.chess-board {
  display: grid;
  grid-template-rows: repeat(8, 1fr);
  grid-template-columns: repeat(8, 1fr);
  border: 1px solid #333;
  box-sizing: border-box;
  width: 80vmin; /* Set width to 80% of the smaller viewport dimension */
  height: 80vmin; /* Set height to 80% of the smaller viewport dimension */
  max-width: 80vh; /* Prevent the board from exceeding 80% of the viewport height */
  max-height: 80vh; /* Prevent the board from exceeding 80% of the viewport height */
}

/* Cell styles */
.cell {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

/* Color styles for dark and light cells */
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

/* Piece styles */
.piece {
  width: 80%;
  height: 80%;
  transition: width 0.3s, height 0.3s;
}

.piece:hover {
  width: 90%;
  height: 90%;
}

/* Media Queries for responsive design */

/* For wide screens or landscape orientation */
@media (min-width: 800px) {
  .chess-board {
    width: 80vh; /* 80% of the screen height */
    height: 80vh; /* 80% of the screen height */
  }

  /* Ensure cells stay square */
  .chess-board .cell {
    height: 9.95vh; /* Each cell will be 1/8th of the board's width */
    width: 9.95vh;  /* Each cell will be 1/8th of the board's width */
  }
}

/* For smaller screens */
@media (max-width: 800px) {
  .chess-board {
    width: 80vw; /* 80% of the screen width */
    height: 80vw; /* 80% of the screen width */
  }

  /* Ensure cells stay square */
  .chess-board .cell {
    height: 9.95vw; /* Each cell will be 1/8th of the board's width */
    width: 9.95vw;  /* Each cell will be 1/8th of the board's width */
  }
}
</style>

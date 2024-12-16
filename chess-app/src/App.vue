<template>
  <div id="app">
    <div v-if="currentView === 'Settings'">
      <Settings @navigate="navigate" :socket="socket" />
    </div>
    <div v-else-if="currentView === 'ChessBoard'">
      <ChessBoard
          :board="board"
          :basePath="basePath"
          @cell-click="handleCellClick"
      />
    </div>
  </div>
</template>

<script>
import ChessBoard from "./components/Chess-board.vue";
import Settings from "./components/Start.vue";

export default {
  name: "App",
  data() {
    return {
      currentView: "ChessBoard", // Initial view
      board: [],
      basePath: "/path/to/your/images",
      socket: new WebSocket("ws://localhost:9000/socket"),
    };
  },
  components: {
    ChessBoard,
    Settings,
  },
  mounted() {
    console.log("App mounted, setting up WebSocket...");
    this.setupWebSocket();
  },
  methods: {
    navigate(view) {
      console.log(`Navigating to view: ${view}`);
      this.currentView = view;
      if (view === "ChessBoard") {
        this.loadBoard();
      }
    },
    loadBoard() {
      console.log("Loading board from server...");
      fetch("http://localhost:9000/jsonGame", {
        method: "GET",
        mode: "no-cors", // 'no-cors' sollte eher vermieden werden, falls der Server es unterstützt
      })
          .then((response) => {
            console.log("Response received from server:", response);
            return response.json();
          })
          .then((data) => {
            console.log("Board data received:", data);
            this.board = this.renderBoard(data.game.pieces);
          })
          .catch((error) => {
            console.error("Error loading board:", error);
            alert("Error loading the board.");
          });
    },
    renderBoard(pieces) {
      console.log("Rendering board with pieces:", pieces);
      const board = Array.from({ length: 8 }, () => Array(8).fill({}));
      pieces.forEach((piece) => {
        const { x, y } = piece.cords;
        board[x][y] = {
          piece: piece.piece,
          color: piece.color.toLowerCase().trim(),
        };
      });
      console.log("Rendered board:", board);
      return board;
    },
    handleCellClick(rowIndex, colIndex) {
      console.log(`Clicked on cell at: ${rowIndex}, ${colIndex}`);
      const targetCell = `${rowIndex},${colIndex}`;
      const origin = this.convertToChessNotation(targetCell);
      console.log("Origin in chess notation:", origin);
      this.sendMove(origin);
    },
    sendMove(origin) {
      console.log("Sending move with origin:", origin);
      fetch("/move/", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ origin }),
      })
          .then(() => {
            console.log("Move sent successfully, reloading board...");
            this.loadBoard();
          })
          .catch((error) => {
            console.error("Error sending move:", error);
            alert("Invalid move or server error.");
          });
    },
    convertToChessNotation(coords) {
      const [row, col] = coords.split(",").map(Number);
      const file = String.fromCharCode("a".charCodeAt(0) + row);
      const rank = (col + 1).toString();
      const chessNotation = file + rank;
      console.log(`Converted coordinates ${coords} to chess notation: ${chessNotation}`);
      return chessNotation;
    },
    setupWebSocket() {
      console.log("Setting up WebSocket connection...");
      this.socket = new WebSocket("ws://localhost:9000/socket");

      this.socket.onopen = () => {
        console.log("WebSocket connected");
      };

      this.socket.onmessage = (event) => {
        console.log("Message from server: ", event.data);
        if (event.data === "Game Started") {
          console.log("Game started, switching to ChessBoard view.");
          this.currentView = "ChessBoard";
        }
      };

      this.socket.onerror = (error) => {
        console.error("WebSocket error: ", error);
      };

      this.socket.onclose = () => {
        console.log("WebSocket connection closed");
      };
    },
  },
};
</script>

<template>
  <div id="app">
    <!-- Ladeoverlay -->
    <div v-if="loading" class="loading-overlay">
      <p>Loading...</p>
    </div>

    <!-- Authentifizierungsseiten -->
    <div v-else-if="!user">
      <div v-if="currentView === 'Login'">
        <Login @login-success="handleLoginSuccess" @switch-to-register="switchToRegister" />
      </div>
      <div v-else-if="currentView === 'Register'">
        <Register @register-success="handleRegistrationSuccess" @switch-to-login="switchToLogin" />
      </div>
    </div>

    <!-- Hauptansicht nach erfolgreicher Anmeldung -->
    <div v-else>
      <div v-if="currentView === 'Settings'">
        <Settings @start="startGame" @rules="switchToRules" />
      </div>
      <div v-else-if="currentView === 'Rules'">
        <Rules @back="switchToSettings" />
      </div>
      <div v-else-if="currentView === 'ChessBoard'">
        <ChessBoard
            :board="board"
            :basePath="basePath"
            @cell-click="handleCellClick"
            @back-to-menu="switchToSettings"
            @move-made="loadBoard"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Login from "./components/LoginComponent.vue";
import Register from "./components/registerComponent.vue";
import ChessBoard from "./components/Chess-board.vue";
import Settings from "./components/Start.vue";
import Rules from "./components/Rules.vue";
import {auth} from "./firebase";
import {onAuthStateChanged} from "firebase/auth";

const API_BASE_URL = process.env.VUE_APP_BACKEND_URL;

export default {
  name: "App",
  components: {Login, Register, ChessBoard, Settings, Rules},
  data() {
    return {
      user: null,
      currentView: "Login",
      board: [],
      basePath: "/path/to/your/images",
      loading: false,
      websocket: null,
    };
  },
  watch: {
    currentView(newView) {
      if (newView === 'ChessBoard') {
        this.connectWebSocket();
      } else if (this.websocket) {
        this.websocket.close();
      }
    }
  },
  created() {
    this.checkPersistentLogin();
  },
  mounted() {
    if (this.currentView === 'ChessBoard') {
      this.connectWebSocket();
    }
  },
  unmounted() {
    if (this.websocket) {
      this.websocket.close();
    }
  },
  methods: {
    checkPersistentLogin() {
      onAuthStateChanged(auth, (user) => {
        if (user) {
          this.user = user;
          if (this.currentView === "Login" || this.currentView === "Register") {
            this.currentView = "Settings";
          }
        } else {
          this.user = null;
          this.currentView = "Login";
        }
      });
    },
    handleLoginSuccess(user) {
      this.user = user;
      this.currentView = "Settings";
    },
    handleRegistrationSuccess(user) {
      this.user = user;
      this.currentView = "Settings";
    },
    switchToRegister() {
      this.currentView = "Register";
    },
    switchToLogin() {
      this.currentView = "Login";
    },
    switchToRules() {
      console.log("Switching to Rules");
      this.currentView = "Rules";
    },
    switchToSettings() {
      console.log("Switching to Settings");
      this.currentView = "Settings";
    },
    connectWebSocket() {
      if (this.websocket) {
        this.websocket.close();
      }

      this.websocket = new WebSocket(`${API_BASE_URL}/websocket`);

      this.websocket.onopen = () => {
        console.log("Connected to WebSocket");
      };

      this.websocket.onmessage = (e) => {
        try {
          const data = JSON.parse(e.data);
          if (data.game && data.game.pieces) {
            console.log("Received game state update");
            this.board = this.renderBoard(data.game.pieces);
          }
        } catch (error) {
          console.log("Received message:", e.data);
        }
      };

      this.websocket.onerror = (error) => {
        console.error("WebSocket error:", error);
      };

      this.websocket.onclose = () => {
        console.log("WebSocket connection closed");
        // Optional: Implement reconnection logic here
        setTimeout(() => this.connectWebSocket(), 5000);
      };
    },
    async startGame() {
      this.loading = true;
      try {
        await fetch(`${API_BASE_URL}/start`, {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          mode: "cors",
        });
        this.currentView = "ChessBoard";
        await this.loadBoard();
      } catch (error) {
        console.error("Error starting the game:", error);
        alert("Failed to start the game.");
      } finally {
        this.loading = false;
      }
    },
    async loadBoard() {
      this.loading = true;
      try {
        const response = await fetch(`${API_BASE_URL}/jsonGame`, {
          method: "GET",
          headers: {"Content-Type": "application/json"},
          mode: "cors",
        });
        const data = await response.json();
        this.board = this.renderBoard(data.game.pieces);
      } catch (error) {
        console.error("Error loading board:", error);
      } finally {
        this.loading = false;
      }
    },
    renderBoard(pieces) {
      const board = Array.from({length: 8}, () => Array(8).fill({}));
      pieces.forEach(({cords, piece, color}) => {
        board[cords.x][cords.y] = {
          piece,
          color: color.toLowerCase().trim(),
        };
      });
      return board;
    },
    async handleCellClick(rowIndex, colIndex) {
      const targetCell = `${rowIndex},${colIndex}`;
      const origin = this.convertToChessNotation(targetCell);

      try {
        const response = await fetch(`${API_BASE_URL}/move/`, {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({origin}),
          mode: "cors",
        });

        if (!response.ok) {
          throw new Error('Invalid move');
        }

        // After successful move, reload the board
        await this.loadBoard();

      } catch (error) {
        console.error("Error making move:", error);
        alert("Invalid move or server error.");
      }
    },
    convertToChessNotation(coords) {
      const [row, col] = coords.split(",").map(Number);
      const file = String.fromCharCode("a".charCodeAt(0) + row);
      const rank = (col + 1).toString();
      return file + rank;
    },
  },
};
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 1.5rem;
  z-index: 10;
}
</style>
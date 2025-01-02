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
        <Settings @start="startGame" />
      </div>
      <div v-else-if="currentView === 'ChessBoard'">
        <ChessBoard
            :board="board"
            :basePath="basePath"
            @cell-click="handleCellClick"
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
import {auth} from "./firebase";
import {onAuthStateChanged} from "firebase/auth";

const API_BASE_URL = process.env.VUE_APP_BACKEND_URL;

export default {
  name: "App",
  components: {Login, Register, ChessBoard, Settings},
  data() {
    return {
      user: null, // Benutzerobjekt für den angemeldeten Nutzer
      currentView: "Login", // Startansicht (Login)
      board: [],
      basePath: "/path/to/your/images",
      loading: false, // Ladezustand
      socket: null, // WebSocket-Verbindung
    };
  },
  created() {
    this.checkPersistentLogin(); // Prüft, ob der Benutzer bereits eingeloggt ist
  },
  mounted() {
    // Hier kannst du den WebSocket-Verbindungsmechanismus hinzufügen
    this.setupWebSocket();
  },
  unmounted() {
    // Schließt die WebSocket-Verbindung, wenn die Seite verlassen wird
    if (this.socket) {
      this.socket.close();
    }
  },
  methods: {
    // Prüft auf persistente Anmeldung
    checkPersistentLogin() {
      onAuthStateChanged(auth, (user) => {
        if (user) {
          this.user = user; // Benutzer setzen
          this.currentView = "Settings"; // Hauptansicht laden
        }
      });
    },
    // Wird nach erfolgreichem Login ausgeführt
    handleLoginSuccess(user) {
      this.user = user;
      this.currentView = "Settings";
    },
    // Wird nach erfolgreicher Registrierung ausgeführt
    handleRegistrationSuccess(user) {
      this.user = user;
      this.currentView = "Settings";
    },
    // Zur Registrierung wechseln
    switchToRegister() {
      this.currentView = "Register";
    },
    // Zum Login wechseln
    switchToLogin() {
      this.currentView = "Login";
    },
    // Spiel starten
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
    // Spielbrett laden
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
    // Spielfeld rendern
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
    // Zellenklick verarbeiten
    async handleCellClick(rowIndex, colIndex) {
      const targetCell = `${rowIndex},${colIndex}`;
      const origin = this.convertToChessNotation(targetCell);
      try {
        await fetch(`${API_BASE_URL}/move/`, {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({origin}),
          mode: "cors",
        });
        this.loadBoard();
      } catch (error) {
        console.error("Error sending move:", error);
        alert("Invalid move or server error.");
      }
    },
    // Koordinaten in Schachnotation umwandeln
    convertToChessNotation(coords) {
      const [row, col] = coords.split(",").map(Number);
      const file = String.fromCharCode("a".charCodeAt(0) + row);
      const rank = (col + 1).toString();
      return file + rank;
    },
    // WebSocket-Verbindung einrichten
    setupWebSocket() {
      // Erstelle eine WebSocket-Verbindung zu deinem Server (URL anpassen)
      this.socket = new WebSocket("ws://your-websocket-url");

      // Füge Event-Listener hinzu
      this.socket.onopen = () => {
        console.log("WebSocket connected");
      };

      this.socket.onmessage = (event) => {
        console.log("Received message:", event.data);
      };

      this.socket.onerror = (error) => {
        console.error("WebSocket error:", error);
      };

      this.socket.onclose = () => {
        console.log("WebSocket closed");
      };
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

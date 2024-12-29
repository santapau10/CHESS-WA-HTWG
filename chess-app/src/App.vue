<template>
  <div id="app">
    <!-- Ladeoverlay (nur für Start/Board-Laden, nicht beim Bewegen) -->
    <div v-if="loading" class="loading-overlay">
      <p>Loading...</p>
    </div>

    <!-- Offline-Komponente anzeigen -->
    <div v-else-if="isOffline">
      <OfflineComponent @go-to-settings="reloadPage" />
    </div>

    <!-- Hauptansichten -->
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
import ChessBoard from "./components/Chess-board.vue";
import Settings from "./components/Start.vue";
import OfflineComponent from "./components/OfflineComponent.vue";

const API_BASE_URL = process.env.VUE_APP_BACKEND_URL;

export default {
  name: "App",
  components: { ChessBoard, Settings, OfflineComponent },
  data() {
    return {
      currentView: "Settings", // Startansicht
      board: [],
      basePath: "/path/to/your/images",
      loading: false, // Ladezustand (außer Moves)
      isOffline: false, // Offline-Zustand
    };
  },
  created() {
    console.log("API_BASE_URL:", API_BASE_URL);
    this.registerServiceWorker(); // Service Worker registrieren
  },
  methods: {
    registerServiceWorker() {
      if ("serviceWorker" in navigator) {
        navigator.serviceWorker
            .register("/service-worker.js") // Pfad zur SW-Datei
            .then((registration) => {
              console.log("Service Worker registered with scope:", registration.scope);
            })
            .catch((error) => {
              console.error("Service Worker registration failed:", error);
            });
      }
    },
    async startGame() {
      this.loading = true;
      const isServerOnline = await this.checkServerStatusWithTimeout(5000);

      if (isServerOnline) {
        try {
          await fetch(`${API_BASE_URL}/start`, { method: "POST" });
          this.currentView = "ChessBoard";
          await this.loadBoard();
        } catch (error) {
          console.error("Error starting the game:", error);
          alert("Failed to start the game.");
        }
      }
      this.loading = false; // Ladeanzeige immer deaktivieren
    },
    async loadBoard() {
      this.loading = true;
      const isServerOnline = await this.checkServerStatusWithTimeout(5000);

      if (isServerOnline) {
        try {
          const response = await fetch(`${API_BASE_URL}/jsonGame`);
          const data = await response.json();
          this.board = this.renderBoard(data.game.pieces);
        } catch (error) {
          console.error("Error loading board:", error);
        }
      }
      this.loading = false; // Ladeanzeige deaktivieren
    }
    ,
    async handleCellClick(rowIndex, colIndex) {
      const targetCell = `${rowIndex},${colIndex}`;
      const origin = this.convertToChessNotation(targetCell);
      console.log("Sending move:", origin);

      try {
        await fetch(`${API_BASE_URL}/move/`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ origin }),
        });
        this.loadBoard(); // Lade Board neu, ohne `loading` zu aktivieren
      } catch (error) {
        console.error("Error sending move:", error);
        alert("Invalid move or server error.");
      }
    },
    async checkServerStatusWithTimeout(timeout) {
      try {
        const response = await Promise.race([
          fetch(`${API_BASE_URL}/`, { method: "GET" }),
          new Promise((_, reject) =>
              setTimeout(() => reject(new Error("Timeout")), timeout)
          ),
        ]);
        if (response && response.ok) {
          this.isOffline = false; // Server antwortet erfolgreich
          return true;
        }
      } catch (error) {
        console.error("Server status check failed:", error.message);
        this.isOffline = true; // Server nicht erreichbar
        return false;
      }
    }
    ,
    renderBoard(pieces) {
      const board = Array.from({ length: 8 }, () => Array(8).fill({}));
      pieces.forEach(({ cords, piece, color }) => {
        board[cords.x][cords.y] = {
          piece,
          color: color.toLowerCase().trim(),
        };
      });
      return board;
    },
    convertToChessNotation(coords) {
      const [row, col] = coords.split(",").map(Number);
      const file = String.fromCharCode("a".charCodeAt(0) + row);
      const rank = (col + 1).toString();
      return file + rank;
    },
    reloadPage() {
      this.isOffline = false;
      this.currentView = "Settings";
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

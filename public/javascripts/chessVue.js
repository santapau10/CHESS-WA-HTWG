$(document).ready(function () {
    Vue.component('chess-board', {
        data() {
            return {
                board: [], // Rendered board
                basePath: basePath, // Base path for images
            };
        },
        template: `
            <div class="chess-board">
                <div v-for="(row, rowIndex) in board" :key="rowIndex" class="chesshtml-row">
                    <div
                        v-for="(cell, colIndex) in row"
                        :key="colIndex"
                        :class="cellClass(rowIndex, colIndex)"
                        :data-coords="\`\${rowIndex},\${colIndex}\`"
                        class="cell"
                        @click="handleCellClick(rowIndex, colIndex)"
                    >
                        <img v-if="cell.piece" :src="getPieceImage(cell)" alt="piece" class="piece" />
                    </div>
                </div>
            </div>
        `,
        created() {
            this.connectWebSocket(); // Establish WebSocket connection
            this.loadBoard(); // Load the board when component is created
        },
        methods: {
            loadBoard() {
                $.ajax({
                    url: "/jsonGame", // Endpoint that returns game state
                    type: "GET",
                    dataType: "json",
                    success: (response) => {
                        this.board = this.renderBoard(response.game.pieces);
                    },
                    error: () => {
                        alert("Error loading the board.");
                    }
                });
            },
            renderBoard(pieces) {
                const board = Array.from({ length: 8 }, () => Array(8).fill({}));
                pieces.forEach(piece => {
                    const { x, y } = piece.cords;
                    board[x][y] = {
                        piece: piece.piece,
                        color: piece.color.toLowerCase().trim()
                    };
                });
                return board;
            },
            cellClass(rowIndex, colIndex) {
                return (rowIndex + colIndex) % 2 === 0 ? 'dark-cell' : 'light-cell';
            },
            getPieceImage(cell) {
                if (cell.piece && cell.color) {
                    return `${this.basePath}/${cell.color}/${cell.piece.toLowerCase()}.png`;
                }
                return '';
            },
            handleCellClick(rowIndex, colIndex) {
                console.log(`Clicked on cell at: ${rowIndex}, ${colIndex}`);
                const targetCell = `${rowIndex},${colIndex}`;
                const origin = this.convertToChessNotation(targetCell); // Convert to chess notation
                this.sendMove(origin); // Send move via POST request to backend
            },
            sendMove(origin) {
                $.ajax({
                    url: "/move/", // The route where the move is processed
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({ origin: origin }),
                    success: () => {
                        this.loadBoard(); // Reload the board after move
                    },
                    error: () => {
                        alert("Invalid move or server error.");
                    }
                });
            },
            convertToChessNotation(coords) {
                const [row, col] = coords.split(',').map(Number);
                const file = String.fromCharCode('a'.charCodeAt(0) + row);
                const rank = (col + 1).toString();
                return file + rank; // Convert to chess notation like "a2", "h8", etc.
            },
            connectWebSocket() {
                const websocket = new WebSocket("ws://localhost:9000/websocket");

                websocket.onopen = (event) => {
                    console.log("Connected to Websocket", websocket);
                };

                websocket.onclose = () => {
                    console.log('Connection with Websocket Closed!');
                };

                websocket.onerror = (error) => {
                    console.log('Error in Websocket Occurred: ' + error);
                };

                websocket.onmessage = (e) => {
                    if (e.data.startsWith("I received your message")) {
                        // Handle any test or debug messages from the backend
                        console.log("Received move confirmation", websocket);
                    } else {
                        // Assume it's the updated game state in JSON
                        console.log("Received game state update");
                        console.log(e.data)
                        let gameState = JSON.parse(e.data);
                        this.loadBoardFromWebSocket(gameState); // Properly call Vue's method
                    }
                };
            },
            loadBoardFromWebSocket(gameState) {
                // If the game state contains a list of pieces, load the board
                this.board = this.renderBoard(gameState.game.pieces);
            }
        }
    });

    new Vue({
        el: "#game"
    });
});

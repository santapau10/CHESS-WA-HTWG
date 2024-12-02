$(document).ready(function () {
    Vue.component('chess-board', {
        template: `
    <div v-for="(row, rowIndex) in board" :key="rowIndex" class="chesshtml-row">
      <div
        v-for="(cell, colIndex) in row"
        :key="colIndex"
        :class="cellClass(rowIndex, colIndex)"
        :data-coords="\`\${rowIndex},\${colIndex}\`"
        class="cell"
        @click="handleCellClick(rowIndex, colIndex)"
      >
        <img v-if="cell.piece" :src="getPieceImage(cell.piece)" alt="piece" class="piece" />
      </div>
    </div>
  `,
        data: function () {
            return {
                // Board-Daten, initialisiert mit den Schachbrett-Daten
                board: [
                    [
                        {piece: {icon: 'rook.png', color: 'white'}, row: 0, col: 0},
                        {piece: {icon: 'knight.png', color: 'white'}, row: 0, col: 1},
                        {piece: {icon: 'bishop.png', color: 'white'}, row: 0, col: 2},
                        {piece: {icon: 'queen.png', color: 'white'}, row: 0, col: 3},
                        {piece: {icon: 'king.png', color: 'white'}, row: 0, col: 4},
                        {piece: {icon: 'bishop.png', color: 'white'}, row: 0, col: 5},
                        {piece: {icon: 'knight.png', color: 'white'}, row: 0, col: 6},
                        {piece: {icon: 'rook.png', color: 'white'}, row: 0, col: 7}
                    ],
                    [
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 0},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 1},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 2},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 3},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 4},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 5},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 6},
                        {piece: {icon: 'pawn.png', color: 'white'}, row: 1, col: 7}
                    ],
                    [
                        {piece: null, row: 2, col: 0},
                        {piece: null, row: 2, col: 1},
                        {piece: null, row: 2, col: 2},
                        {piece: null, row: 2, col: 3},
                        {piece: null, row: 2, col: 4},
                        {piece: null, row: 2, col: 5},
                        {piece: null, row: 2, col: 6},
                        {piece: null, row: 2, col: 7}
                    ],
                    [
                        {piece: null, row: 3, col: 0},
                        {piece: null, row: 3, col: 1},
                        {piece: null, row: 3, col: 2},
                        {piece: null, row: 3, col: 3},
                        {piece: null, row: 3, col: 4},
                        {piece: null, row: 3, col: 5},
                        {piece: null, row: 3, col: 6},
                        {piece: null, row: 3, col: 7}
                    ],
                    [
                        {piece: null, row: 4, col: 0},
                        {piece: null, row: 4, col: 1},
                        {piece: null, row: 4, col: 2},
                        {piece: null, row: 4, col: 3},
                        {piece: null, row: 4, col: 4},
                        {piece: null, row: 4, col: 5},
                        {piece: null, row: 4, col: 6},
                        {piece: null, row: 4, col: 7}
                    ],
                    [
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 0},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 1},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 2},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 3},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 4},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 5},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 6},
                        {piece: {icon: 'pawn.png', color: 'black'}, row: 5, col: 7}
                    ],
                    [
                        {piece: {icon: 'rook.png', color: 'black'}, row: 6, col: 0},
                        {piece: {icon: 'knight.png', color: 'black'}, row: 6, col: 1},
                        {piece: {icon: 'bishop.png', color: 'black'}, row: 6, col: 2},
                        {piece: {icon: 'queen.png', color: 'black'}, row: 6, col: 3},
                        {piece: {icon: 'king.png', color: 'black'}, row: 6, col: 4},
                        {piece: {icon: 'bishop.png', color: 'black'}, row: 6, col: 5},
                        {piece: {icon: 'knight.png', color: 'black'}, row: 6, col: 6},
                        {piece: {icon: 'rook.png', color: 'black'}, row: 6, col: 7}
                    ]
                ]
            }
        },
        methods: {
            // Bestimmt die Hintergrundfarbe der Zellen (Schwarz oder Weiß)
            cellClass(rowIndex, colIndex) {
                return (rowIndex + colIndex) % 2 === 0 ? 'dark-cell' : 'light-cell';
            },
            // Gibt das Bild des Schachstücks basierend auf dem Stück zurück
            getPieceImage(piece) {
                if (piece) {
                    return `images/${piece.color}/${piece.icon}`;
                }
                return ''; // Keine Figur in der Zelle
            },
            // Handle Click auf eine Zelle
            handleCellClick(rowIndex, colIndex) {
                console.log(`Clicked on cell at: ${rowIndex}, ${colIndex}`);
                // Hier kann die Logik zum Ziehen der Schachfiguren implementiert werden
            }
        }
    });
});
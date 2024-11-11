// public/javascripts/chess.js

$(document).ready(function() {
    $(".cell").on("click", function() {
        const targetCell = $(this).data("coords");
        
        if (targetCell) {
            const origin = convertToChessNotation(targetCell);
            
            $.ajax({
                url: "/move/",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ origin: origin }),
                success: function() {
                    updateBoardState();
                },
                error: function() {
                    alert("Invalid move or server error.");
                }
            });
        }
    });

    // Initial board load
    loadBoard();
});

function loadBoard() {
    $.ajax({
        url: "/jsonGame", // Assuming this endpoint returns the current board state
        type: "GET",
        dataType: "json",
        success: function(response) {
            renderBoard(response.game.pieces);
        },
        error: function() {
            alert("Error loading the board.");
        }
    });
}

function updateBoardState() {
    $.ajax({
        url: "/jsonGame",
        type: "GET",
        dataType: "json",
        success: function(boardData) {
            renderBoard(boardData.game.pieces); // Render board with updated data
        },
        error: function() {
            alert("Error updating board state.");
        }
    });
}

function renderBoard(pieces) {
    $(".cell").empty(); // Clear all cells

    pieces.forEach(function(piece) {
        const { x, y } = piece.cords;
        const color = piece.color.toLowerCase().trim();
        const imgPath = `${basePath}${color}/${piece.piece}.png`;

        const cell = $(`[data-coords='${x},${y}']`);
        if (cell.length > 0) {
            cell.html(`<img src="${imgPath}" alt="" class="piece" />`);
        }
    });
}



// Convert board coordinates to chess notation (e.g., 1,0 -> "a2")
function convertToChessNotation(coords) {
    const [row, col] = coords.split(',').map(Number);
    const file = String.fromCharCode('a'.charCodeAt(0) + row); 
    const rank = (col + 1).toString(); 
    return file + rank;
}

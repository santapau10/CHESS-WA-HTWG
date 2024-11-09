// public/javascripts/chess.js


$(document).ready(function() {
    $(".cell").on("click", function() {
        const targetCell = $(this).data("coords");
        
        if (targetCell) {
            const origin = convertToChessNotation(targetCell);
            console.log(origin, )
            
            $.ajax({
                url: "/move/",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ origin: origin }),
                success: function(response) {
                },
                error: function() {
                    alert("Invalid move or server error.");
                }
            });
            
        } else {
            selectedCell = targetCell;
            console.log(selectedCell)
        }
    });
});

// Convierte las coordenadas numéricas a notación de ajedrez (ej. 1,0 -> "a2")
function convertToChessNotation(coords) {
    console.log(coords)
    const [row, col] = coords.split(',').map(Number);
    const rowA = row 
    const colA = col + 1
    const file = String.fromCharCode('a'.charCodeAt(0) + rowA); 
    const rank = (colA).toString(); 
    return file + rank;
}


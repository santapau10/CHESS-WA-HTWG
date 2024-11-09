$(document).ready(function() {
    let firstCell = null;
    let secondCell = null;

    $(".cell").click(function() {
        console.log($(this).data("position"))
        const cellPosition = $(this).data("position");

        // Si no hay celda seleccionada, asigna la primera
        if (!firstCell) {
            firstCell = cellPosition;
            $(this).addClass("selected"); // Añade estilo para resaltar la celda seleccionada
        } else if (!secondCell) {
            // Asigna la segunda celda y envía la solicitud
            secondCell = cellPosition;
            $(this).addClass("selected");

            // Enviar solicitud AJAX para mover pieza entre las celdas seleccionadas
            $.ajax({
                url: `/move/${firstCell}-${secondCell}`,
                method: "POST",
                success: function(response) {
                    console.log("Movimiento realizado:", response);
                    // Actualiza el tablero según la respuesta o recarga la página
                    $(".cell").removeClass("selected"); // Elimina selección
                    firstCell = null;
                    secondCell = null;
                },
                error: function() {
                    alert("Error al realizar el movimiento.");
                    $(".cell").removeClass("selected");
                    firstCell = null;
                    secondCell = null;
                }
            });
        }
    });
});

<template>
  <div class="rules">
    <h1>Chess Rules</h1>

    <section>
      <h3>Objective</h3>
      <p>The objective of the chess game is to checkmate the opponent's king, which means the king is in a position to be captured and cannot escape capture.</p>
    </section>

    <section>
      <h3>ChessBoard</h3>
      <p>The chessboard consists of 64 squares arranged in an 8x8 grid. The squares are alternately colored dark and light. The ranks are numbered from 1 to 8, and the files are lettered from a to h.</p>
    </section>

    <section>
      <h3>Chess Pieces</h3>
      <ul>
        <li>King</li>
        <li>Queen</li>
        <li>Rook</li>
        <li>Knight</li>
        <li>Bishop</li>
        <li>Pawn</li>
      </ul>
      <p>Each piece has specific movement capabilities and restrictions.</p>
    </section>

    <section>
      <h3>Special Rules</h3>
      <ul>
        <li><strong>Castling:</strong> A special move where the king and a rook are simultaneously moved.</li>
        <li><strong>En passant:</strong> A special pawn capture rule for pawns that have just moved two squares.</li>
        <li><strong>Pawn Promotion:</strong> When a pawn reaches the opponent's back rank, it can be promoted.</li>
      </ul>
    </section>

    <img src="/assets/game.png" alt="Chessboard" class="chessboard-image" />
  </div>
</template>

<script>
export default {
  name: 'Rules'
}
</script>
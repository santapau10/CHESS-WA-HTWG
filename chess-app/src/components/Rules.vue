<template>
  <div class="rules-container">
    <h1><a href="https://en.wikipedia.org/wiki/Chess">Chess Rules</a></h1>

    <div class="rules">
      <h3>Objective</h3>
      <p>The objective of the chessBoard game is to checkmate the opponent's king, which means the king is in a position to be
        captured and cannot escape capture.</p>

      <h3>ChessBoard</h3>
      <p>The chessboard consists of 64 squares arranged in an 8x8 grid. The squares are alternately colored dark and
        light. The ranks are numbered from 1 to 8, and the files are lettered from a to h.</p>

      <h3>Chess Pieces</h3>
      <p>There are six different types of chessBoard pieces:</p>
      <ul>
        <li>King</li>
        <li>Queen</li>
        <li>Rook</li>
        <li>Knight</li>
        <li>Bishop</li>
        <li>Pawn</li>
      </ul>
      <p>Each piece has specific movement capabilities and restrictions.</p>

      <h3>Gameplay</h3>
      <p>Players take turns to move their pieces. A move consists of moving one of the player's pieces to another square
        according to the rules of that particular piece. There are different types of moves, such as regular moves,
        capturing opponent's pieces, and special moves like castling and en passant capture.</p>

      <h3>Special Rules</h3>
      <ul>
        <li><strong>Castling</strong>: A special move where the king and a rook are simultaneously moved. It is only
          allowed under certain conditions.</li>
        <li><strong>En passant</strong>: A special rule that allows a pawn to capture an opponent's pawn that has moved
          two squares forward from its starting position, as if it had only moved one square.</li>
        <li><strong>Pawn Promotion</strong>: When a pawn reaches the opponent's back rank, it can be promoted to any
          other piece except the king.</li>
      </ul>

      <h3>Chessboard Image</h3>
      <img :src="require('@/assets/pictures/game.png')" alt="Chessboard Image" width="400" height="400" />
      <p><a href="https://github.com/santapau10/CHESS-WA-HTWG.git" target="_blank">GitHub Repo</a></p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GameRules'
}
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-image: url('@/assets/pictures/knight.jpg');
  color: #333;
  margin: 0;
  padding: 20px;
}

h1 {
  color: #2c3e50;
  font-size: 2.5rem;
  text-align: center;
  margin-bottom: 20px;
  text-decoration: underline;
}

h1 a {
  color: #2c3e50;
  text-decoration: none;
}

h1 a:hover {
  color: #2980b9;
  text-decoration: underline;
}

.rules {
  max-width: 800px;
  margin: 0 auto;
  background-color: #ffffff;
  padding: 50px;
  border: 2px solid #ddd;
  border-radius: 40px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.7);
}

.rules h3 {
  color: #2980b9;
  font-size: 1.8rem;
  margin-top: 20px;
}

.rules p {
  font-size: 1rem;
  line-height: 1.6;
  margin-bottom: 15px;
}

.rules ul {
  list-style-type: disc;
  padding-left: 20px;
  margin-bottom: 15px;
}

.rules ul li {
  font-size: 1rem;
  margin-bottom: 8px;
}

.rules img {
  display: block;
  margin: 20px auto;
  border: 2px solid #2980b9;
  border-radius: 8px;
  max-width: 100%;
}

.rules a {
  color: #2980b9;
  text-decoration: none;
  font-weight: bold;
}

.rules a:hover {
  color: #1c5980;
  text-decoration: underline;
}
</style>
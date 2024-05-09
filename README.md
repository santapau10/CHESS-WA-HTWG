[![Scala CI](https://github.com/Gewinkh/CHESS-SE-HTWG/actions/workflows/scala.yml/badge.svg)](https://github.com/Gewinkh/CHESS-SE-HTWG/actions/workflows/scala.yml)
[![Coverage Status](https://coveralls.io/repos/github/Gewinkh/CHESS-SE-HTWG/badge.svg?branch=master)](https://coveralls.io/github/Gewinkh/CHESS-SE-HTWG?branch=master)
# Chess Game Project at HTWG Konstanz

## Overview

This project was developed by Gewin and Arian as part of their computer science studies at the University of Applied Sciences in Konstanz (HTWG Konstanz). The aim of the project was to implement a fully functional chess game that incorporates all the basic rules and functionalities of chess.

## Game Rules

### Objective

The objective of the chess game is to checkmate the opponent's king, which means the king is in a position to be captured and cannot escape capture.

### ChessBoard

The chessboard consists of 64 squares arranged in an 8x8 grid. The squares are alternately colored dark and light. The ranks are numbered from 1 to 8, and the files are lettered from a to h.

### Chess Pieces

There are six different types of chess pieces:
- King
- Queen
- Rook
- Knight
- Bishop
- Pawn

Each piece has specific movement capabilities and restrictions.

### Gameplay

Players take turns to move their pieces. A move consists of moving one of the player's pieces to another square according to the rules of that particular piece. There are different types of moves, such as regular moves, capturing opponent's pieces, and special moves like castling and en passant capture.

### Special Rules

- **Castling**: A special move where the king and a rook are simultaneously moved. It is only allowed under certain conditions.
- **En passant**: A special rule that allows a pawn to capture an opponent's pawn that has moved two squares forward from its starting position, as if it had only moved one square.
- **Pawn Promotion**: When a pawn reaches the opponent's back rank, it can be promoted to any other piece except the king.

package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;

public class Bishop extends ChessPiece {
	
	public Bishop(Player player) {
		super(player);
		id = PieceID.BISHOP;
	}
	
	public Bishop clone() {
		return new Bishop(player);
	}
		
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		Position[][] board = cb.getBoard();
		ArrayList<Move> moves = new ArrayList<Move>();

		int row = cord.getRow();
		int col = cord.getCol();
		int c, r; //Indices for current row/column in below for loops
		
		//check north-west diagonal
		for (c = col-1, r = row - 1; c >= 0 && r >= 0; c--, r--) {
			if (board[r][c].isEmpty())
				moves.add(new Move(cord, new Coord(r, c), null));
			else {
				if (board[r][c].isEnemy(player))
					moves.add(new Move(cord, new Coord(r, c), null));
				break; //encountered a piece, stop walking
			}
		}
		
		//check north-east diagonal
		for (r = row - 1, c = col+1; c < 8 && r >= 0; c++, r--) {
			if (board[r][c].isEmpty())
				moves.add(new Move(cord, new Coord(r, c), null));
			else {
				if (board[r][c].isEnemy(player))
					moves.add(new Move(cord, new Coord(r, c), null));
				break; //encountered a piece, stop walking
			}
		}
		
		//check south-west diagonal
		for (r = row + 1, c = col-1; c >= 0 && r < 8; c--, r++) {
			if (board[r][c].isEmpty())
				moves.add(new Move(cord, new Coord(r, c), null));
			else {
				if (board[r][c].isEnemy(player))
					moves.add(new Move(cord, new Coord(r, c), null));
				break; //encountered a piece, stop walking
			}
		}
		
		//check south-east diagonal
		for (r = row + 1, c = col+1; c < 8 && r < 8; c++, r++) {
			if (board[r][c].isEmpty())
				moves.add(new Move(cord, new Coord(r, c), null));
			else {
				if (board[r][c].isEnemy(player))
					moves.add(new Move(cord, new Coord(r, c), null));
				break; //encountered a piece, stop walking
			}
		}
		
		return moves;
	}
	
}
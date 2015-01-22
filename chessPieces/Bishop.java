package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;

public class Bishop extends ChessPiece {

	private boolean hasMoved;
	
	public Bishop(Player player) {
		super(player);
		hasMoved = false;
		id = PieceID.BISHOP;
	}
	
	public Bishop(Player player, boolean hasMoved) { 
		super(player);
		this.hasMoved = hasMoved;
		id = PieceID.BISHOP;
	}
	
	public Bishop clone() {
		return new Bishop(player, hasMoved);
	}
		
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		ArrayList<Move> moves = new ArrayList<Move>();
		Position[][] board = cb.getBoard();

		int row = cord.getRow();
		int col = cord.getCol();
		
		//check north-west diagonal
		int r = row - 1;
		for (int c = col-1; c >= 0 && r >= 0; c--, r--) {
			if (board[row][c].isEmptyOrEnemy())
				moves.add(new Move(cord, new Coord(row, c)));
			else
				break; //encountered own piece
		}
		
		//check north-east diagonal
		r = row - 1;
		for (int c = col+1; c < 8 && r >= 0; c++, r--) {
			if (board[row][c].isEmptyOrEnemy())
				moves.add(new Move(cord, new Coord(row, c)));
			else
				break; //encountered own piece
		}
		
		//check south-west diagonal
		r = row + 1;
		for (int c = col-1; c >= 0 && r < 8; c--, r++) {
			if (board[row][c].isEmptyOrEnemy())
				moves.add(new Move(cord, new Coord(row, c)));
			else
				break; //encountered own piece
		}
		
		//check south-east diagonal
		r = row + 1;
		for (int c = col+1; c < 8 && r < 8; c++, r++) {
			if (board[row][c].isEmptyOrEnemy())
				moves.add(new Move(cord, new Coord(row, c)));
			else
				break; //encountered own piece
		}
		
		
		return moves;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public Code moveCode(Coord from, Coord to) {
		hasMoved = true;
		return Code.SUCCESS;
	}
	
}
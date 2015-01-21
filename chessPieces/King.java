package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;


public class King extends ChessPiece {

	public King(Player player) {
		super(player);
		id = PieceID.KING;
	}
	
	public King clone() {
		return new King(player);
	}
	
	public String toString() {
		return "K" + player.ordinal();
	}

	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		Position[][] board = cb.getBoard();
		
		int row = cord.getRow();
		int col = cord.getCol();
		
		//TODO: Can I use a set class to get every pemutation of [1 -1] and use loop?!
		//one square in any direction
		if (cb.validPosition(row-1, col) && board[row-1][col].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row-1, col)));
		
		if (cb.validPosition(row-1, col-1) && board[row-1][col-1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row-1, col-1)));
		
		if (cb.validPosition(row-1, col+1) && board[row-1][col+1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row-1, col+1)));
		
		if (cb.validPosition(row, col-1) && board[row][col-1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row, col-1)));
		
		if (cb.validPosition(row, col+1) && board[row][col+1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row, col)));
		
		if (cb.validPosition(row+1, col) && board[row+1][col].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row+1, col)));	
		
		if (cb.validPosition(row+1, col-1) && board[row+1][col-1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row+1, col-1)));
		
		if (cb.validPosition(row+1, col+1) && board[row+1][col+1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row+1, col+1)));

		return moves;
	}
	
}

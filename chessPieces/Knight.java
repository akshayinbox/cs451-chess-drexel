package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;

public class Knight extends ChessPiece {
	
	public Knight(Player player) {
		super(player);
		id = PieceID.KNIGHT;
	}
	
	public Knight clone() {
		return new Knight(player);
	}
	
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		Position[][] board = cb.getBoard();
		
		int row = cord.getRow();
		int col = cord.getCol();
		
		//check all 8 positions, TODO: generate the 1,2 permutations 
		if (cb.validPosition(row-1, col-2) && board[row-1][col-2].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row-1, col-2)));
		if (cb.validPosition(row-1, col+2) && board[row-1][col+2].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row-1, col+2)));	
		
		if (cb.validPosition(row+1, col-2) && board[row+1][col-2].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row+1, col-2)));	
		if (cb.validPosition(row+1, col+2) && board[row+1][col+2].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row+1, col+2)));	
			
		if (cb.validPosition(row-2, col+1) && board[row-2][col+1].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row-2, col+1)));	
		if (cb.validPosition(row-2, col-1) && board[row-2][col-1].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row-2, col-1)));	
			
		if (cb.validPosition(row+2, col+1) && board[row+2][col+1].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row+2, col+1)));	
		if (cb.validPosition(row+2, col-1) && board[row+2][col-1].isEmptyOrEnemy());
			moves.add(new Move(cord, new Coord(row+2, col-1)));	
			
		return moves;
	}
		
}

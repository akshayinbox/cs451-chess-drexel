package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;


public class Pawn extends ChessPiece {

	private boolean hasMoved;
	
	public Pawn(Player player) {
		super(player);
		hasMoved = false;
		id = PieceID.PAWN;
	}
	
	public Pawn(Player player, boolean hasMoved) {
		super(player);
		this.hasMoved = hasMoved;
		id = PieceID.PAWN;
	}
	
	public Pawn clone() {
		return new Pawn(player, hasMoved);
	}
	
	public PieceID getID() {
		return id;
	}
		
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		Position[][] board = cb.getBoard();
		
		int row = cord.getRow();
		int col = cord.getCol();
		
		//one square ahead
		if (cb.validPosition(row-1, col) && board[row-1][col].isEmpty())
			moves.add(new Move(cord, new Coord(row-1, col)));
		
		//TODO: Can you jump over pieces?
		//two squares ahead
		if (!hasMoved && cb.validPosition(row-2, col) && board[row-2][col].isEmpty())
			moves.add(new Move(cord, new Coord(row-2, col)));
		
		//attack diagonal
		if (cb.validPosition(row-1, col-1) && board[row-1][col-1].isEnemy());
			moves.add(new Move(cord, new Coord(row-1, col-1)));
		if (cb.validPosition(row-1, col+1) && board[row-1][col+1].isEnemy());
			moves.add(new Move(cord, new Coord(row-1, col+1)));	
		
		return moves;
	}
	
	public Code moveTo(Coord coord) {
		//this.coord = coord;
		hasMoved = true;
		
		if (coord.getRow() == 0)
			return Code.PROMOTION;
		else
			return Code.SUCCESS;
	}
	
	public String toString() {
		return "P" + player.ordinal();
	}


}

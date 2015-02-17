package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;

public class Rook extends ChessPiece {

	private boolean hasMoved;
	
	public Rook(Player player) {
		super(player);
		hasMoved = false;
		id = PieceID.ROOK;
	}
	
	public Rook(Player player, boolean hasMoved) { 
		super(player);
		this.hasMoved = hasMoved;
		id = PieceID.ROOK;
	}
	
	public Rook clone() {
		return new Rook(player, hasMoved);
	}
		
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		Position[][] board = cb.getBoard();
		ArrayList<Move> moves = new ArrayList<Move>();

		int row = cord.getRow();
		int col = cord.getCol();
		
		//check row to left
		for (int c = col-1; c >= 0; c--) {
			if (board[row][c].isEmpty())
				moves.add(new Move(cord, new Coord(row, c), null));
			else {
				if (board[row][c].isEnemy(player)) 
					moves.add(new Move(cord, new Coord(row, c), null));
				break; //encountered own piece or enemy piece
			}
		}
		
		//check row to right
		for (int c = col+1; c < 8; c++) {
			if (board[row][c].isEmpty())
				moves.add(new Move(cord, new Coord(row, c), null));
			else {
				if (board[row][c].isEnemy(player)) 
					moves.add(new Move(cord, new Coord(row, c), null));
				break; //encountered own piece or enemy piece
			}
		}
		
		//check column to north
		for (int r = row-1; r >= 0; r--) {
			if (board[r][col].isEmpty())
				moves.add(new Move(cord, new Coord(r, col), null));
			else {
				if (board[r][col].isEnemy(player)) 
					moves.add(new Move(cord, new Coord(r, col), null));
				break; //encountered own piece or enemy piece
			}
		}
		
		//check column to south
		for (int r = row+1; r < 8; r++) {
			if (board[r][col].isEmpty())
				moves.add(new Move(cord, new Coord(r, col), null));
			else {
				if (board[r][col].isEnemy(player)) 
					moves.add(new Move(cord, new Coord(r, col), null));
				break; //encountered own piece or enemy piece
			}
		}
		
		return moves;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public Code moveCode(ChessBoard cb, Coord from, Coord to) {
		hasMoved = true;
		return Code.SUCCESS;
	}
	
}

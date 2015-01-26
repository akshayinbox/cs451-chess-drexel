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
	
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		Position[][] board = cb.getBoard();
		ArrayList<Move> moves = new ArrayList<Move>();
		
		int row = cord.getRow();
		int col = cord.getCol();
		
		//one square ahead
		if (ChessBoard.validPosition(row-1, col) && board[row-1][col].isEmpty())
			moves.add(new Move(cord, new Coord(row-1, col)));
		
		//TODO: Can you jump over pieces?
		//two squares ahead
		if (!hasMoved && ChessBoard.validPosition(row-2, col) && board[row-2][col].isEmpty())
			moves.add(new Move(cord, new Coord(row-2, col)));
		
		//attack diagonal
		if (ChessBoard.validPosition(row-1, col-1) && board[row-1][col-1].isEnemy(player));
			moves.add(new Move(cord, new Coord(row-1, col-1)));
		if (ChessBoard.validPosition(row-1, col+1) && board[row-1][col+1].isEnemy(player));
			moves.add(new Move(cord, new Coord(row-1, col+1)));	
			
		//check for en Passant
		Move lastMove = cb.getPreviousMove();
		
		if (lastMove != null) {
			Coord fromPos = lastMove.getFrom();
			Coord toPos = lastMove.getTo();
			
			//check if opposing pawn moved by 2 spaces
			if (board[toPos.getRow()][toPos.getCol()].isOpposingType(PieceID.PAWN) &&
					toPos.getRow() - fromPos.getRow() == 2) {
				
				//check if piece is now one row to the left/right of this piece and on same row
				int columnDifference = toPos.getCol() - col;
				if ((columnDifference == 1 || columnDifference == -1) && toPos.getRow() == row) 
					moves.add(new Move(new Coord(row, col), new Coord(row + 1, col + columnDifference)));
			}
		}
		
		return moves;
	}
	
	public Code moveCode(ChessBoard cb, Coord from, Coord to) {
		hasMoved = true;
		
		if (to.getRow() == 0)
			return Code.PROMOTION;
		//if it attacked diagonally to an empty space, it's En Passant
		else if (from.getCol() - to.getCol() != 0 && (cb.getBoard())[to.getRow()][to.getCol()].isEmpty())
			return Code.EN_PASSANT;
		else
			return Code.SUCCESS;
	}
	
}
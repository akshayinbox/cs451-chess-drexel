package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;


public class King extends ChessPiece {
	
	private boolean hasMoved;

	public King(Player player) {
		super(player);
		hasMoved = false;
		id = PieceID.KING;
	}
	
	public King clone() {
		return new King(player);
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
			moves.add(new Move(cord, new Coord(row, col+1)));
		
		if (cb.validPosition(row+1, col) && board[row+1][col].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row+1, col)));	
		
		if (cb.validPosition(row+1, col-1) && board[row+1][col-1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row+1, col-1)));
		
		if (cb.validPosition(row+1, col+1) && board[row+1][col+1].isEmptyOrEnemy())
			moves.add(new Move(cord, new Coord(row+1, col+1)));
		
		/* You can only castle if:
		 * 1. The king has not moved.
		 * 2. The rook in question has not moved.
		 * 3. There are no pieces inbetween king and said rook.
		 * 4. The king is not in check.
		 * 5. The position the king skips over would not put him in check.
		 * 6. The new position would not put the king in check.
		 */
		if (!cb.kingInCheck(board) && !hasMoved) {
			
			//check left rook
			if (board[7][0].isOwnType(PieceID.ROOK)) {
				
				boolean empty1 = board[7][1].isEmpty();
			    boolean empty2 = board[7][2].isEmpty();
			    boolean empty3 = board[7][3].isEmpty();
				
				Rook left = (Rook) board[7][0].getPiece();
				boolean rookMoved = left.hasMoved();
				
				//check if position king is moving through would put in check
				Position[][] boardClone = cb.cloneBoard();
				Coord kingFrom = new Coord(7, 4);
				Coord kingThrough = new Coord(7, 3);
				cb.applyMove(new Move(kingFrom, kingThrough), boardClone);
				boolean inCheckThrough = cb.kingInCheck(boardClone);
				
				if(empty1 && empty2 && empty3 && !rookMoved && !inCheckThrough)
					moves.add(new Move(cord, new Coord(row, col-2)));
			}
			
			//check right rook
			if (board[7][0].isOwnType(PieceID.ROOK)) {
				
				boolean empty1 = board[7][5].isEmpty();
			    boolean empty2 = board[7][6].isEmpty();
				
				Rook right = (Rook) board[7][7].getPiece();
				boolean rookMoved = right.hasMoved();
				
				//check if position king is moving through would put in check
				Position[][] boardClone = cb.cloneBoard();
				Coord kingFrom = new Coord(0, 4);
				Coord kingThrough = new Coord(0, 5);
				cb.applyMove(new Move(kingFrom, kingThrough), boardClone);
				boolean inCheckThrough = cb.kingInCheck(boardClone);
				
				if(empty1 && empty2 && !rookMoved && !inCheckThrough)
					moves.add(new Move(cord, new Coord(row, col+2)));
			}
		}

		return moves;
	}
	
	public Code moveCode(Coord from, Coord to) {
		hasMoved = true;
		
		if (from.getCol() - to.getCol() == 2)
			return Code.CASTLE_LEFT;
		else if (from.getCol() - to.getCol() == -2)
			return Code.CASTLE_RIGHT;
		else
			return Code.SUCCESS;
	}
	
}

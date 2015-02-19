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
	
	public King(Player player, boolean hasMoved) {
		super(player);
		this.hasMoved = hasMoved;
		id = PieceID.KING;
	}
	
	public King clone() {
		return new King(player, hasMoved);
	}

	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		Position[][] board = cb.getBoard();
		ArrayList<Move> moves = new ArrayList<Move>();
		
		int row = cord.getRow();
		int col = cord.getCol();
		
		//TODO: Can I use a set class to get every permutation of [1 -1] and use a loop instead?
		//one square in any direction
		if (ChessBoard.validPosition(row-1, col) && board[row-1][col].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row-1, col), null));
		
		if (ChessBoard.validPosition(row-1, col-1) && board[row-1][col-1].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row-1, col-1), null));
		
		if (ChessBoard.validPosition(row-1, col+1) && board[row-1][col+1].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row-1, col+1), null));
		
		if (ChessBoard.validPosition(row, col-1) && board[row][col-1].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row, col-1), null));
		
		if (ChessBoard.validPosition(row, col+1) && board[row][col+1].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row, col+1), null));
		
		if (ChessBoard.validPosition(row+1, col) && board[row+1][col].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row+1, col), null));	
		
		if (ChessBoard.validPosition(row+1, col-1) && board[row+1][col-1].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row+1, col-1), null));
		
		if (ChessBoard.validPosition(row+1, col+1) && board[row+1][col+1].isEmptyOrEnemy(player))
			moves.add(new Move(cord, new Coord(row+1, col+1), null));
		
		/* You can only castle if:
		 * 1. The king has not moved.
		 * 2. The rook in question has not moved.
		 * 3. There are no pieces inbetween king and said rook.
		 * 4. The king is not in check.
		 * 5. The position the king skips over would not put him in check.
		 * 6. The new position would not put the king in check.
		 */
		
		//only check for castle if it's PLAYER1 since PLAYER2 is responsible for seeing if he can castle
		if (player == Player.PLAYER1 && !hasMoved && !cb.kingInCheck()) {
			
			//check left rook
			if (board[7][0].isOwnType(PieceID.ROOK)) {
				
				
				//TODO: depends on starting ponti of king, find where king is
				//then go through positions to left (could be 3 or 4)! YOu also already know 
				//king never moved.
				
				boolean leftSideEmpty = true;
				for (int i = col-1; i > 0; i--) 
					leftSideEmpty = leftSideEmpty && board[7][i].isEmpty();
				
				Rook left = (Rook) board[7][0].getPiece();
				boolean rookMoved = left.hasMoved();
				
				//check if position king is moving through would put in check
				ChessBoard boardClone = new ChessBoard(cb);
				Coord kingFrom = new Coord(row, col);
				Coord kingThrough = new Coord(row, col-1);
				boardClone.applyMove(new Move(kingFrom, kingThrough, null));
				boolean inCheckThrough = boardClone.kingInCheck();
				
				if(leftSideEmpty && !rookMoved && !inCheckThrough)
					moves.add(new Move(cord, new Coord(row, col-2), null));
			}
			
			//check right rook
			if (board[7][0].isOwnType(PieceID.ROOK)) {
				
				boolean rightSideEmpty = true;
				for (int i = col+1; i < 7; i++) 
					rightSideEmpty = rightSideEmpty && board[7][i].isEmpty();
				
				Rook right = (Rook) board[7][7].getPiece();
				boolean rookMoved = right.hasMoved();
				
				//check if position king is moving through would put in check
				ChessBoard boardClone = new ChessBoard(cb);
				Coord kingFrom = new Coord(7, col);
				Coord kingThrough = new Coord(7, col+1);
				boardClone.applyMove(new Move(kingFrom, kingThrough, null));
				boolean inCheckThrough = boardClone.kingInCheck();
				
				if(rightSideEmpty && !rookMoved && !inCheckThrough)
					moves.add(new Move(cord, new Coord(row, col+2), null));
			}
		}

		return moves;
	}

	public Code moveCode(ChessBoard cb, Coord from, Coord to) {
		hasMoved = true;
		
		if (from.getCol() - to.getCol() == 2)
			return Code.CASTLE_LEFT;
		else if (from.getCol() - to.getCol() == -2)
			return Code.CASTLE_RIGHT;
		else
			return Code.SUCCESS;
	}
	
}

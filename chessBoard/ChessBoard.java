package chessBoard;

import java.util.ArrayList;

import chessPieces.Bishop;
import chessPieces.King;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.PieceID;
import chessPieces.Queen;
import chessPieces.Rook;


public class ChessBoard {

	private Position[][] board;
	
	public ChessBoard() {
		board = new Position[8][8];
	}
	
	public void initializeBoard() {
		
		//initialize positions
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				board[i][j] = new Position(i, j);
		
		//initialize pawns
		for (int i = 0; i < 8; i++)
			board[6][i].addPiece(new Pawn(Player.PLAYER1));
		
		for (int i = 0; i < 8; i++)
			board[1][i].addPiece(new Pawn(Player.PLAYER2));
		
		//initialize rooks
		board[7][0].addPiece(new Rook(Player.PLAYER1));
		board[7][7].addPiece(new Rook(Player.PLAYER1));
		board[0][0].addPiece(new Rook(Player.PLAYER2));
		board[0][7].addPiece(new Rook(Player.PLAYER2));
		
		//initialize knights
		board[7][1].addPiece(new Knight(Player.PLAYER1));
		board[7][6].addPiece(new Knight(Player.PLAYER1));
		board[0][1].addPiece(new Knight(Player.PLAYER2));
		board[0][6].addPiece(new Knight(Player.PLAYER2));
		
		//initialize bishops
		board[7][2].addPiece(new Bishop(Player.PLAYER1));
		board[7][5].addPiece(new Bishop(Player.PLAYER1));
		board[0][2].addPiece(new Bishop(Player.PLAYER2));
		board[0][5].addPiece(new Bishop(Player.PLAYER2));
		
		//initialize queens
		board[7][3].addPiece(new Queen(Player.PLAYER1));
		board[0][3].addPiece(new Queen(Player.PLAYER2));
		
		//initialize kings
		board[7][4].addPiece(new King(Player.PLAYER1));
		board[0][4].addPiece(new King(Player.PLAYER2));
	}
	
	public Position[][] cloneBoard() {
		Position[][] boardClone = new Position[8][8];
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				boardClone[i][j] = board[i][j].clone();
		
		return boardClone;
	}
	
	//received move from network, we know it's valid so just make the move to the actual board
	public void receiveMove(Move opponentMove) {
		applyMove(opponentMove, board);
	}
	
	//apply move to board, use return code to handle special cases
	public Code applyMove(Move move, Position[][] board) {
		Coord fromCoord = move.getFrom();
		Coord toCoord = move.getTo();
		Position fromPosition = board[fromCoord.getRow()][fromCoord.getCol()];
		Position toPosition = board[toCoord.getRow()][toCoord.getCol()];
		
		Code returnCode = fromPosition.getPiece().moveCode(fromCoord,toCoord);

		//check if castle was applied and update rook
		if (returnCode == Code.CASTLE_LEFT) {
			Position rookFrom = board[fromCoord.getRow()][0];
			Position rookTo = board[fromCoord.getRow()][3];
			rookTo.addPiece(rookFrom.getPiece());
			rookFrom.clearPiece();
		}
		else if (returnCode == Code.CASTLE_RIGHT) {
			Position rookFrom = board[fromCoord.getRow()][7];
			Position rookTo = board[fromCoord.getRow()][5];
			rookTo.addPiece(rookFrom.getPiece());
			rookFrom.clearPiece();
		}
		
		toPosition.addPiece(fromPosition.getPiece());
		fromPosition.clearPiece();
		
		return returnCode;
	}
	
	public Code validateAndApply(Move move) {
		
		Coord from = move.getFrom();
		Coord to = move.getTo();
		
		Position fromPosition = board[from.getRow()][from.getCol()];
		Position toPosition = board[to.getRow()][to.getCol()];
		
		//can't move on top of your own piece
		if (!toPosition.isEmpty() && toPosition.getPiece().getPlayer() == Player.PLAYER1) 
			return Code.NOT_LEGAL;
		
		//can't move enemy piece
		if (!fromPosition.isEmpty() && fromPosition.getPiece().getPlayer() == Player.PLAYER2) 
			return Code.NOT_LEGAL;
		
		//check if legal move
		ArrayList<Move> moves = fromPosition.getPiece().getMoves(this, from);
		if (moves == null || !moves.contains(move))
			return Code.NOT_LEGAL;
		
		//clone board, apply new move
		Position[][] cloneBoard = cloneBoard();
		Code returnCode = applyMove(move, cloneBoard);
				
		//check if king is moved into check, otherwise update the board to the new board
		if (kingInCheck(cloneBoard))
			return Code.NOT_LEGAL;
		else 
			board = cloneBoard;
		
		return returnCode;
	}
	
//	private boolean gameOver() {
//		
//		if (!kingInCheck(board))
//			return false;
//		else {
//			//check if king can get out of check
//			
//			//
//		}
//		
//	}
	
	public boolean kingInCheck(Position[][] board) {
		
		int row = 0, col = 0;
		
		//find king's position
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if(board[r][c].isOwnType(PieceID.KING)) {
					row = r;
					col = c;
					break;
				}
			}
		}

		//
		
		//check if opposing pawn can capture
		if (validPosition(row-1, col-1) && board[row-1][col-1].isOpposingType(PieceID.PAWN))
			return true;
		if (validPosition(row-1, col+1) && board[row-1][col+1].isOpposingType(PieceID.PAWN))
			return true;
		
		//check if knight can capture.. etc, for the other piece types
		
		return false;
	}
	
	public boolean validPosition(int row, int col) {
		return row >= 0 && row < 8 && col >=0 && col < 8; 
	}
	
	public Position[][] getBoard() {
		return board;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("   ");
		for (int i = 0; i < 8; i++) 
			sb.append("  " + i);
		
		sb.append('\n');
		
		for (int i = 0; i < 8; i++) {
			sb.append(" " + i + "  ");
			for (int j = 0; j < 8; j++) {
				sb.append(board[i][j].toString() + " ");
			}
			sb.append('\n');
		}
			
		return sb.toString();
	}
	
	
}

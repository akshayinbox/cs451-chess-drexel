package chessBoard;

import java.io.Serializable;
import java.util.ArrayList;

import chessPieces.Bishop;
import chessPieces.ChessPiece;
import chessPieces.King;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.PieceID;
import chessPieces.Queen;
import chessPieces.Rook;

/**
 * This class is a model of an 8x8 game board used for chess. It aggregates chess pieces and is primarily
 * used by the UI for validating moves and applying moves to the board.
 *
 */

public class ChessBoard  implements Serializable {
	private static final long serialVersionUID = -1372026691004024485L;
	private Position[][] board;
	private Move opposingPreviousMove;
	private boolean isHost;
	private final static int BOARD_ROWS = 8;
	private final static int BOARD_COLS = 8;
	
	public ChessBoard(boolean host) {
		board = new Position[BOARD_COLS][BOARD_ROWS];
		isHost = host;
	}
	
	public ChessBoard(ChessBoard cb) {
		board = cloneBoard(ChessBoard.cloneBoard(cb.getBoard()));
	}
	
	/**
	 * Initializes the gameboard by constructing the appropriate ChessPieces and inserting them into
	 * the board array.
	 */
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
		
		if (isHost) {
			//initialize queens
			board[7][3].addPiece(new Queen(Player.PLAYER1));
			board[0][3].addPiece(new Queen(Player.PLAYER2));
			
			//initialize kings
			board[7][4].addPiece(new King(Player.PLAYER1));
			board[0][4].addPiece(new King(Player.PLAYER2));
		} else {
			//initialize queens
			board[7][4].addPiece(new Queen(Player.PLAYER1));
			board[0][4].addPiece(new Queen(Player.PLAYER2));
			
			//initialize kings
			board[7][3].addPiece(new King(Player.PLAYER1));
			board[0][3].addPiece(new King(Player.PLAYER2));
		}
	}
	
	/**
	 * Clones and returns a board, which is a 2-dimensional array of Positions containing pieces.
	 * @param board The board to copy.
	 * @return The cloned board.
	 */
	public static Position[][] cloneBoard(Position[][] board) {
		Position[][] boardClone = new Position[8][8];
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				boardClone[i][j] = board[i][j].clone();
		
		return boardClone;
	}
	
	/**
	 * Applies a move to the board.
	 * @param opponentMove The opponent's last move.
	 */
	public void receiveMove(Move opponentMove) {
		//TODO: should the opposingPreviousMove save the translated or untranslated version?
		//      additionally, should the Move class have a translate() method which either swaps the
		//      coordinates inplace or which returns the translated move?
		opposingPreviousMove = opponentMove;
		applyMove(new Move(opponentMove.getFromTranslated(), opponentMove.getToTranslated(), opponentMove.getTimeTaken()));
	}
	
	/**
	 * Returns the last move of the opponent. Used for determining if En Passant
	 * is allowed.
	 * @return The opponent's last move
	 */
	public Move getPreviousMove() {
		return opposingPreviousMove;
	}
	
	/**
	 * Applies a move to the board and returns the move code.
	 * @param move Move to be applied.
	 * @return The code of the move, which could be a special case, e.g. Castle, En Passant
	 */
	public Code applyMove(Move move) {
		Coord fromCoord = move.getFrom();
		Coord toCoord = move.getTo();
		Position fromPosition = board[fromCoord.getRow()][fromCoord.getCol()];
		Position toPosition = board[toCoord.getRow()][toCoord.getCol()];
		
		Code returnCode = fromPosition.getPiece().moveCode(this, fromCoord,toCoord);

		//check if castle was applied and update rook
		if (returnCode == Code.CASTLE_LEFT) {
			Position rookFrom = board[fromCoord.getRow()][0];
			Position rookTo = board[fromCoord.getRow()][fromCoord.getCol() -1];
			rookTo.addPiece(rookFrom.getPiece());
			rookFrom.clearPiece();
		}
		else if (returnCode == Code.CASTLE_RIGHT) {
			Position rookFrom = board[fromCoord.getRow()][7];
			Position rookTo = board[fromCoord.getRow()][fromCoord.getCol() + 1];
			rookTo.addPiece(rookFrom.getPiece());
			rookFrom.clearPiece();
		}
		else if (returnCode == Code.EN_PASSANT) {
			int rowDifference = fromPosition.getRow() - toPosition.getRow();
			
			Position capturedPawnPosition = board[toPosition.getRow()+rowDifference][toPosition.getCol()];
			capturedPawnPosition.clearPiece();
		}
		
		toPosition.addPiece(fromPosition.getPiece());
		fromPosition.clearPiece();
		
		return returnCode;
	}
	
	/**
	 * Receives a move from the UI and validates to make sure the move is legal
	 * @param move Move of the player from the UI.
	 * @return Code of whether the move was successful or not.
	 */
	public Code validateAndApply(Move move) {
		
		Coord from = move.getFrom();
		Position fromPosition = board[from.getRow()][from.getCol()];
		
		//can't move enemy piece
		if (!fromPosition.isEmpty() && fromPosition.getPiece().getPlayer() == Player.PLAYER2) 
			return Code.NOT_LEGAL;
		
		//check if legal move
		ArrayList<Move> moves = fromPosition.getPiece().getMoves(this, from);
		if (moves == null || !moves.contains(move))
			return Code.NOT_LEGAL;
		
		//clone chess board, apply new move
		ChessBoard cloneBoard = new ChessBoard(this);
		Code returnCode = cloneBoard.applyMove(move);
				
		//check if king is moved into check, otherwise update the board to the new board
		if (cloneBoard.kingInCheck())
			return Code.IN_CHECK;
		else 
			board = cloneBoard.getBoard();
		
		return returnCode;
	}
	
	/**
	 * Checks if the game is over by Checkmate (your king) or Stalemate.
	 * If neither of these apply, it returns 
	 * a status code to continue.
	 * @return The status of the game: Checkmate (your king), Stalemate, or Game continues.
	 */
	public GameStatus gameOver() {
		
		//find if any of your possible moves will keep the king out of check.
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (board[r][c].isFriendly(Player.PLAYER1)) {
					ArrayList<Move> moves = board[r][c].getPiece().getMoves(this, new Coord(r, c));
					for (Move move : moves) {
						ChessBoard newBoardState = new ChessBoard(this);
						newBoardState.applyMove(move);
						
						if (!newBoardState.kingInCheck())
							return GameStatus.CONTINUE;
					}
				}
			}			
		}
		
		/**
		 * No possible moves where King is not in check. If the king is already in check, then it's Checkmate
		 * and you lose, Stalemate otherwise.
		 */
		if (kingInCheck())
			return GameStatus.CHECKMATE;
		else
			return GameStatus.STALEMATE;
	}
	
	/**
	 * Checks if the player's king is in check.
	 * @return True if the king is in check, false otherwise.
	 */
	public boolean kingInCheck() {
		Coord kingCoord = null;
		
		//find king's position
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if(board[r][c].isOwnType(PieceID.KING)) {
					kingCoord = new Coord(r, c);
					//THIS DOESN'T BREAK OUT OF DOUBLE LOOP
					break;
				}
			}
		}
		
		//find if any of opponent's pieces can capture king by going through moves
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (board[r][c].isEnemy(Player.PLAYER1)) {
					ArrayList<Move> moves = board[r][c].getPiece().getMoves(this, new Coord(r, c));
					for (Move move : moves) {
						if (move.getTo().equals(kingCoord))
							return true;
					}
				}
			}
		}
	
		return false;	
	}
	
	/**
	 * Determines whether a position on the board exists.
	 * @param row Row number.
	 * @param col Column number.
	 * @return True if the position exists, false otherwise.
	 */
	public static boolean validPosition(int row, int col) {
		return row >= 0 && row < 8 && col >=0 && col < 8; 
	}
	
	/**
	 * Returns the current gameboard, a 2-dimensional array of positions.
	 * @return The 2-dimensional array of positions.
	 */
	public Position[][] getBoard() {
		return board;
	}
	
	public Position getPosition(Coord cord) {
		return board[cord.getRow()][cord.getCol()];
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
	
	
	
	/******** FUNCTIONS FOR TESTING ********/
	
	/* Manually move piece from one position to another, don't check for validity */
	public void forceMove(Move move) {
		Coord fromCoord = move.getFrom();
		Coord toCoord = move.getTo();
		Position fromPosition = board[fromCoord.getRow()][fromCoord.getCol()];
		Position toPosition = board[toCoord.getRow()][toCoord.getCol()];
		
		toPosition.addPiece(fromPosition.getPiece());
		fromPosition.clearPiece();
	}
	
	/* Add pawns to various locations for testing available moves */
	public void addPawns(Player player, Coord... coords) {
		for (Coord c: coords) {
			board[c.getRow()][c.getCol()].addPiece(new Pawn(player));
		}
	}
	
	
	/* Clear entire row */
	public void clearRow(int row) {
		for(int c = 0; c < 8; c++) 
			board[row][c].clearPiece();
	}
	
	public void removePiece(Coord coord) {
		board[coord.getRow()][coord.getCol()].clearPiece();
	}
	
	/* Clear board except for particular position */
	public void clearBoardExcept(Coord coord) {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (r == coord.getRow() && c == coord.getCol())
					continue;
				else
					board[r][c].clearPiece();
			}
		}
	}
	
	/* Get moves available at position */
	public ArrayList<Move> getMoves(Coord cord) {
		return board[cord.getRow()][cord.getCol()].getPiece().getMoves(this, new Coord(cord.getRow(), cord.getCol()));
	}
	
	
}

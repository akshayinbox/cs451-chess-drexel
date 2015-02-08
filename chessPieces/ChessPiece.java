package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;

/**
 * An abstract class for a chess piece. Each piece contains a player, can determine its moves with a
 * chessboard, and also provides a move code (type of move performed, i.e. Castle, En Passant, etc.)
 * 
 */

public abstract class ChessPiece {

	protected Player player;
	protected PieceID id;
	
	/**
	 * Constructs a chess piece for designated player.
	 * @param player Player piece belongs to.
	 */
	ChessPiece (Player player) {
		this.player = player;
	}
	
	/**
	 * Returns the code of the move the piece just performed. The move Code is success if the piece
	 * moves from one position to another. Otherwise, the move may return a special code (e.g. Castle Left,
	 * Castle Right, En Passant, etc.) if the move is not a standard move from one position to another.
	 * @param cb
	 * @param from Previous position.
	 * @param to New position.
	 * @return Code of the move just performed.
	 */
	public Code moveCode(ChessBoard cb, Coord from, Coord to) {
		return Code.SUCCESS;
	}
	
	/**
	 * The player the piece belongs to.
	 * @return The id of the player the piece belongs to.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * The type of the piece, e.g. Knight, Pawn, etc.
	 * @return The type of the piece.
	 */
	public PieceID getID() {
		return id;
	}
	
	public String toString() {
		return id.getName() + player.ordinal();
	}
	
	/**
	 * Clones the chess piece and returns a copy of the clone.
	 * @return the clone of the piece.
	 */
	public abstract ChessPiece clone();
	
	/**
	 * Returns a list of all possible moves the piece can perform on the chessboard. Does NOT
	 * check if a move will put the King in check (this is the responsibility of the chessBoard).
	 * @param board The chessBoard to check for all possible moves.
	 * @param cord The coordinate of the piece.
	 * @return A list of all possible moves the piece may perform.
	 */
	public abstract ArrayList<Move> getMoves(ChessBoard board, Coord cord);
	
}

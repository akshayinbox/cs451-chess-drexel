package chessBoard;
import chessPieces.ChessPiece;
import chessPieces.PieceID;

/**
 * A class representing a position on a chessboard. Each position may also contain
 * a chess piece. This class provides some convenience methods that interface
 * the chess piece it contains.
 */


public class Position {
	private int row, col;
	ChessPiece piece;
	
	/**
	 * Constructs an empty position.
	 */
	public Position() {
		this.piece = null;
	}
	
	/**
	 * Constructs a position at a specified location
	 * @param row Position's row.
	 * @param col Position's column.
	 */
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Constructs a position with a piece.
	 * @param row Position's row.
	 * @param col Position's column.
	 * @param piece Piece the position will contain.
	 */
	public Position(int row, int col, ChessPiece piece) {
		this.row = row;
		this.col = col;
		this.piece = piece;
	}
	
	/**
	 * Returns a clone of the current position with the piece contained (if any).
	 * @return Cloned position.
	 */
	public Position clone() {
		ChessPiece clonePiece = null;
		if (piece != null)
			clonePiece = piece.clone(); 
		
		return new Position(row, col, clonePiece);
	}
	
	/**
	 * Returns the row.
	 * @return Row of the position.
	 */
	public int getRow() {
		return row;
	}
	
	/** 
	 * Returns the column.
	 * @return Column of the position
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Adds a chess piece to the position, removing the old piece.
	 * @param piece ChessPiece to add to position.
	 */
	public void addPiece(ChessPiece piece) {
		this.piece = piece;
	}
	
	/** 
	 * Returns the chess piece contained in this position, if any.
	 * @return The chess piece in this position, null if there isn't one.
	 */
	public ChessPiece getPiece() {
		return piece;
	}
	
	/**
	 * Returns true if chess piece is the opponent's piece and of specified type.
	 * @param id The player whose pieces are friendly pieces.
	 * @return true if the piece belongs to the opposing player and is the specified type, false otherwise.
	 */
	public boolean isOpposingType(PieceID id) {
		if (piece != null && piece.getPlayer() == Player.PLAYER2 && piece.getID() == id)
			return true;
		else
			return false;
	}
	
	/**
	 * Returns true if a piece is the specified type and is your own piece.
	 * @param id The id of the piece.
	 * @return True if the piece belongs to the player with the specified id, false otherwise.
	 */
	public boolean isOwnType(PieceID id) {
		if (piece != null && piece.getPlayer() == Player.PLAYER1 && piece.getID() == id)
			return true;
		else
			return false;
	}
	
	/**
	 * Removes the ChessPiece from the position.
	 */
	public void clearPiece() {
		piece = null;
	}
	
	/**
	 * Checks if the position is empty or not.
	 * @return True if the piece is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return piece == null;
	}
	
	/**
	 * Checks if a player owns a piece (i.e. not the opponent's piece).
	 * @param player The player to check if the piece is his or not.
	 * @return true if piece belongs to player, false otherwise
	 */
	public boolean isFriendly(Player player) {
		return piece != null && piece.getPlayer() == player;
	}
	
	/**
	 * Checks if a the position is empty or belongs to opposing player's piece.
	 * @param player Id of the player
	 * @return true if the position is empty or contains opposing piece, false otherwise.
	 */
	public boolean isEmptyOrEnemy(Player player) {
		return isEmpty() || isEnemy(player);
	}
	
	/**
	 * Checks if a positions belongs to the enemy
	 * @param player Id of the player
	 * @return true if piece belongs to player, false otherwise.
	 */
	public boolean isEnemy(Player player) {
		return piece != null && piece.getPlayer() != player;
	}
	
	public String toString() {
		if (piece == null)
			return "  ";
		else
			return piece.toString();
	}
}

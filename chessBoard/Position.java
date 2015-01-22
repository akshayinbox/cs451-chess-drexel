package chessBoard;
import chessPieces.ChessPiece;
import chessPieces.PieceID;


//an interface for the piece

public class Position {
	private int row, col;
	ChessPiece piece;
	
	public Position() {
		this.piece = null;
	}
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Position(int row, int col, ChessPiece piece) {
		this.row = row;
		this.col = col;
		this.piece = piece;
	}
	
	public Position clone() {
		ChessPiece clonePiece = null;
		if (piece != null)
			clonePiece = piece.clone(); 
		
		return new Position(row, col, clonePiece);
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void addPiece(ChessPiece piece) {
		this.piece = piece;
	}
	
	public ChessPiece getPiece() {
		return piece;
	}
	
	//checks if square is an opposing piece and of specified type
	public boolean isOpposingType(PieceID id) {
		if (piece != null && piece.getPlayer() == Player.PLAYER2 && piece.getID() == id)
			return true;
		else
			return false;
	}
	
	//checks if square is your own piece and of specified type
	public boolean isOwnType(PieceID id) {
		if (piece != null && piece.getPlayer() == Player.PLAYER1 && piece.getID() == id)
			return true;
		else
			return false;
	}
	
	public void clearPiece() {
		piece = null;
	}
	
	public boolean isEmpty() {
		return piece == null;
	}
	
	public boolean isEmptyOrEnemy() {
		return isEmpty() || isEnemy();
	}
	
	public boolean isEnemy() {
		return piece == null || piece.getPlayer() == Player.PLAYER2;
	}
	
	public String toString() {
		if (piece == null)
			return "  ";
		else
			return piece.toString();
	}
}

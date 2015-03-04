package chessBoard;

import java.io.Serializable;

import UI.PieceUI;
import chessPieces.ChessPiece;

public class Promotion implements Serializable {

	private static final long serialVersionUID = -4287881630070680391L;
	private PieceUI promotedPiece;
	private Move move;
	
	/**
	 * Constructs a promotion object consisting of the move causing the promotion and the chosen new piece.
	 * @param move Piece movement.
	 * @param piece New chess piece.
	 */
	public Promotion(Move move, PieceUI piece) {
		this.move = move;
		this.promotedPiece = piece;
	}
	
	public Move getMove() {
		return this.move;
	}

	public void setMove(Move move) {
		this.move = move;
	}
	
	public PieceUI getPiece() {
		return this.promotedPiece;
	}
	
	public void setPiece(PieceUI piece) {
		this.promotedPiece = piece;
	}
}

package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;

public abstract class ChessPiece implements java.io.Serializable {
	private static final long serialVersionUID = 1009526255178159146L;
	protected Player player;
	protected PieceID id;
	
	ChessPiece (Player player) {
		this.player = player;
	}
	
	public Code moveCode(ChessBoard cb, Coord from, Coord to) {
		return Code.SUCCESS;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PieceID getID() {
		return id;
	}
	
	public String toString() {
		return id.getName() + player.ordinal();
	}
	
	public abstract ChessPiece clone();
	
	public abstract ArrayList<Move> getMoves(ChessBoard board, Coord cord);
	
}

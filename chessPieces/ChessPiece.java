package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;

public abstract class ChessPiece {

	protected Player player;
	protected PieceID id;
	
	ChessPiece (Player player) {
		this.player = player;
	}
	
	public Code moveTo(Coord coord) {
		return Code.SUCCESS;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PieceID getID() {
		return id;
	}
	
	public abstract ChessPiece clone();
	
	public abstract ArrayList<Move> getMoves(ChessBoard cb, Coord cord);
	
}

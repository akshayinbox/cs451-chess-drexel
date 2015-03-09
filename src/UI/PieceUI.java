package UI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;

import chessBoard.Player;
import chessPieces.ChessPiece;

public class PieceUI extends JButton{
	private Player playerCode;
	private ChessPiece piece;
	
	/**
	 * New representation for a chess piece.
	 */
	public PieceUI() {
		enableInputMethods(true);
	}
	
	public Player getPlayer() {
		return playerCode;
	}
	
	public void setPlayer(Player code) {
		this.playerCode = code;
	}
	
	public ChessPiece getPiece() {
		return piece;
	}
	
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}
	
	public void onDragged() {
		
	}
}

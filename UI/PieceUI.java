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
	
	public PieceUI() {
		enableInputMethods(true);
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
//				System.out.println(getPlayer());
			}
		});
//		this.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				System.out.println(e);
//			}
//		});
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

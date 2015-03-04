package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import chessBoard.Player;
import chessPieces.Bishop;
import chessPieces.Knight;
import chessPieces.Queen;
import chessPieces.Rook;


public class PromotionPanel extends JPanel implements Serializable {

	private static final long serialVersionUID = -1850482398313870606L;

	private PieceUI selected = null;
	
	public PromotionPanel (Boolean host) throws IOException {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		PieceUI btnKnight = new PieceUI();
		PieceUI btnBishop = new PieceUI();
		PieceUI btnRook = new PieceUI();
		PieceUI btnQueen = new PieceUI();
		
		btnKnight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = (PieceUI)e.getSource();
			}
		});
		btnBishop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = (PieceUI)e.getSource();
			}
		});
		btnRook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = (PieceUI)e.getSource();
			}
		});
		btnQueen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = (PieceUI)e.getSource();
			}
		});
		
		BufferedImage knightButtonIcon;
		BufferedImage bishopButtonIcon;
		BufferedImage rookButtonIcon;
		BufferedImage queenButtonIcon;
		Player playerCode = Player.PLAYER1;
		
		if (host){
			knightButtonIcon = ImageIO.read(new File("pieces/WhiteN_board.png"));
			bishopButtonIcon = ImageIO.read(new File("pieces/WhiteB_board.png"));
			rookButtonIcon = ImageIO.read(new File("pieces/WhiteR_board.png"));
			queenButtonIcon = ImageIO.read(new File("pieces/WhiteQ_board.png"));
			
		} else {
			knightButtonIcon = ImageIO.read(new File("pieces/BrownN_board.png"));
			bishopButtonIcon = ImageIO.read(new File("pieces/BrownB_board.png"));
			rookButtonIcon = ImageIO.read(new File("pieces/BrownR_board.png"));
			queenButtonIcon = ImageIO.read(new File("pieces/BrownQ_board.png"));
		}
		btnKnight.setIcon(new ImageIcon(knightButtonIcon));
		btnKnight.setPlayer(playerCode);
		btnKnight.setPiece(new Knight(playerCode));
		
		btnBishop.setIcon(new ImageIcon(bishopButtonIcon));
		btnBishop.setPlayer(playerCode);
		btnBishop.setPiece(new Bishop(playerCode));
		
		btnRook.setIcon(new ImageIcon(rookButtonIcon));
		btnRook.setPlayer(playerCode);
		btnRook.setPiece(new Rook(playerCode));
		
		btnQueen.setIcon(new ImageIcon(queenButtonIcon));
		btnQueen.setPlayer(playerCode);
		btnQueen.setPiece(new Queen(playerCode));
		
		add(btnKnight);
		add(btnBishop);
		add(btnRook);
		add(btnQueen);
	}
	
	public PieceUI getSelected() {
		return this.selected;
	}
}

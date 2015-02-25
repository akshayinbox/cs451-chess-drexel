package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;
import chessPieces.Bishop;
import chessPieces.ChessPiece;
import chessPieces.King;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.Queen;
import chessPieces.Rook;
import chessNetwork.client.Client;

public class ChessboardUI extends JPanel implements Serializable {
	private static final long serialVersionUID = -5553944313833181921L;
	private ChessBoard chessBoard;
	private JPanel board;
	private transient UI windowUI;
	private Boolean canMove;
	private int thisSecLeft;
	private int opSecLeft;
	private final static Color DARK_BROWN = new Color(79, 8 ,4);
	private final static Color LIGHT_BROWN = new Color(244, 196, 120);
	private final static int BOARD_ROWS = 8;
	private final static int BOARD_COLS = 8;
	private final static String[][] boardRep = new String[BOARD_ROWS][BOARD_COLS];

	public ChessboardUI(JPanel panel, UI window) throws IOException {
		windowUI = window;
		board = panel;
		board.setBorder(new LineBorder(Color.BLACK));
		board.setPreferredSize(new Dimension(500, 500));
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				JPanel square = new JPanel();
				square.setName(Integer.toString(j) + "," + Integer.toString(i));
				square.setBackground(i%2 == j%2 ? LIGHT_BROWN : DARK_BROWN);
				square.setBorder(new EmptyBorder(5, 5, 5, 5));
//				square.setPreferredSize(new Dimension(50, 50));
				board.add(square);
				boardRep[i][j] = setSquareRep(i, j);
			}
		}
	}
	
	public ChessBoard getChessBoard() {
		return this.chessBoard;
	}
	
	public JPanel getBoardUI() {
		return this.board;
	}
	
	public void repaintBoard() {
		for( Component p : board.getComponents()) {
			((JPanel) p).updateUI();
			//**TESTING/DEBUGGING CODE BELOW ****//
			for(Component piece : ((JPanel) p).getComponents()) {
				piece.repaint();
				System.out.println(((PieceUI) piece).getPlayer().toString() + " " + ((PieceUI) piece).getPiece().toString());
			}
			//***TESTING/DEBUGGING CODE ABOVE****//
			
			
		}
		System.out.println("Length = " + board.getComponents().length);
		board.repaint();
	}
	
	public void setChessBoard(ChessBoard cb) {
		this.chessBoard = cb;
	}
	
	public void setCanMove(Boolean can) {
		this.canMove = can;
	}
	
	public Boolean getCanMove() {
		return this.canMove;
	}
	
	public void setOpTimeLeft(int secLeft) {
		this.opSecLeft = secLeft;
	}
	
	/**
	 * After a move has been applied, fixes the UI of the board to match the
	 * new ChessBoard.
	 */
	public void updateBoard() {
		ArrayList<JPanel> before = new ArrayList<JPanel>();
		ArrayList<JPanel> after = new ArrayList<JPanel>();
		ArrayList<String> beforeId = new ArrayList<String>();
		ArrayList<String> afterId = new ArrayList<String>();
		
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				int compIndex = i * BOARD_COLS + j;
				JPanel square = (JPanel)board.getComponent(compIndex);
				
				int cCount = square.getComponentCount();
				Position pos = chessBoard.getPosition(new Coord(i, j));
				
				if (cCount != 0) {
					PieceUI pUI = (PieceUI) square.getComponent(0);
					
					//piece moved from square
					if(pos.isEmpty())  {
						before.add(square);
						beforeId.add(pUI.getPiece().toString());
					}
					//piece moved to square and displaced previous piece
					else if(!pos.getPiece().toString().equals(pUI.getPiece().toString())) {
						System.out.println("Chessboard says " + pos.getPiece().toString());
						System.out.println(pUI.getPiece().toString());
						after.add(square);
						afterId.add(pos.getPiece().toString());
					}
				}
				
				//piece moved to empty square
				if (cCount == 0 && !pos.isEmpty()) {
					after.add(square);
					afterId.add(pos.getPiece().toString());
				}
			}
		}
		
		//swap the old pieces positions on board to the new positions
		for(int i = 0; i < after.size(); i++) {
			for (int j = 0; j < before.size(); j++) {
				if (afterId.get(i).equals(beforeId.get(j))) {
					JPanel oldComp = before.get(j);
					JPanel newComp = after.get(i);
					newComp.removeAll();
					newComp.add(oldComp.getComponent(0));
				}
			}
		}
		
		for (int i = 0; i < before.size(); i++) {
			JPanel oldComp = before.get(i);
			if (oldComp.getComponentCount() == 0)
				continue;
			PieceUI pUI = (PieceUI) oldComp.getComponent(0);
			if (pUI.getPiece().toString().equals(beforeId.get(i))) {
				oldComp.removeAll();
			}
		}
		
		board.repaint();
	}
	
	public void addAllPieces(Boolean host, final Client client) throws IOException {
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				JPanel temp = new JPanel();
				PieceUI b = new PieceUI();
				b.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (canMove) {
							Move m = createMove(e);
							if (m != null) {
								System.out.println(m);
								Code result = chessBoard.validateAndApply(m);
								if (result.getCode() > 0) {
									if (windowUI.getThisTimer() != null)
										windowUI.getThisTimer().stop();
									updateBoard();
									//uiApplyMove(e);
									client.send(m);
									PieceUI piece = (PieceUI)e.getSource();
									String pieceName = piece.getPiece().getClass().getName().replace("chessPieces.", "");
									windowUI.addToMoveList("You: " + pieceName + " " + boardRep[m.getFrom().getRow()][m.getFrom().getCol()] + " to " + boardRep[m.getTo().getRow()][m.getTo().getCol()]);
									windowUI.setThisSecLeft(windowUI.getThisSecLeft() - m.getTimeTaken());
									thisSecLeft = windowUI.getThisSecLeft() - m.getTimeTaken();
									canMove = false;
									System.out.println(chessBoard.toString());
								} else if (result.equals(Code.IN_CHECK))
									JOptionPane.showMessageDialog(null,
										    "You cannot move yourself into check.",
										    "",
										    JOptionPane.WARNING_MESSAGE);
								else
									JOptionPane.showMessageDialog(null,
										    "Invalid Move",
										    "",
										    JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				});
				b.setBorder(BorderFactory.createEmptyBorder());
				b.setContentAreaFilled(false);
//				System.out.println(i + "," + j);
				//int compIndex = 0;
//				if (host)
					int compIndex = i * BOARD_COLS + j;
					
					if(!host) {
						if (j == 3)
							compIndex = compIndex + 1;
						else if (j == 4)
							compIndex = compIndex - 1;
					}
//				else
//					compIndex = (BOARD_COLS*BOARD_ROWS - 1) - (i * BOARD_COLS + j);
				switch(i) {
					case 0:
					case 7: {
						//PROBLEM: player who joins has his pieces labled as PLAYER2
						switch(j) {
						case 0:
						case 7: {b = addRook(b, i, host); temp = (JPanel)board.getComponent(compIndex); break;}
						case 1:
						case 6: {b = addKnight(b, i, host); temp = (JPanel)board.getComponent(compIndex); break;}
						case 2:
						case 5: {b = addBishop(b, i, host); temp = (JPanel)board.getComponent(compIndex); break;}
						case 3: {b = addQueen(b, i, host); temp = (JPanel)board.getComponent(compIndex); break;}
						case 4: {b = addKing(b, i, host); temp = (JPanel)board.getComponent(compIndex); break;}
						}
						break;
					}
					case 1: 
					case 6: {b = addPawn(b, i, host); temp = (JPanel)board.getComponent(compIndex); break;}
					
				}
//				System.out.println(temp);
				temp.add(b);
				temp.updateUI();
//				board.add(square);
			}
		}
//		boardPane.add(board);
//		board.updateUI();
	}
	
	public void clearAllPieces() {
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				int compIndex = getComponentIndex(i, j);
				JPanel square = (JPanel)board.getComponent(compIndex);
				square.removeAll();
			}
		}
		board.updateUI();
	}

	public void receiveMove(Move m) {
		//apply the move to the actual model of the chess board
		chessBoard.receiveMove(m);
		System.out.println("After receiving move: ");
		System.out.println(chessBoard);

//		//then apply it to the UI
		Coord from = m.getFromTranslated();
		Coord to = m.getToTranslated();
		int fromCompIndex = getComponentIndex(from.getRow(), from.getCol());
		int toCompIndex = getComponentIndex(to.getRow(), to.getCol());
		//get the piece component
		Component oldComp = ((JPanel) board.getComponent(fromCompIndex)).getComponents()[0];
		//get the new square onto which the peice will be palced
		JPanel newComp = (JPanel) board.getComponent(toCompIndex);
		//then finally move the piece
		//movePiece(oldComp, newComp);

		PieceUI piece = (PieceUI)oldComp;
		String pieceName = piece.getPiece().getClass().getName().replace("chessPieces.", "");
		windowUI.addToMoveList("Opp: " + pieceName + " " + boardRep[from.getRow()][from.getCol()] + " to " + boardRep[to.getRow()][to.getCol()]);
		updateBoard();
		canMove = true;
	}

	private String setSquareRep(int row, int col) {
		row += 1;
		char letter = (char) (col + 65);
		return Character.toString(letter) + Integer.toString(row);
	}
	
	private int getComponentIndex(int row, int column) {
		return row * BOARD_COLS + column;
	}

	private void movePiece(Component piece, JPanel newSquare) {
		piece.getParent().remove(piece);
		newSquare.removeAll();
		newSquare.add(piece);
		board.repaint();
	}
	
	private void uiApplyMove(MouseEvent e) {
		Component oldComp = e.getComponent();
		JPanel newComp = (JPanel) getClosestComponent(e);
		movePiece(oldComp, newComp);
	}

	private Move createMove(MouseEvent e) {
		Component origin = e.getComponent();
		Container square = origin.getParent();
		Component oldComp = board.getComponentAt(square.getX(), square.getY());
		String[] originLocation = oldComp.getName().split(",");
		int originCol = Integer.parseInt(originLocation[0]);
		int originRow = Integer.parseInt(originLocation[1]);
		
		Component newComp = getClosestComponent(e);
		if (newComp == null)
			return null;
		String[] newLocation = newComp.getName().split(",");
		int newCol = Integer.parseInt(newLocation[0]);
		int newRow = Integer.parseInt(newLocation[1]);
		int secLeft = windowUI.getThisSecLeft();
		int currentMin = windowUI.getThisMin();
		int currentSec = windowUI.getThisSec();
		int totalTime = secLeft - (60*currentMin + currentSec);
		Move m = new Move(new Coord(originRow, originCol), new Coord(newRow, newCol), totalTime);
		return m;
	}
	
	private Component getClosestComponent(MouseEvent e) {
		Component c = e.getComponent();
		Container square = c.getParent();
		int comX = square.getX();
		int comY = square.getY();
		int x = e.getX();
		int y = e.getY();
		return board.getComponentAt(x + comX, y + comY);
	}
		
	private PieceUI addPawn(PieceUI button, int row, boolean isHost) throws IOException {
		BufferedImage buttonIcon;
		Player playerCode;
		if (row == 1) {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/BrownP_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/WhiteP_board.png"));
			playerCode = Player.PLAYER2;
		} else {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/WhiteP_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/BrownP_board.png"));
			playerCode = Player.PLAYER1;
		}
		
		button.setIcon(new ImageIcon(buttonIcon));
		button.setPlayer(playerCode);
		button.setPiece(new Pawn(playerCode));
		return button;
	}
	
	private PieceUI addRook(PieceUI button, int row, boolean isHost) throws IOException {
		BufferedImage buttonIcon;
		Player playerCode;
		if (row == 0) {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/BrownR_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/WhiteR_board.png"));
			playerCode = Player.PLAYER2;
		} else {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/WhiteR_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/BrownR_board.png"));
			playerCode = Player.PLAYER1;
		}
		button.setIcon(new ImageIcon(buttonIcon));
		button.setPlayer(playerCode);
		button.setPiece(new Rook(playerCode));
		return button;
	}
	
	private PieceUI addKnight(PieceUI button, int row, boolean isHost) throws IOException {
		BufferedImage buttonIcon;
		Player playerCode;
		if (row == 0) {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/BrownN_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/WhiteN_board.png"));
			playerCode = Player.PLAYER2;
		} else {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/WhiteN_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/BrownN_board.png"));
			playerCode = Player.PLAYER1;
		}
		button.setIcon(new ImageIcon(buttonIcon));
		button.setPlayer(playerCode);
		button.setPiece(new Knight(playerCode));
		return button;
	}
	
	private PieceUI addBishop(PieceUI button, int row, boolean isHost) throws IOException {
		BufferedImage buttonIcon;
		Player playerCode;
		if (row == 0) {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/BrownB_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/WhiteB_board.png"));
			playerCode = Player.PLAYER2;
		} else {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/WhiteB_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/BrownB_board.png"));
			playerCode = Player.PLAYER1;
		}
		button.setIcon(new ImageIcon(buttonIcon));
		button.setPlayer(playerCode);
		button.setPiece(new Bishop(playerCode));
		return button;
	}
	
	private PieceUI addQueen(PieceUI button, int row, boolean isHost) throws IOException {
		BufferedImage buttonIcon;
		Player playerCode;
		if (row == 0) {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/BrownQ_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/WhiteQ_board.png"));
			playerCode = Player.PLAYER2;
		} else {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/WhiteQ_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/BrownQ_board.png"));
			playerCode = Player.PLAYER1;
		}
		button.setIcon(new ImageIcon(buttonIcon));
		button.setPlayer(playerCode);
		button.setPiece(new Queen(playerCode));
		return button;
	}
	
	private PieceUI addKing(PieceUI button, int row, boolean isHost) throws IOException {
		BufferedImage buttonIcon;
		Player playerCode;
		if (row == 0) {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/BrownK_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/WhiteK_board.png"));
			playerCode = Player.PLAYER2;
		} else {
			if (isHost)
				buttonIcon = ImageIO.read(new File("pieces/WhiteK_board.png"));
			else
				buttonIcon = ImageIO.read(new File("pieces/BrownK_board.png"));
			playerCode = Player.PLAYER1;
		}
		button.setIcon(new ImageIcon(buttonIcon));
		button.setPlayer(playerCode);
		button.setPiece(new King(playerCode));
		return button;
	}
}

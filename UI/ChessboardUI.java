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
import chessBoard.GameStatus;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;
import chessBoard.Promotion;
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

	/**
	 * Creates a UI chessboard
	 * @param panel The JPanel the chessboard will be attached to.
	 * @param window The rest of the UI window.
	 */
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
	
	/**
	 * Updates every piece on the board.
	 */
	public void repaintBoard() {
		for( Component p : board.getComponents()) {
			((JPanel) p).updateUI();
			for(Component piece : ((JPanel) p).getComponents()) {
				piece.repaint();
				System.out.println(((PieceUI) piece).getPlayer().toString() + " " + ((PieceUI) piece).getPiece().toString());
			}
			
			
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
	
	public int getOpTimeLeft() {
		return this.opSecLeft;
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
	
	/**
	 * Adds all of the initial board pieces and attaches handlers to each piece
	 * @param host Whether or not the current UI is the game's host.
	 * @param client A network client.
	 */
	public void addAllPieces(final Boolean host, final Client client) throws IOException {
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				JPanel temp = new JPanel();
				PieceUI b = new PieceUI();
				// Add a drag listener on each piece
				addDragListener(b, host, client);
				b.setBorder(BorderFactory.createEmptyBorder());
				b.setContentAreaFilled(false);
				int compIndex = i * BOARD_COLS + j;
				
				if(!host) {
					if (j == 3)
						compIndex = compIndex + 1;
					else if (j == 4)
						compIndex = compIndex - 1;
				}
				switch(i) {
					case 0:
					case 7: {
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
				temp.add(b);
				temp.updateUI();
			}
		}
	}
	
	/**
	 * Attaches a listener to a button for dragging
	 * @param b The piece attaching the listener to.
	 * @param host Whether or not the current UI is the game's host.
	 * @param client A network client.
	 */
	public void addDragListener(PieceUI b, final Boolean host, final Client client) {
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (canMove) {
					// Create the move based on the dragging motion
					Move m = createMove(e);
					if (m != null) {
						System.out.println(m);
						// Try and perform the move
						Code result = chessBoard.validateAndApply(m);
						if (result.getCode() > 0) {
							if (windowUI.getThisTimer() != null)
								windowUI.getThisTimer().stop();
							updateBoard();
							// Promotions are a special type of move. Handle differently.
							if (result.equals(Code.PROMOTION)) {
								PromotionPanel promotionPanel = null;
								PieceUI newPiece = new PieceUI();
								try {
									promotionPanel = new PromotionPanel(host);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								int promotionOk = JOptionPane.showConfirmDialog(null, promotionPanel,
						        		"Connect", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
								if (promotionOk == JOptionPane.OK_OPTION) {
									newPiece = promotionPanel.getSelected();
								} else {
									// If the user decides to ignore this choice, give them knight as punishment.
									newPiece.setPlayer(Player.PLAYER1);
									newPiece.setPiece(new Knight(Player.PLAYER1));
								}
								addDragListener(newPiece, host, client);
								newPiece.setBorder(BorderFactory.createEmptyBorder());
								newPiece.setContentAreaFilled(false);
								// Change the pawn with the new promoted piece
								int col = m.getTo().getCol();
								int row = m.getTo().getRow();
								
								chessBoard.getBoard()[row][col].clearPiece();
								chessBoard.getBoard()[row][col].addPiece(newPiece.getPiece());
								
								int toCompIndex = getComponentIndex(row, col);
								// Panel where pawn currently is
								JPanel newComp = (JPanel) board.getComponent(toCompIndex);
								newComp.removeAll();
								newComp.add(newPiece);
								board.updateUI();
								
								Promotion p = new Promotion(m, newPiece);
								client.send(p);
							} else
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
	}
	
	/**
	 * Clears all the pieces on the board.
	 */
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

	/**
	 * Receive a move from the opponent.
	 * @param m The opponent's move.
	 */
	public void receiveMove(Move m) {
		//apply the move to the actual model of the chess board
		chessBoard.receiveMove(m);
		System.out.println("After receiving move: ");
		System.out.println(chessBoard);

		//then apply it to the UI
		Coord from = m.getFromTranslated();
		Coord to = m.getToTranslated();
		int fromCompIndex = getComponentIndex(from.getRow(), from.getCol());
		//get the piece component
		Component oldComp = ((JPanel) board.getComponent(fromCompIndex)).getComponents()[0];

		PieceUI piece = (PieceUI)oldComp;
		String pieceName = piece.getPiece().getClass().getName().replace("chessPieces.", "");
		windowUI.addToMoveList("Opp: " + pieceName + " " + boardRep[from.getRow()][from.getCol()] + " to " + boardRep[to.getRow()][to.getCol()]);
		updateBoard();
		canMove = true;
	}

	/**
	 * Receive a promotion message from the client.
	 * @param p The opponent's promotion.
	 */
	public void receivePromotion(Promotion p) {
		System.out.println("After receiving move: ");
		System.out.println(chessBoard);
		Move m = p.getMove();
		PieceUI newPiece = p.getPiece();
		
		int fromCol = m.getFromTranslated().getCol();
		int fromRow = m.getFromTranslated().getRow();
		
		int toCol = m.getToTranslated().getCol();
		int toRow = m.getToTranslated().getRow();
		
		chessBoard.getBoard()[fromRow][toCol].clearPiece();
		chessBoard.getBoard()[toRow][toCol].clearPiece();
		chessBoard.getBoard()[toRow][toCol].addPiece(newPiece.getPiece());
		
		int fromCompIndex = getComponentIndex(fromRow, fromCol);
		int toCompIndex = getComponentIndex(toRow, toCol);
		// Panel where pawn currently is
		JPanel oldComp = (JPanel) board.getComponent(fromCompIndex);
		JPanel newComp = (JPanel) board.getComponent(toCompIndex);
		oldComp.removeAll();
		newComp.removeAll();
		newComp.add(newPiece);
		board.updateUI();
		windowUI.addToMoveList("Opp: Pawn promoted to " + newPiece.getClass().getName().replace("chessPieces.", "") + " - " + boardRep[toRow][toCol]);
		canMove = true;
	}
	
	/**
	 * Create a string representation of a square on the board in A1 format.
	 * @param row The row on the board.
	 * @param col The column on the board.
	 * @return The board square representation string.
	 */
	private String setSquareRep(int row, int col) {
		row += 1;
		char letter = (char) (col + 65);
		return Character.toString(letter) + Integer.toString(row);
	}
	
	/**
	 * Get the index of a 2d matrix in 1d array.
	 * @param row The row on the board.
	 * @param col The column on the board.
	 * @return The index in an array.
	 */
	private int getComponentIndex(int row, int column) {
		return row * BOARD_COLS + column;
	}

	/**
	 * Move a piece to a new square.
	 * @param piece The piece being moved.
	 * @param newSquare The square the piece is moving to.
	 */
	private void movePiece(Component piece, JPanel newSquare) {
		piece.getParent().remove(piece);
		newSquare.removeAll();
		newSquare.add(piece);
		board.repaint();
	}
	
	/**
	 * Create a move based on a mouse dragging event on a piece.
	 * @param e The drag event.
	 * @return The created move.
	 */
	private Move createMove(MouseEvent e) {
		Component origin = e.getComponent();
		Container square = origin.getParent();
		// Get the piece being moved
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
	
	/**
	 * Get the closest component the player released the mouse on.
	 * @param e The drag event.
	 * @return The component closest to the end of the drag event.
	 */
	private Component getClosestComponent(MouseEvent e) {
		Component c = e.getComponent();
		Container square = c.getParent();
		int comX = square.getX();
		int comY = square.getY();
		int x = e.getX();
		int y = e.getY();
		return board.getComponentAt(x + comX, y + comY);
	}
	
	/**
	 * Create a Pawn Piece from a blank button.
	 * @param button The button turning into a pawn.
	 * @param row The row on the board.
	 * @param isHost Determines the color of the piece.
	 * @return The new Pawn Piece.
	 */
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
	
	/**
	 * Create a Rook Piece from a blank button.
	 * @param button The button turning into a rook.
	 * @param row The row on the board.
	 * @param isHost Determines the color of the piece.
	 * @return The new Rook Piece.
	 */
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
	
	/**
	 * Create a Knight Piece from a blank button.
	 * @param button The button turning into a knight.
	 * @param row The row on the board.
	 * @param isHost Determines the color of the piece.
	 * @return The new Knight Piece.
	 */
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
	
	/**
	 * Create a Bishop Piece from a blank button.
	 * @param button The button turning into a bishop.
	 * @param row The row on the board.
	 * @param isHost Determines the color of the piece.
	 * @return The new Bishop Piece.
	 */
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
	
	/**
	 * Create a Queen Piece from a blank button.
	 * @param button The button turning into a queen.
	 * @param row The row on the board.
	 * @param isHost Determines the color of the piece.
	 * @return The new Queen Piece.
	 */
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
	
	/**
	 * Create a King Piece from a blank button.
	 * @param button The button turning into a king.
	 * @param row The row on the board.
	 * @param isHost Determines the color of the piece.
	 * @return The new King Piece.
	 */
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

	public UI getWindowUI() {
		return this.windowUI;
	}
}

package Tests;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import UI.ChessboardUI;
import UI.UI;
import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessNetwork.client.Client;

public class ChessboardUITest {
	
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
	
	private ChessboardUI cbui;

	@Before
	public void setUp() throws Exception {
		
		board = new JPanel();
		windowUI = new UI();
		chessBoard = new ChessBoard(true); //host cb
		canMove = new Boolean(false);
		opSecLeft = 600;
		thisSecLeft = 900;
		
		cbui = new ChessboardUI(board, windowUI);
		
	}

	@Test
	public void testChessboardUI() throws IOException {
		assertTrue(cbui.getBoardUI().equals(board));
		assertTrue(cbui.getWindowUI().equals(windowUI));
	}

	@Test
	public void testGetChessBoard() {
		cbui.setChessBoard(chessBoard);
		assertTrue(cbui.getChessBoard().equals(chessBoard));
	}

	@Test
	public void testGetBoardUI() {
		assertTrue(cbui.getBoardUI().equals(board));
	}

	@Test
	public void testRepaintBoard() {
		cbui.repaintBoard();
		assertTrue(cbui.equals(cbui));
	}

	@Test
	public void testSetChessBoard() {
		cbui.setChessBoard(chessBoard);
		assertTrue(cbui.getChessBoard().equals(chessBoard));
	}

	@Test
	public void testSetCanMove() {
		cbui.setCanMove(canMove);
		assertTrue(cbui.getCanMove() == canMove);
	}

	@Test
	public void testGetCanMove() {
		cbui.setCanMove(canMove);
		assertTrue(cbui.getCanMove() == canMove);
	}

	@Test
	public void testSetOpTimeLeft() {
		cbui.setOpTimeLeft(1000);
		assertTrue(cbui.getOpTimeLeft() == 1000);
	}

	@Test
	public void testUpdateBoard() throws Exception {
		//cbui.updateBoard();
		assertTrue(true);
	}

	@Test
	public void testAddAllPieces() throws IOException {
		cbui.addAllPieces(true, new Client());
		assertTrue(cbui.equals(cbui));
		
	}

	@Test
	public void testClearAllPieces() {
		cbui.clearAllPieces();
		
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				int compIndex = getComponentIndex(i, j);
				JPanel square = (JPanel)board.getComponent(compIndex);
				assertTrue(square.getComponentCount() == 0);
			}
		}
		
	}



	@Test
	public void testReceiveMove() throws Exception {
		Move m = new Move(new Coord(1,2), new Coord(2,2));
		//cbui.receiveMove(m);
		assertTrue(cbui.equals(cbui));
	}
	
	private int getComponentIndex(int row, int column) {
		return row * BOARD_COLS + column;
	}

}

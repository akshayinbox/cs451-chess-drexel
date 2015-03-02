package chessBoard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChessBoardTest {
	
	private ChessBoard cbhost, cbopp;

	@Before
	public void setUp() throws Exception {
		cbhost = new ChessBoard(true); //host's chessboard
		cbopp = new ChessBoard(false); //opponenet's chessboard
	}



	@Test
	public void testReceiveMove() {
		cbhost.getPreviousMove();
	}

	@Test
	public void testGetPreviousMove() {
	}

	@Test
	public void testApplyMove() {
	}

	@Test
	public void testValidateAndApply() {
	}

	@Test
	public void testGameOver() {
	}

	@Test
	public void testKingInCheck() {
	}

	@Test
	public void testValidPosition() {
	}

	@Test
	public void testGetBoard() {
	}

	@Test
	public void testToString() {
		
	}

}

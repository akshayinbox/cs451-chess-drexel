package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;
import chessPieces.ChessPiece;
import chessPieces.Rook;

public class ChessPieceTest {

	public ChessPiece cp;
	
	@Before
	public void setUp() throws Exception {
		cp = new Rook(Player.PLAYER2);
	}

	@Test
	public void testChessPiece() {
		assertTrue(cp.getPlayer() == Player.PLAYER2);
	}

	@Test
	public void testMoveCode() {
	}

	@Test
	public void testGetPlayer() {
		assertTrue(cp.getPlayer() == Player.PLAYER2);
	}

	@Test
	public void testGetID() {
		assertTrue(cp.getID().getName().equals("R"));
	}

	@Test
	public void testToString() {
		assertTrue(cp.toString().equals("R1"));
	}


}

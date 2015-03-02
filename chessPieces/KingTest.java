package chessPieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class KingTest {

	public King king;
	
	@Before
	public void setUp() throws Exception {
		king = new King(Player.PLAYER1);
	}



	@Test
	public void testKingPlayer() {
		assertTrue(king.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testKingPlayerBoolean() {
		king = new King(Player.PLAYER1, true);
		assertTrue(king.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testClone() {
		King k = king.clone();
		assertTrue(k.getID() == king.getID());
	}

}

package chessPieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class BishopTest {

	Bishop b, c;
	
	@Before
	public void setUp() throws Exception {
		b = new Bishop(Player.PLAYER1);
	}


	@Test
	public void testBishop() {
		assertTrue(b.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testClone() {
		c = b.clone();
		assertTrue(c.getPlayer() == b.getPlayer());
	}

}

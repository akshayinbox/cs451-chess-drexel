package chessPieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class KnightTest {
	
	public Knight knight;

	@Before
	public void setUp() throws Exception {
		knight = new Knight(Player.PLAYER1);
	}


	@Test
	public void testKnight() {
		assertTrue(knight.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testClone() {
		Knight k = knight.clone();
		assertTrue(k.getID() == knight.getID());
	}

}

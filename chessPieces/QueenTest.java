package chessPieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class QueenTest {

	Queen queen;
	
	@Before
	public void setUp() throws Exception {
		queen = new Queen(Player.PLAYER2);
	}

	@Test
	public void testQueen() {
		assertTrue(queen.getPlayer() == Player.PLAYER2);
	}

	@Test
	public void testClone() {
		Queen q = queen.clone();
		assertTrue(q.getID() == queen.getID());
	}

}

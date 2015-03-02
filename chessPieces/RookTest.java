package chessPieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class RookTest {

	public Rook rook;
	
	@Before
	public void setUp() throws Exception {
		rook = new Rook(Player.PLAYER2);
	}

	@Test
	public void testRookPlayer() {
		assertTrue(rook.getPlayer() == Player.PLAYER2);
	}

	@Test
	public void testRookPlayerBoolean() {
		rook = new Rook(Player.PLAYER2, true);
		assertTrue(rook.getPlayer() == Player.PLAYER2);
		assertTrue(rook.hasMoved());
	}

	@Test
	public void testClone() {
		Rook r = rook.clone();
		assertTrue(r.getID() == rook.getID());
	}

	@Test
	public void testHasMoved() {
		rook = new Rook(Player.PLAYER2, true);
		assertTrue(rook.hasMoved());
	}

}

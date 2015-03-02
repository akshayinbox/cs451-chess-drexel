package chessPieces;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class PawnTest {

	public Pawn pawn;
	
	@Before
	public void setUp() throws Exception {
		pawn = new Pawn(Player.PLAYER1);
	}


	@Test
	public void testPawnPlayer() {
		assertTrue(pawn.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testPawnPlayerBoolean() {
		pawn = new Pawn(Player.PLAYER1, true);
		assertTrue(pawn.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testClone() {
		Pawn p = pawn.clone();
		assertTrue(p.getID() == pawn.getID());
	}

}

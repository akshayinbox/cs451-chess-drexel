package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;

public class PlayerTest {

	public Player a, b;
	
	@Before
	public void setUp() throws Exception {
		a = Player.PLAYER1;
		b = Player.PLAYER2;
	}

	@Test
	public void testOtherPlayer() {
		assertTrue(a.otherPlayer().equals(b));
		assertTrue(b.otherPlayer().equals(a));
		
	}

}

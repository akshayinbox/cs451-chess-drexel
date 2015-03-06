package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Coord;
import chessBoard.Move;

public class MoveTest {

	private Move m;
	private Coord fromPos, toPos;
	private Coord fromPosTranslated, toPosTranslated;
	private Integer timeTaken;
	
	@Before
	public void setUp() throws Exception {
		fromPos = new Coord(1, 2);
		toPos = new Coord(3, 4);
		this.fromPosTranslated = new Coord(6, 5);
		this.toPosTranslated =  new Coord(4, 3);
		timeTaken = 27;
		testMove();
	}

	@Test
	public void testMove() {
		m = new Move(fromPos, toPos, timeTaken);
		assertTrue(m.getFrom().equals(fromPos));
		assertTrue(m.getTo().equals(toPos));
		assertTrue(m.getTimeTaken() == timeTaken);
	}

	@Test
	public void testGetFrom() {
		assertTrue(m.getFrom().equals(fromPos));
	}

	@Test
	public void testGetTo() {
		assertTrue(m.getTo().equals(toPos));
	}

	@Test
	public void testToString() {
		assertTrue(m.toString().equals("1,2 to 3,4"));
	}

	@Test
	public void testToStringTranslated() {
		assertTrue(m.toStringTranslated().equals("6,5 to 4,3"));
	}

	@Test
	public void testGetFromTranslated() {
		assertTrue(m.getFromTranslated().equals(fromPosTranslated));
	}

	@Test
	public void testGetToTranslated() {
		assertTrue(m.getFromTranslated().equals(fromPosTranslated));
	}

	@Test
	public void testGetTimeTaken() {
		assertTrue(m.getTimeTaken() == 27);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(m.equals(new Move(fromPos, toPos, timeTaken)));
	}

}

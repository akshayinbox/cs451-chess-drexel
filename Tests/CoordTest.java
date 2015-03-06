package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Coord;

public class CoordTest {

	public Coord point, point2, cloned;
	
	@Before
	public void setUp() throws Exception {
		point = new Coord(1,2);
	}

	@Test
	public void testCoord() {
		assertTrue(point.getRow() == 1);
		assertTrue(point.getCol() == 2);
	}

	@Test
	public void testClone() throws CloneNotSupportedException {
		cloned =  point.clone();
		assertTrue(cloned.getRow() == 1);
		assertTrue(cloned.getCol() == 2);
	}

	@Test
	public void testToString() {
		assertTrue(point.toString().equals("1,2"));
	}

	@Test
	public void testGetRow() {
		assertTrue(point.getRow() == 1);
	}

	@Test
	public void testGetCol() {
		assertTrue(point.getCol() == 2);
	}

	@Test
	public void testEqualsObject() {
		point2 = new Coord(1,2);
		assertTrue(point.equals(point2));
	}

}

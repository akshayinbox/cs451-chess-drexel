package chessBoard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CodeTest {

	public Code a = Code.SUCCESS, b = Code.NOT_LEGAL, c = Code.PROMOTION, d = Code.CASTLE_LEFT,  e = Code.CASTLE_RIGHT, f = Code.EN_PASSANT;
	
	
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {
		assertTrue(a == Code.SUCCESS);
		assertTrue(b == Code.NOT_LEGAL);
		assertTrue(c == Code.PROMOTION);
		assertTrue(d == Code.CASTLE_LEFT);
		assertTrue(e == Code.CASTLE_RIGHT);
		assertTrue(f == Code.EN_PASSANT);
	}

}

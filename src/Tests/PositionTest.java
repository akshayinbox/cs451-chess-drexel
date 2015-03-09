package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chessBoard.Player;
import chessBoard.Position;
import chessPieces.ChessPiece;
import chessPieces.King;
import chessPieces.PieceID;
import chessPieces.Rook;

public class PositionTest {
	
	private int row, col;
	ChessPiece piece;
	Position p1, p2, p3;


	@Before
	public void setUp() throws Exception {
		
		p1 = new Position();
		p2 = new Position(1,2);
		piece = new Rook(Player.PLAYER1);
		p3 = new Position(1, 2, piece);

	}

	@Test
	public void testPosition() {
		p1 = new Position();
		assertTrue(p1.getPiece() == null);
	}

	@Test
	public void testPositionIntInt() {
		p2 = new Position(1,2);
		assertTrue(p2.getRow() == 1);
		assertTrue(p2.getCol() == 2);
	}
	
	@Test
	public void testPositionIntIntPiece() {
		p3 = new Position(1, 2, piece);
		assertTrue(p3.getRow() == 1);
		assertTrue(p3.getCol() == 2);
		assertTrue(p3.getPiece() == piece);
	}


	@Test
	public void testClone() {
		p3 = p2.clone();
		assertTrue(p3.getRow() == 1);
		assertTrue(p3.getCol() == 2);
		
	}

	@Test
	public void testGetRow() {
		assertTrue(p2.getRow() == 1);
		assertTrue(p3.getRow() == 1);
	}

	@Test
	public void testGetCol() {
		assertTrue(p2.getCol() == 2);
		assertTrue(p3.getCol() == 2);
	}

	@Test
	public void testAddPiece() {
		ChessPiece king  = new King(Player.PLAYER1);
		p1.addPiece(king);
		assertTrue(p1.getPiece().equals(king));
	}

	@Test
	public void testGetPiece() {
		assertTrue(p3.getPiece().equals(piece));
	}

	@Test
	public void testIsOpposingType() {
		assertTrue(!p3.isOpposingType(PieceID.ROOK));
	}

	@Test
	public void testIsOwnType() {
		assertTrue(p3.isOwnType(PieceID.ROOK));
	}

	@Test
	public void testClearPiece() {
		p3.clearPiece();
		assertTrue(p3.getPiece() == null);
	}

	@Test
	public void testIsEmpty() {
		assertTrue(p1.isEmpty());
	}

	@Test
	public void testIsFriendly() {
		assertTrue(p3.isFriendly(Player.PLAYER1));
		assertTrue(!p3.isFriendly(Player.PLAYER2));
	}

	@Test
	public void testIsEmptyOrEnemy() {
		assertTrue(p1.isEmptyOrEnemy(Player.PLAYER1));
		assertTrue(!p3.isEmptyOrEnemy(Player.PLAYER1));
	}

	@Test
	public void testIsEnemy() {
		assertTrue(!p3.isEnemy(Player.PLAYER1));
		assertTrue(p3.isEnemy(Player.PLAYER2));
	}

	@Test
	public void testToString() {
		assertTrue(p3.toString().equals("R0"));
	}

}

package Tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessPieces.Bishop;

public class BishopTest {

	Bishop b, c;
	ChessBoard cb;
	
	@Before
	public void setUp() throws Exception {
		b = new Bishop(Player.PLAYER1);
		cb = new ChessBoard(true);
		cb.initializeBoard();
	}

	/**
	 * Surrounds Bishop with opposing pieces at varying lengths in row/col, tests to make
	 * sure it can capture them or move to blocks preceding these pieces.
	 */
	@Test 
	public void testCapturingMoves() {
		
		//put bishop in middle of board, surround by opposing pawns
		cb.forceMove(new Move(new Coord(7,2), new Coord(4, 3)));
		Coord fromPos = new Coord(4,3);
		Coord toPos;
		
		cb.addPawns(Player.PLAYER2, new Coord(5, 2), new Coord(5, 4));
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//should have 8 available moves
		assertTrue(moves.size() == 8);
		
		assertTrue(validateBishopMoves(moves, fromPos));
	}
	
	/**
	 * Surrounds Bishop with own pieces at varying lengths in row/column and makes sure
	 * Bishop cannot capture own pieces but can move to positions before own pieces.
	 */
	@Test 
	public void testBlockingMoves() {
		
		//put bishop in middle of board, surround by own pawns at varying lengths
		cb.forceMove(new Move(new Coord(7,2), new Coord(4, 3)));
		Coord fromPos = new Coord(4,3);
		Coord toPos;
		
		cb.addPawns(Player.PLAYER1, new Coord(2, 1), new Coord(2, 5));
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//should have 4 available moves
		assertTrue(moves.size() == 4);

		assertTrue(validateBishopMoves(moves, fromPos));
	}
	
	/**
	 * Clears the board, makes sure bishop can visit every position in diagonal 
	 * without going off the board.
	 */
	@Test 
	public void testBoundaryMoves() {
		
		//put bishop in middle of board, clear rest of board
		cb.forceMove(new Move(new Coord(7,2), new Coord(4, 3)));
		Coord fromPos = new Coord(4,3);
		Coord toPos;
		cb.clearBoardExcept(fromPos);
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//test first main diagonal (top left to bottom right)
		assertTrue(moves.size() == 13);
		assertTrue(validateBishopMoves(moves, fromPos));
		
		//test other main diagonal (top right to bottom left)
		cb.forceMove(new Move(new Coord(4,3), new Coord(4, 4)));
		fromPos = new Coord(4,4);
		cb.clearBoardExcept(fromPos);
		moves = cb.getMoves(fromPos);
		
		assertTrue(moves.size() == 13);
		assertTrue(validateBishopMoves(moves, fromPos));
		
		
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
	
	/** Moves should be diagonal, i.e. the to position and from position
	 * should have the same change in rows / cols.
	*/
	public boolean validateBishopMoves(ArrayList<Move> moves, Coord fromPos) {
		
		//make sure no duplicate moves, i.e. all unique
		for (int i = 0; i < moves.size(); i++) {
			for (int j = i+1; j < moves.size(); j++) {
				if (moves.get(i).equals(moves.get(j)))
						return false;
			}
		}
		
		Coord toPos;
		for (int i = 0; i < moves.size(); i++) {
			toPos = moves.get(i).getTo();
			
			if (Math.abs(toPos.getRow() - fromPos.getRow()) != 
					Math.abs(toPos.getCol() - fromPos.getCol()))
				return false;
			if (!ChessBoard.validPosition(toPos.getRow(), toPos.getCol()))
				return false;
		}
		return true;
	}

}

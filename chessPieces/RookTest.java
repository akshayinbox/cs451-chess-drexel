package chessPieces;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;

public class RookTest {

	public Rook rook;
	public ChessBoard cb;
	
	@Before
	public void setUp() throws Exception {
		rook = new Rook(Player.PLAYER2);
		cb = new ChessBoard(true);
		cb.initializeBoard();
	}

	
	/**
	 * Surrounds rook with opposing pieces at varying lengths in row/col, tests to make
	 * sure it can capture them or move to blocks preceding these pieces.
	 */
	@Test 
	public void testCapturingMoves() {
		
		//put rook in middle of board, surround by opposing pawns
		cb.forceMove(new Move(new Coord(7,0), new Coord(4, 4)));
		Coord fromPos = new Coord(4,4);
		Coord toPos;
		
		cb.addPawns(Player.PLAYER2, new Coord(4, 0), new Coord(4, 7), new Coord(6, 4));
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//should have 12 available moves
		assertTrue(moves.size() == 12);
		
		/* Moves should be horizontal/vertical, i.e. the to position
		should have the same row xor the same column. 
		Also validate to-positions are valid positions.
		*/
		for (int i = 0; i < moves.size(); i++) {
			toPos = moves.get(i).getTo();
			assertTrue(toPos.getRow() == 4 ^ toPos.getCol() == 4);
			assertTrue(ChessBoard.validPosition(toPos.getRow(), toPos.getCol()));
		}
		
	}
	
	/**
	 * Surrounds rook with own pieces at varying lengths in row/column and makes sure
	 * rook cannot capture own pieces but can move to positions before own pieces.
	 */
	@Test 
	public void testBlockingMoves() {
		
		//put rook in middle of board, surround by own pawns
		cb.forceMove(new Move(new Coord(7,0), new Coord(4, 4)));
		Coord fromPos = new Coord(4,4);
		Coord toPos;
		
		cb.addPawns(Player.PLAYER1, new Coord(4, 0), new Coord(4, 7), new Coord(1, 4));
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//should have 12 available moves
		assertTrue(moves.size() == 8);

		
		/* Moves should be horizontal/vertical, i.e. the to position
		should have the same row xor the same column. 
		Also validate to-positions are valid positions.
		*/
		for (int i = 0; i < moves.size(); i++) {
			toPos = moves.get(i).getTo();
			assertTrue(toPos.getRow() == 4 ^ toPos.getCol() == 4);
			assertTrue(ChessBoard.validPosition(toPos.getRow(), toPos.getCol()));
		}
		
	}
	
	/**
	 * Clears the board, makes sure rook can visit every position in its row/column
	 * and also not go off the board.
	 */
	@Test 
	public void testBoundaryMoves() {
		
		//put rook in middle of board, clear rest of board
		cb.forceMove(new Move(new Coord(7,0), new Coord(4, 4)));
		Coord fromPos = new Coord(4,4);
		Coord toPos;
		cb.clearBoardExcept(fromPos);
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//should have 14 available moves
		assertTrue(moves.size() == 14);

		
		/* Moves should be horizontal/vertical, i.e. the to position
		should have the same row xor the same column. 
		Also validate to-positions are valid positions.
		*/
		for (int i = 0; i < moves.size(); i++) {
			toPos = moves.get(i).getTo();
			assertTrue(toPos.getRow() == 4 ^ toPos.getCol() == 4);
			assertTrue(ChessBoard.validPosition(toPos.getRow(), toPos.getCol()));
		}
		
	}
	
	/**
	 * Makes sure rook is updated after movement, i.e.
	 * hasMoved should be false before movement, and true after
	 */
	@Test
	public void testRookUpdated() {
		
		//rook initially has not moved
		Rook rook = (Rook) cb.getPosition(new Coord(7,0)).getPiece();
		assertFalse(rook.hasMoved());
		
		//move pawn, then rook
		cb.validateAndApply(new Move(new Coord(6,0), new Coord(4,0)));
		cb.validateAndApply(new Move(new Coord(7,0), new Coord(6,0)));
		
		//rook should have move flag set to true
		rook = (Rook) cb.getPosition(new Coord(6,0)).getPiece();
		assertTrue(rook.hasMoved());
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

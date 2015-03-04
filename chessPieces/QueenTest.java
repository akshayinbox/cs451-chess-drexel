package chessPieces;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;

public class QueenTest {

	ChessBoard cb;
	Queen queen;
	
	@Before
	public void setUp() throws Exception {
		queen = new Queen(Player.PLAYER2);
		cb = new ChessBoard(true);
		cb.initializeBoard();
	}
	
	/**
	 * Clears the board, makes sure Queen can visit all vertical/horizontal
	 * and diagonal positions relating to starting position. Note, since Queen
	 * uses getMoves() from bishop and rook, we only need to make sure
	 * they are added together successfully.
	 */
	@Test 
	public void testBoundaryMoves() {
		
		//put queen, clear rest of board
		cb.forceMove(new Move(new Coord(7,3), new Coord(4, 3)));
		Coord fromPos = new Coord(4,3);
		Coord toPos;
		cb.clearBoardExcept(fromPos);
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//test first main diagonal (top left to bottom right)
		System.out.println(moves.size());
		assertTrue(moves.size() == 27);
		
		assertTrue(validateQueenMoves(moves, fromPos));
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
	
	/** Moves should be diagonal or vertical in relation to
	 * starting position. Also, no duplicate moves should exist.
	*/
	public boolean validateQueenMoves(ArrayList<Move> moves, Coord fromPos) {
		
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
			
			boolean isDiagonalMovement = Math.abs(toPos.getRow() - fromPos.getRow()) == 
					Math.abs(toPos.getCol() - fromPos.getCol());
			boolean isVertHorzMovement = toPos.getRow() == fromPos.getRow() ^ 
					toPos.getCol() == fromPos.getCol();
			
			//movement must be in diagonal or a horiztonal/vertical fashion
			if (!isDiagonalMovement && !isVertHorzMovement)
				return false;
			
			if (!ChessBoard.validPosition(toPos.getRow(), toPos.getCol()))
				return false;
		}
		return true;
	}

}

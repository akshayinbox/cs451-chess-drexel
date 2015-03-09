package Tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessPieces.Knight;

public class KnightTest {
	
	public Knight knight;
	ChessBoard cb;

	@Before
	public void setUp() throws Exception {
		knight = new Knight(Player.PLAYER1);
		cb = new ChessBoard(true);
		cb.initializeBoard();
	}
	
	/**
	 * Makes sure Knight can capture opposing pieces in all 8 positions.
	 */
	@Test 
	public void testKnightCapture() {
		//put knight in middle of board, with 8 pawns in each of his attack
		//positions
		cb.forceMove(new Move(new Coord(7,1), new Coord(4, 4)));
		Coord fromPos = new Coord(4,4);
		
		//put pawns in each of his 8 attacking positions
		cb.addPawns(Player.PLAYER2, new Coord(6,5), new Coord(6,3),
					new Coord(5, 6), new Coord(5, 2), new Coord(3,6),
					new Coord(3, 2), new Coord(2,5), new Coord(2, 3));
		
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//should have 8 available moves
		assertTrue(moves.size() == 8);
		
		assertTrue(validateKnightMoves(moves, fromPos));
	}
	
	/**
	 * Makes sure Knight cannot land on any of his own pieces
	 */
	@Test 
	public void testKnightBlocked() {
		//put knight in middle of board, with 8 pawns in each of his attack
		//positions
		cb.forceMove(new Move(new Coord(7,1), new Coord(4, 4)));
		Coord fromPos = new Coord(4,4);
		
		//put pawns in each of his 8 attacking positions
		cb.addPawns(Player.PLAYER1, new Coord(6,5), new Coord(6,3),
					new Coord(5, 6), new Coord(5, 2), new Coord(3,6),
					new Coord(3, 2), new Coord(2,5), new Coord(2, 3));
		
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//no available moves
		assertTrue(moves.size() == 0);
	}
	
	/**
	 * Makes sure Knight can land on empty positions.
	 */
	@Test 
	public void testKnightOpenBoard() {
		//put knight in middle of board, with 8 pawns in each of his attack
		//positions
		cb.forceMove(new Move(new Coord(7,1), new Coord(4, 4)));
		Coord fromPos = new Coord(4,4);
		cb.clearBoardExcept(fromPos);
				
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//all available moves since board is empty.
		assertTrue(moves.size() == 8);
	}
		
	@Test
	public void testKnight() {
		assertTrue(knight.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testClone() {
		Knight k = knight.clone();
		assertTrue(k.getID() == knight.getID());
	}
	
	public boolean validateKnightMoves(ArrayList<Move> moves, Coord fromPos) {
		
		//make sure no duplicate moves, i.e. all unique
		for (int i = 0; i < moves.size(); i++) {
			for (int j = i+1; j < moves.size(); j++) {
				if (moves.get(i).equals(moves.get(j)))
						return false;
			}
		}
		
		for (int i = 0; i < moves.size(); i++) {
			Coord toPos = moves.get(i).getTo();
			
			//make sure move is on the board
			if (!ChessBoard.validPosition(toPos.getRow(), toPos.getCol()))
				return false;
			
			int rowDifference = Math.abs(fromPos.getRow() - toPos.getRow());
			int colDifference = Math.abs(fromPos.getCol() - toPos.getCol());
			
			//knight can move 2 in one direction, and 1 in the other
			if ((rowDifference == 2 && colDifference != 1) ||
					(rowDifference == 1 && colDifference != 2))
					return false;
		}
		
		return true;
		
	}
	

}

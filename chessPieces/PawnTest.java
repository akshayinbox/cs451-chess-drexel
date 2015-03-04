package chessPieces;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;

public class PawnTest {

	public Pawn pawn;
	ChessBoard cb;
	
	@Before
	public void setUp() throws Exception {
		pawn = new Pawn(Player.PLAYER1);
		cb = new ChessBoard(true);
		cb.initializeBoard();
	}

	/**Tests all pawn's forward 2 movement:
	 * Moving 2 spaces forward only if:
	 * -first movement for said pawn
	 * -cannot jump 2 on any other move
	 */
	@Test
	public void testPawnTwoForward() {
		
		Coord fromPos = new Coord(6,2);
		ArrayList<Move> moves = cb.getMoves(fromPos);
		
		//2 possible movements available at beginning
		assertTrue(moves.size() == 2);
		assertTrue(moves.contains(new Move(fromPos, new Coord(5,2))));
		assertTrue(moves.contains(new Move(fromPos, new Coord(4,2))));
		
		//move pawn only 1 forward
		cb.validateAndApply(new Move(fromPos, new Coord(5,2)));
		
		//only one possible move forward after
		fromPos = new Coord(5,2);
		moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 1);
		assertTrue(moves.contains(new Move(fromPos, new Coord(4,2))));
		
	}
	
	/**
	 * Tests to make sure pawn can move 2 forward but only when no
	 * pieces (enemy/own) are in the way (so no jumping). Also makes sure pawn only only
	 * move forward if no obstructions
	 */
	@Test
	public void testPawnForwardObstruction() {
		Coord fromPos = new Coord(6,2);
		
		//put own knight 1 in front of pawn
		cb.forceMove(new Move(new Coord(7,1), new Coord(5, 2)));
		ArrayList<Move> moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 0);
		
		//put own knight 2 in front of pawn
		cb.forceMove(new Move(new Coord(5,2), new Coord(4, 2)));
		moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 1);
		assertTrue(moves.contains(new Move(fromPos, new Coord(5,2))));
		
		cb.initializeBoard();
		//put opponents knight 1 infront of pawn
		cb.forceMove(new Move(new Coord(0,1), new Coord(5, 2)));
		moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 0);
		
		//put opponents knight 2 in front of pawn
		cb.forceMove(new Move(new Coord(5,2), new Coord(4, 2)));
		moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 1);
		assertTrue(moves.contains(new Move(fromPos, new Coord(5,2))));
	}
	
	/**
	 * Test to make sure that pawn can attack diagonally, but only diagonal in front.
	 * For Player1's pawns.
	 */
	@Test
	public void testPawnAttackPlayer1() {
		
		//put pawn in middle of board, surround diagonals by 4 pawns, one pawn in front
		Coord fromPos = new Coord(4,4);
		cb.addPawns(Player.PLAYER1, fromPos);
		
		cb.addPawns(Player.PLAYER2, new Coord(3,3), new Coord(3,5),
				new Coord(5, 3), new Coord(5, 5), new Coord(3,4));
		
		ArrayList<Move> moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 2);
		
		//can attack two pieces in forward diagonal
		assertTrue(moves.contains(new Move(fromPos, new Coord(3,3))));
		assertTrue(moves.contains(new Move(fromPos, new Coord(3,5))));
	}
	
	/**
	 * Test to make sure that pawn can attack diagonally, but only diagonal in front.
	 * For Player2's pawns.
	 */
	@Test
	public void testPawnAttackPlayer2() {
		
		//put pawn in middle of board, surround diagonals by 4 pawns, one pawn in front
		Coord fromPos = new Coord(4,4);
		cb.addPawns(Player.PLAYER2, fromPos);
		
		cb.addPawns(Player.PLAYER1, new Coord(3,3), new Coord(3,5),
				new Coord(5, 3), new Coord(5, 5), new Coord(5,4));
		
		ArrayList<Move> moves = cb.getMoves(fromPos);
		assertTrue(moves.size() == 2);
		
		//can attack two pieces in forward diagonal
		assertTrue(moves.contains(new Move(fromPos, new Coord(5,3))));
		assertTrue(moves.contains(new Move(fromPos, new Coord(5,5))));
	}
	
	/**
	 * Testing for a normal condition en passant for player 1.
	 */
	@Test
	public void testEnPassantPlayer1() {
		
		//clear board except for opposing and own pawn.
		Coord opposingPawn = new Coord(1, 5);
		Coord playersPawn = new Coord(3, 6);
		cb.clearBoardExcept(opposingPawn);
		cb.addPawns(Player.PLAYER1, playersPawn);
		
		//move opposing pawn 2 forward by receving opponents move
		cb.receiveMove(new Move(new Coord(6,2), new Coord(4,2)));
		
		ArrayList<Move> moves = cb.getMoves(playersPawn);
		
		//validate En Passant is in moves
		assertTrue(moves.contains(new Move(playersPawn, new Coord(2, 5))));
		
		//apply en passant
		Code moveCode = cb.validateAndApply(new Move(playersPawn, new Coord(2,5)));
		assertTrue(moveCode == Code.EN_PASSANT);
		
	}
	
	/**
	 * Testing for a normal condition en passant for player 2.
	 */
	@Test
	public void testEnPassantPlayer2() {
		
		//clear board except for opposing and own pawn.
		Coord opposingPawn = new Coord(4, 3);
		Coord playersPawn = new Coord(4, 4);
		cb.clearBoardExcept(opposingPawn);
		cb.addPawns(Player.PLAYER2, opposingPawn);
		cb.addPawns(Player.PLAYER1, playersPawn);
		
		//receive opponents en passant move
		cb.receiveMove(new Move(new Coord(3,4), new Coord(2,3)));
		
		//validate users pawn is gone
		assertTrue(cb.getPosition(playersPawn).isEmpty());
	}
	
	/**
	 * Testing for en passant, but player waited one turn after opposing pawn
	 * moved into en passant, so now no longer valid. Note we do not need to test
	 * opposing pawn's illegal en passants as thats the responsibility of opponent
	 * to validate his own moves.
	 */
	@Test
	public void testIllegalEnPassantPlayer1() {
		
		//clear board except for opposing and own pawn.
		Coord opposingPawn = new Coord(1, 5);
		Coord dummyPawn = new Coord(0,0);
		Coord playersPawn = new Coord(3, 6);
		cb.clearBoardExcept(opposingPawn);
		cb.addPawns(Player.PLAYER1, playersPawn);
		cb.addPawns(Player.PLAYER2, dummyPawn);
		
		//move opposing pawn 2 forward by receving opponents move
		cb.receiveMove(new Move(new Coord(6,2), new Coord(4,2)));
		
		ArrayList<Move> moves = cb.getMoves(playersPawn);
		
		//validate En Passant is in moves
		assertTrue(moves.contains(new Move(playersPawn, new Coord(2, 5))));
		
		//receive another move from opponent
		cb.receiveMove(new Move(new Coord(7,7), new Coord(6,7)));
		
		//En passant no longer valid
		moves = cb.getMoves(playersPawn);
		assertFalse(moves.contains(new Move(playersPawn, new Coord(2, 5))));
	}
	
	/**
	 * Test that the pawn receives a move code of PAWN PROMOTION when reaching
	 * other side of board.
	 */
	@Test
	public void testPawnPromotion() {
		//clear board except for pawn 1 space from promotion
		Coord playersPawn = new Coord(1, 6);
		cb.addPawns(Player.PLAYER1, playersPawn);
		cb.clearBoardExcept(playersPawn);
		
		Code moveCode = cb.validateAndApply(new Move(playersPawn, new Coord(0,6)));
		assertTrue(moveCode == Code.PROMOTION);
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

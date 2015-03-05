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

public class KingTest {

	public King king;
	ChessBoard cb;
	
	@Before
	public void setUp() throws Exception {
		king = new King(Player.PLAYER1);
		cb = new ChessBoard(true);
		cb.initializeBoard();
	}

	/**
	 * Tests to make sure the king can move in all 8 adjacent spaces on an 
	 * empty board.
	 */
	@Test
	public void testBasicKingMovement() {
		
		//Move king to middle of the board, clear rest of pieces, should be able to move
		//in any direction.
		Coord kingPos = new Coord(4,4);
		cb.forceMove(new Move(new Coord(7, 4), kingPos));
		cb.clearBoardExcept(kingPos);
		ArrayList<Move> moves = cb.getMoves(kingPos);
		
		assertTrue(moves.size() == 8);
		assertTrue(validateKingMoves(moves, kingPos));	
	}
	
	/**
	 * Tests to make sure the king can capture any pieces in adjacent squares.
	 * Not checking for if king in check.
	 */
	@Test
	public void testBasicKingCapture() {
		
		//Move king to middle of the board, clear rest of pieces, should be able to move
		//in any direction.
		Coord kingPos = new Coord(4,4);
		cb.forceMove(new Move(new Coord(7, 4), kingPos));
		cb.clearBoardExcept(kingPos);
		
		cb.addPawns(Player.PLAYER2, new Coord(4,5), new Coord(4,3), new Coord(3,3),
					new Coord(3,4), new Coord(3,5), new Coord(5,3), new Coord(5,4),
					new Coord(5,5));
		
		ArrayList<Move> moves = cb.getMoves(kingPos);
		assertTrue(moves.size() == 8);
		assertTrue(validateKingMoves(moves, kingPos));	
	}
	
	/** 
	 * Test castling to the left with all other conditions fine.
	 */
	@Test
	public void testCastleLeft() {
		
		Coord kingPos = new Coord(7,4);
		Coord toPos = new Coord(7,2); //when king moves to left 2 spaces, considered castle
		
		//verify if pieces are between king and rook that castle is not available
		ArrayList<Move> moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toPos)));
		
		//verify king can castle after knight/bishop have moved
		cb.removePiece(new Coord(7,1));
		cb.removePiece(new Coord(7,2));
		cb.removePiece(new Coord(7,3));
		moves = cb.getMoves(kingPos);
		assertTrue(moves.contains(new Move(kingPos, toPos)));
		
		//apply castle left
		cb.validateAndApply(new Move(kingPos, toPos));
		assertTrue(cb.getPosition(toPos).getPiece().getID() == PieceID.KING);
		assertTrue(cb.getPosition(new Coord(7,3)).getPiece().getID() == PieceID.ROOK);
	}
	
	/** 
	 * Test castling to the right with all other conditions fine.
	 */
	@Test
	public void testCastleRight() {
		
		Coord kingPos = new Coord(7,4);
		Coord toPos = new Coord(7,6); //when king moves to right 2 spaces, considered castle
		
		//verify if pieces are between king and rook that castle is not available
		ArrayList<Move> moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toPos)));
		
		//verify king can castle after knight/bishop have moved
		cb.removePiece(new Coord(7,5));
		cb.removePiece(new Coord(7,6));
		moves = cb.getMoves(kingPos);
		assertTrue(moves.contains(new Move(kingPos, toPos)));
		
		//apply castle right
		cb.validateAndApply(new Move(kingPos, toPos));
		assertTrue(cb.getPosition(toPos).getPiece().getID() == PieceID.KING);
		assertTrue(cb.getPosition(new Coord(7,5)).getPiece().getID() == PieceID.ROOK);
	}
	
	/** 
	 * Test illegal castleing because of following conditions:
	 * -King has moved.
	 * -Rook has moved.
	 */
	@Test
	public void testInvalidCastleFromMovement() {
		
		Coord kingPos = new Coord(7,4);
		Coord toLeftPos = new Coord(7,2); //when king moves to left 2 spaces, considered castle
		Coord toRightPos = new Coord(7,6);
				
		//remove bishop/knight/queen
		cb.removePiece(new Coord(7,1));
		cb.removePiece(new Coord(7,2));
		cb.removePiece(new Coord(7,3));
		cb.removePiece(new Coord(7,5));
		cb.removePiece(new Coord(7,6));
		
		//verify king can intially castle to left/right
		ArrayList<Move> moves = cb.getMoves(kingPos);
		assertTrue(moves.contains(new Move(kingPos, toLeftPos)));
		assertTrue(moves.contains(new Move(kingPos, toRightPos)));
		
		//move left rook to and from position.
		cb.validateAndApply(new Move(new Coord(7,0), new Coord(7,1)));
		//cb.validateAndApply(new Move(new Coord(7,1), new Coord(7,0)));
		
		//verify king can no longer castle left
		moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toLeftPos)));
		
		//move right rook to and from position.
		cb.validateAndApply(new Move(new Coord(7,7), new Coord(7,6)));
		cb.validateAndApply(new Move(new Coord(7,6), new Coord(7,7)));
		
		//verify king can no longer castle right
		moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toRightPos)));
		
		/* reset board, move king from place and to place. Verify
		 * can no longer castle because king moved.
		 */
		cb.initializeBoard();
		cb.removePiece(new Coord(7,1));
		cb.removePiece(new Coord(7,2));
		cb.removePiece(new Coord(7,3));
		cb.removePiece(new Coord(7,5));
		cb.removePiece(new Coord(7,6));
		
		cb.validateAndApply(new Move(kingPos, new Coord(7,3)));
		cb.validateAndApply(new Move(new Coord(7,3), kingPos));
		
		moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toRightPos)));
		assertFalse(moves.contains(new Move(kingPos, toLeftPos)));
		
	}
	
	/**
	 * Tests that King cannot move through a position while castleing where he would
	 * be in check. Also, King cannot castle while he is in check. It's the ChessBoard
	 * responsibility from not validating a move that puts the king in check, so casteling
	 * into check is not checked here.
	 */
	@Test
	public void testInvalidCastleFromCheck() {
		Coord kingPos = new Coord(7,4);
		Coord toLeftPos = new Coord(7,2); //when king moves to left 2 spaces, considered castle
				
		//remove bishop/knight/queen and row of pawns
		cb.removePiece(new Coord(7,1));
		cb.removePiece(new Coord(7,2));
		cb.removePiece(new Coord(7,3));
		cb.clearRow(6);
		
		//put rook in column that the king has to move through to castle.
		cb.forceMove(new Move(new Coord(0,0), new Coord(4,3)));
		ArrayList<Move> moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toLeftPos)));
		
		//move rook to column that king is in, make sure King cannot castle
		//while in check.
		cb.forceMove(new Move(new Coord(4,3), new Coord(4,4)));
		moves = cb.getMoves(kingPos);
		assertFalse(moves.contains(new Move(kingPos, toLeftPos)));
		
	}
	
	
	@Test
	public void testKingPlayer() {
		assertTrue(king.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testKingPlayerBoolean() {
		king = new King(Player.PLAYER1, true);
		assertTrue(king.getPlayer() == Player.PLAYER1);
	}

	@Test
	public void testClone() {
		King k = king.clone();
		assertTrue(k.getID() == king.getID());
	}
	
	/**
	 *  Moves should only be in 1 adjacent square.
	*/
	public boolean validateKingMoves(ArrayList<Move> moves, Coord fromPos) {
		
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
			
			int rowDiff = Math.abs(toPos.getRow() - fromPos.getRow());
			int colDiff = Math.abs(toPos.getCol() - fromPos.getCol());
			
			if(!ChessBoard.validPosition(toPos.getRow(), toPos.getCol()))
				return false;
			
			if (rowDiff > 1 || colDiff > 1)
				return false;
		}
		return true;
	}

}

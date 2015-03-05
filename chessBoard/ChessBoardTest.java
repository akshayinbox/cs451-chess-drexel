package chessBoard;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chessPieces.Queen;

public class ChessBoardTest {
	
	private ChessBoard cbHost, cbOpp;

	@Before
	public void setUp() throws Exception {
		cbHost = new ChessBoard(true); //host's chessboard
		cbOpp = new ChessBoard(false); //opponnet's chessboard
		cbHost.initializeBoard();
		cbOpp.initializeBoard();
	}


	/**
	 * Makes sure you cannot move an opponent's piece, that an illegal move on your
	 * own piece returns NOT_LEGAL.
	 */
	@Test
	public void testValidateIllegalMove() {
		
		Move moveOpposingKnight = new Move(new Coord(0,1), new Coord(2,0));
		
		//try moving opponent's piece
		Code moveCode = cbHost.validateAndApply(moveOpposingKnight);
		assertTrue(moveCode == Code.NOT_LEGAL);
		moveCode = cbOpp.validateAndApply(moveOpposingKnight);
		assertTrue(moveCode == Code.NOT_LEGAL);
		
		//try moving queen on top of own pawn
		moveCode = cbHost.validateAndApply(new Move(new Coord(7,3), new Coord(6,3)));
		assertTrue(moveCode == Code.NOT_LEGAL);
		moveCode = cbOpp.validateAndApply(new Move(new Coord(7,3), new Coord(6,3)));
		assertTrue(moveCode == Code.NOT_LEGAL);
	}
	
	/**
	 * Test to make sure that putting your own King in check will return an 
	 * illegal move, and that moving pieces that would put your king in check
	 * will also be illegal. When king is in check, only allow moves that put him
	 * out of check.
	 * 
	 */
	@Test
	public void testValidatePutKingInCheck() {
		
		//clear pawns
		cbHost.clearRow(1);
		cbHost.clearRow(6);
		
		//have opponents queen in position to attack king diagonally, but own queen is blocking
		//have opponents rook in row above king ready to attack
		cbHost.forceMove(new Move(new Coord(0,3), new Coord(5,2)));
		cbHost.forceMove(new Move(new Coord(0,7), new Coord(6,7)));
		cbHost.forceMove(new Move(new Coord(7,3), new Coord(6,3)));
		
		//make sure moving queen and putting king in check is invalid
		Code moveCode = cbHost.validateAndApply(new Move(new Coord(6,3), new Coord(5,3)));
		assertTrue(moveCode == Code.IN_CHECK);
		
		//make sure moving King into check (from opponents rook) is invalid
		moveCode = cbHost.validateAndApply(new Move(new Coord(7,4), new Coord(6,4)));
		assertTrue(moveCode == Code.IN_CHECK);
		
		//have opposing queen take own queen. Only valid moves should be for own knight/bishop to 
		//kill queen but not the king (since king would be in check).
		cbHost.forceMove(new Move(new Coord(5,2), new Coord(6,3)));
		moveCode = cbHost.validateAndApply(new Move(new Coord(7,4), new Coord(6,3)));
		assertTrue(moveCode == Code.IN_CHECK);
		moveCode = cbHost.validateAndApply(new Move(new Coord(7,1), new Coord(6,3)));
		assertTrue(moveCode == Code.SUCCESS);	
	}
	
	/**
	 * Test that putting the king in check by a castle move will not be accepted.
	 * Also test that when the king is in check, that he cannot castle to get out of
	 * check.
	 * 
	 * Note: Much more tests on casteling in the KingTest tests. The chessboard is only
	 * responsible for telling the king when he can't castle if it would leave him in check.
	 */
	@Test
	public void testValidateInCheck() {
		
		//clear pawns, remove right bishop/knight
		cbHost.clearRow(6);
		cbHost.getPosition(new Coord(7,5)).clearPiece();
		cbHost.getPosition(new Coord(7,6)).clearPiece();
		
		//put opposing rook to position where it could attack king after castle
		//verify castleing would not be allowed
		cbHost.forceMove(new Move(new Coord(0,7), new Coord(2,6)));
		
		Move castleRight = new Move(new Coord(7,4), new Coord(7,6));
		Code moveCode = cbHost.validateAndApply(castleRight);
		assertTrue(moveCode == Code.IN_CHECK);
		
		//put king in check by rook, do not allow him to castle out of check
		cbHost.forceMove(new Move(new Coord(2,6), new Coord(2,4)));
		moveCode = cbHost.validateAndApply(castleRight);
		assertTrue(moveCode == Code.NOT_LEGAL);
	}
	
	/**
	 * Test to make sure game is over by checkmate and also not when a piece can
	 * block king/capture piece putting king in check.
	 * Test are for host.
	 */
	@Test
	public void testCheckMateHost() {
		
		//remove pawn in front of king
		assertTrue(GameStatus.CONTINUE == cbHost.gameOver());
		cbHost.getPosition(new Coord(6,4)).clearPiece();
		
		//move opposing queen to column of missing Pawn, not game over since
		//bishop can block king, or the knight can block too.
		cbHost.forceMove(new Move(new Coord(0, 3), new Coord(2, 4)));
		assertTrue(GameStatus.CONTINUE == cbHost.gameOver());
		
		//remove blocking bishop/knight/queen by replacing with pawns, now nothing
		//can block king and should be checkmate
		cbHost.addPawns(Player.PLAYER1, new Coord(7,5), new Coord(7,6),
							new Coord(7,3));
		assertTrue(GameStatus.CHECKMATE == cbHost.gameOver());
	}
	
	/**
	 * Test to make sure game is over by checkmate and also not when a piece can
	 * block king/capture piece putting king in check.
	 * Test is for person joining game (king/queen in different positions than host).
	 */
	@Test
	public void testCheckMateOpponent() {
		
		//remove pawn in front of king
		assertTrue(GameStatus.CONTINUE == cbOpp.gameOver());
		cbOpp.getPosition(new Coord(6,3)).clearPiece();
		
		//move opposing queen to column of missing Pawn, not game over since
		//bishop can block king, or the knight can block too.
		cbOpp.forceMove(new Move(new Coord(0, 4), new Coord(2, 3)));
		assertTrue(GameStatus.CONTINUE == cbOpp.gameOver());
		
		//remove blocking bishop/knight/queen by replacing with pawns, now nothing
		//can block king and should be checkmate
		cbOpp.addPawns(Player.PLAYER1, new Coord(7,1), new Coord(7,2),
							new Coord(7,4));
		assertTrue(GameStatus.CHECKMATE == cbOpp.gameOver());
	}
	
	/**
	 * Test condition of stalemate returns stalemate.
	 */
	@Test
	public void testStaleMate() {
		
		Coord kingPos = new Coord(7,4);
		
		//Put two queens two rows above and on left/right column of king. King has no
		//moves he can make and should return stalemate.
		cbHost.clearBoardExcept(kingPos);
		cbHost.getPosition(new Coord(5,3)).addPiece(new Queen(Player.PLAYER2));
		cbHost.getPosition(new Coord(5,5)).addPiece(new Queen(Player.PLAYER2));
		
		assertFalse(cbHost.kingInCheck());
		
		//king not in check, but has no moves, so Stalemate
		GameStatus gs = cbHost.gameOver();
		assertTrue(gs == GameStatus.STALEMATE);
	}
	
	/**
	 * Tests that the board is initialized correctly, must be visually inspected by
	 * debugging.
	 */
	@Test
	public void testInitialize() {
		cbHost.toString();
		cbOpp.toString();
	}
	

}

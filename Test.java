import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Coord;
import chessBoard.Move;


public class Test {

	public static void main(String[] args) {
		ChessBoard cb = new ChessBoard();
		cb.initializeBoard();
		
		//Move movePawn = new Move(new Coord(6,1), new Coord(5,1));
		//cb.makeMove(movePawn);
		
		for (int i = 7; i > 2; i--) {
			Move movePawn = new Move(new Coord(i,4), new Coord(i-1,4));
			cb.validateAndApply(movePawn);
		}
		
		System.out.println(cb.toString());
	}
}

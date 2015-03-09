import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import UI.UI;
import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;


public class Test {

	public static void main(String[] args) throws IOException {
//		ChessBoard cb = new ChessBoard();
//		UI ui = new UI(cb);
		UI ui = new UI();
//		cb.initializeBoard();
		Player nextPlayer = Player.PLAYER1;
//		ChessBoard cb = null;
//		while(true) {
//			if (ui.getInitialized()) {
//				if (cb == null) {
//					cb = ui.getChessBoard();
//				}
//				System.out.println(cb.toString());
//				Move nextMove = getMove(nextPlayer);
//				
//				//chessboard only needs to validate own moves by player1.
//				//assume opponent's input is correct
//				if (nextPlayer == Player.PLAYER1) {
//					
//					Code moveCode = cb.validateAndApply(nextMove);
//					while (moveCode == Code.NOT_LEGAL) {
//						System.out.println("Invalid move, try again.");
//						nextMove = getMove(nextPlayer);
//						moveCode = cb.validateAndApply(nextMove);
//					}
//					
//				}	
//				else
//					cb.receiveMove(nextMove);
//				
//				if (nextPlayer == Player.PLAYER1)
//					nextPlayer = Player.PLAYER2;
//				else
//					nextPlayer = Player.PLAYER1;
//			}
//		}
		
		//for testing what moves are available to a piece
//		Position[][] board = cb.getBoard();
//		ArrayList<Move> moves = board[7][4].getPiece().getMoves(cb, new Coord(7,4));
//		System.out.println(cb.toString());
//		for(int i=0; i < moves.size(); i++)
//			System.out.println(moves.get(i).toString());
		
	}
	
	//method just for testing - reads in move from user by console
	public static Move getMove(Player player) {
		System.out.println("Player " + player.ordinal() + " enter a move");
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter starting row, hit enter, then col");
		int fromRow = scanner.nextInt();
		int fromCol= scanner.nextInt();
		
		System.out.println("Enter ending row, hit enter, then col");
		int toRow = scanner.nextInt();
		int toCol= scanner.nextInt();
		
		Coord fromCoord = new Coord(fromRow, fromCol);
		Coord toCoord = new Coord(toRow, toCol);
		
		return new Move(fromCoord, toCoord, null);
	}
}

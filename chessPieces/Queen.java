package chessPieces;

import java.util.ArrayList;

import chessBoard.ChessBoard;
import chessBoard.Code;
import chessBoard.Coord;
import chessBoard.Move;
import chessBoard.Player;
import chessBoard.Position;

public class Queen extends ChessPiece {
	
	public Queen(Player player) {
		super(player);
		id = PieceID.QUEEN;
	}
	
	public Queen clone() {
		return new Queen(player);
	}
		
	public ArrayList<Move> getMoves(ChessBoard cb, Coord cord) {
		
		//treat queen as a bishop/rook to get all her moves
		ArrayList<Move> rookMoves = new ArrayList<Move>();
		ArrayList<Move> bishopMoves = new ArrayList<Move>();

		Bishop bishop = new Bishop(player);
		Rook rook = new Rook(player);
		
		rookMoves = bishop.getMoves(cb, cord);
		bishopMoves = rook.getMoves(cb, cord);
		
		for (int i = 0; i < rookMoves.size(); i++)
			bishopMoves.add(rookMoves.get(i));
		
		return bishopMoves;

	}
	
	public Code moveCode(Coord from, Coord to) {
		return Code.SUCCESS;
	}
	
}
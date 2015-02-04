package chessNetwork;

import chessBoard.Move;

public class MoveMessage extends Message {
	public MoveMessage(Move move) {
		super(MessageType.MOVE, move);
	}
}

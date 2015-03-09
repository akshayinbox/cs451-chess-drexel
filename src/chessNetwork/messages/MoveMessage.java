package chessNetwork.messages;

import chessBoard.Move;

/**
  * Message class for sending chess Moves over the network
  */
public class MoveMessage extends Message {

	/**
	  * Creates a Message of type MOVE with Move content move
	  * @param move the Move content of the message
	  */
	public MoveMessage(Move move) {
		super(MessageType.MOVE, move);
	}
}

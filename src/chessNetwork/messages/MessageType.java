package chessNetwork.messages;

/**
  * Indicates a type for a Message object: MOVE, CHAT, END, or PROMOTION
  */
public enum MessageType {
	MOVE(0), CHAT(1), END(2), PROMOTION(3);

	private int type;

	private MessageType(int type) {
		this.type = type;
	}
}

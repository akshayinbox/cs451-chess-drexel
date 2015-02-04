package chessNetwork;

public enum MessageType {
	MOVE(0), CHAT(1);

	private int type;

	private MessageType(int type) {
		this.type = type;
	}
}

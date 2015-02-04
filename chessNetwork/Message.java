package chessNetwork;

import java.io.Serializable;

public abstract class Message implements Serializable {
	private MessageType type;
	private Object content;

	protected Message(MessageType type, Object content) {
		this.type = type;
		this.content = content;
	}

	public MessageType getType() {
		return type;
	}

	public Object getContent() {
		return content;
	}
}

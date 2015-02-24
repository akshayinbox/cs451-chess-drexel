package chessNetwork.messages;

import java.io.Serializable;

public class ChatMessage extends Message implements Serializable {
	private static final long serialVersionUID = 3335170904414151103L;

	public ChatMessage(String text) {
		super(MessageType.CHAT, text);
	}
}

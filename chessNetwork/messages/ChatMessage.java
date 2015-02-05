package chessNetwork.messages;

public class ChatMessage extends Message {
	public ChatMessage(String text) {
		super(MessageType.CHAT, text);
	}
}

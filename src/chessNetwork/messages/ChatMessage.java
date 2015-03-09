package chessNetwork.messages;

import java.io.Serializable;

/**
  * Message class for sending chat messages over the network
  */
public class ChatMessage extends Message implements Serializable {
	private static final long serialVersionUID = 3335170904414151103L;

	/**
	  * Creates a Message of type CHAT with content text
	  * @param text the text content of the chat message
	  */
	public ChatMessage(String text) {
		super(MessageType.CHAT, text);
	}
}

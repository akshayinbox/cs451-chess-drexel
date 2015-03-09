package chessNetwork.messages;

import java.io.Serializable;

/**
  * An abstract class to hold various types of data to be sent over sockets
  */
public abstract class Message implements Serializable {
	private static final long serialVersionUID = -6402845113204556710L;
	private MessageType type;
	private Object content;

	/**
	  * Creates a message of the indicated type with the indicated content
	  * @param type the type of Message to be created
	  * @param content the content of the Message to be created
	  */
	protected Message(MessageType type, Object content) {
		this.type = type;
		this.content = content;
	}

	/**
	  * Gets the type of the Message
	  * @return the type of the Message
	  */
	public MessageType getType() {
		return type;
	}

	/**
	  * Gets the content of the Message
	  * @return the content of the Message
	  */
	public Object getContent() {
		return content;
	}
}

package chessNetwork.messages;

import java.io.Serializable;

/**
  * Message class for sending chess game over indications over the network
  */
public class EndMessage extends Message implements Serializable {
	private static final long serialVersionUID = 5689406726310471734L;
	
	/**
	  * Creates a Message of type END with game over message text
	  * @param text the game over message
	  */
	public EndMessage(String text) {
		super(MessageType.END, text);
	}
}

package chessNetwork.messages;

import java.io.Serializable;

public class EndMessage extends Message implements Serializable {
	private static final long serialVersionUID = 5689406726310471734L;

	public EndMessage(String text) {
		super(MessageType.END, text);
	}
}

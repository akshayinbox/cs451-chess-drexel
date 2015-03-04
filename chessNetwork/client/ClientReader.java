package chessNetwork.client;

import chessNetwork.messages.Message;
import chessNetwork.messages.MessageProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class ClientReader implements Runnable, Serializable {
	private static final long serialVersionUID = 2165989946953642916L;
	private transient ObjectInputStream in;
	private MessageProcessor processor;

	//imagine passing in a UI object of some kind here and saving it as a member variable
	public ClientReader(ObjectInputStream in, MessageProcessor processor) {
		this.in = in;
		this.processor = processor;
	}

	@Override
	public void run() {
		try {
			Message received = (Message) in.readObject();
			while (received != null) {
				//Here is how I imagine this working with the chess board:
				//	The UI (or whatever) implements the MessageProcessor interface, and then
				//	whenever a message is received, the thread passes the received message to the
				//	UI. Then, the UI queries what type of Message it is: mvoe or chat
				//	message. If it's a chat message, it prints it to the chat window, and if it's a
				//	move, applies the move. Right now, this is all happening in this thread, the one that
				//	receives messages. In the future, if we find that that causes too much blocking,
				//	we might have to, say, process the message in a short-lived thread, but that is
				//	down the line.
				processor.process(received);
				received = (Message) in.readObject();
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("This exception should never happen.");
		}
		catch (IOException e) {
			System.out.println("Caught an Exception reading. Exiting.");
		}

		System.out.println("Reader exiting.");
	}
}

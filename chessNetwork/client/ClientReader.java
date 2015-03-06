package chessNetwork.client;

import chessNetwork.messages.Message;
import chessNetwork.messages.MessageProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
  * A reader for a Client type object, constantly reading from the server and passing receieved
  * Messages off to some other object which implements the MessageProcessor interface when run as a
  * Runnable object
  */
public class ClientReader implements Runnable, Serializable {
	private static final long serialVersionUID = 2165989946953642916L;
	private transient ObjectInputStream in;
	private MessageProcessor processor;

	/**
	  * Creates a new ClientReader object which can read from the ObjectInputStream in and which
	  * passes received Messages off to processor to process
	  * @param in the ObjectInputStream from which this object will read
	  * @param processor the MessageProcessor object to which the reader will pass its Messages
	  */
	public ClientReader(ObjectInputStream in, MessageProcessor processor) {
		this.in = in;
		this.processor = processor;
	}

	/**
	  * The run method for the Runnable interface, in which the reader object will enter a loop,
	  * constantly reading from the server until it is closed or catches some kind exception,
	  * passing received messages to the MessageProcessor
	  */
	@Override
	public void run() {
		try {
			Message received = (Message) in.readObject();
			while (received != null) {
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

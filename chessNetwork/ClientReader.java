package chessNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientReader implements Runnable {
	private ObjectInputStream in;

	//imagine passing in a UI object of some kind here and saving it as a member variable
	public ClientReader(ObjectInputStream in) {
		this.in = in;	
	}

	@Override
	public void run() {
		try {
			Message received = (Message) in.readObject();
			while (received != null) {
				//then, while the thread is running, every time a message is recieved, instead of
				//simply printing the content to the screen, imagine handing it off to the UI, i.e.,
				//ui.process(message), or something to that effect. This allows the thread to simply
				//gather messages as they are sent and let the UI process them. Though that happens
				//all in this thread...hm...this might not work as well as I thought. Perhaps the
				//processing of a message could all be done in a short thread too? That would
				//prevent the reading thread from blocking for too long.
				System.out.println(received.getContent());
				received = (Message) in.readObject();
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("This exception should never happen.");
		}
		catch (IOException e) {
			System.out.println("Caught an Exception reading. Exiting.");
		}
	}
}

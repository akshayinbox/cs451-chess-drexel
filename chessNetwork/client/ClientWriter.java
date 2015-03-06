package chessNetwork.client;

import chessNetwork.messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.Queue;

public class ClientWriter implements Runnable, Serializable {
	private final Queue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private final Semaphore availableMessages = new Semaphore(0);
	private ObjectOutputStream out;

	/**
	  * Creates a new ClientWriter object which can write to the specified ObjectOutputStream
	  * @param out the ObjectOutputStream to which this object will write
	  */
	public ClientWriter(ObjectOutputStream out) {
		this.out = out;
	}

	/**
	  * Readies the message to be sent on the interally held queue of messages
	  * @param m the Message to be sent to the server
	  */
	public void send(Message m) {
		messageQueue.add(m);
		availableMessages.release();
	}

	/**
	  * Tells the writer that writing is done, allowing the run method to exit  after it finishes
	  * writing all messages in its queue
	  */
	public void exit() {
		availableMessages.release();
	}
	
	/**
	  * The run method for the Runnable interface, in which the writer object will enter a loop,
	  * writing all Messages passed to its send method to its ObjectOutputStream until it can no
	  * longer do so, if it is told to exit or receives an exception 
	  */
	@Override
	public void run() {
		try {
			availableMessages.acquire();
			while (!messageQueue.isEmpty()) {
				out.writeObject(messageQueue.poll());
				out.flush();
				availableMessages.acquire();
			}
		}
		catch (InterruptedException e) {
			System.out.println("Interrupted while waiting for messages. Exiting.");
		}
		catch (IOException e) {
			System.out.println("Caught an IOException writing. Exiting.");
		}

		System.out.println("Writer exiting.");
	}


}

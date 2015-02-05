package chessNetwork.client;

import chessNetwork.messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.Queue;

public class ClientWriter implements Runnable {
	private final Queue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private final Semaphore availableMessages = new Semaphore(0);
	private ObjectOutputStream out;

	public ClientWriter(ObjectOutputStream out) {
		this.out = out;
	}

	public void send(Message m) {
		messageQueue.add(m);
		availableMessages.release();
	}

	public void exit() {
		availableMessages.release();
	}

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
	}


}

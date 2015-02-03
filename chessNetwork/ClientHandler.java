package chessNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.Socket;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.Queue;

public class ClientHandler implements Runnable {
	private static final int MAX_GAMES = 5;//Integer.MAX_VALUE;
	private static final Queue<Integer> ids = new ConcurrentLinkedQueue<Integer>();
	private static final ConcurrentMap<Integer, ClientHandler> waitingClients = new ConcurrentHashMap<Integer, ClientHandler>();

	private Socket clientSocket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	private ObjectOutputStream peerOut;
	private ClientHandler peer;
	private Semaphore peerSemaphore = new Semaphore(0);

	public static void establishIDs() {
		for (int i = 0; i <= MAX_GAMES; i++) {
			ids.add(i);
		}
	}
	
	
	public ClientHandler(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.socketIn = new ObjectInputStream(clientSocket.getInputStream());
		this.socketOut = new ObjectOutputStream(clientSocket.getOutputStream());
	}

	@Override
	public void run() {
		int gameID;
		try {
			gameID = socketIn.readInt();
		
			if (gameID < 0) {
				if (!establishNewGame()) {
					System.out.println("No more room. Quitting.");
					socketOut.writeInt(-1);
					socketOut.flush();
					return;
				}
			}
			else {
				joinExistingGame(gameID);
			}

			readWriteLoop();
			
			clientSocket.close();
		}
		catch (Exception e) {
			return;	
		}
	}

	private boolean establishNewGame() throws IOException, InterruptedException {
		Integer gameID = ids.poll();
		if (gameID == null) {
			return false;
		}
		socketOut.writeInt(gameID);
		socketOut.flush();

		waitingClients.put(gameID, this);
		waitForPeer();
		removeFromWaiting(gameID);
		return true;
	}

	private void removeFromWaiting(int gameID) {
		waitingClients.remove(gameID);
		ids.add(gameID);
	}

	private void joinExistingGame(int gameID) throws NullPointerException, IOException {
		peer = waitingClients.get(gameID);
		peerOut	= new ObjectOutputStream(peer.clientSocket.getOutputStream());
		peer.peerArrived(this);
	}

	private void waitForPeer() throws InterruptedException {
		peerSemaphore.acquire();
	}

	private void peerArrived(ClientHandler other) throws IOException {
		peer = other;
		peerOut = new ObjectOutputStream(peer.clientSocket.getOutputStream());
		peerSemaphore.release();
	}

	private void readWriteLoop() throws ClassNotFoundException, IOException {
		Object obj = socketIn.readObject();
		while (obj != null) {
			peerOut.writeObject(obj);
			peerOut.flush();
			obj = socketIn.readObject();
		}
	}

	private void readError() {
		System.out.println("Error reading from client. Client thread exiting.");
	}

	private void writeError() {
		System.out.println("Error writing to client. Client thread exiting.");
	}

	private void interruptedError() {
		System.out.println("Error in thread (interrupted). Client thread exiting.");
	}

	private void communicationError() {
		System.out.println("Error communication between clients. Client thread exiting.");
	}

	private void socketError() {
		System.out.println("Error closing socket. Client thread exiting.");
	}

	private void wrongIDError() {
		System.out.println("Error in gameID (doesn't exist). Client thread exiting.");
	}
}

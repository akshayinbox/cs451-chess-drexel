package chessNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Semaphore;

public class ClientHandler implements Runnable {
	private static AtomicInteger nextID = new AtomicInteger();
	private static Map<Integer, ClientHandler> waitingClients = new HashMap<Integer, ClientHandler>();

	private Socket clientSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Semaphore peer = new Semaphore(0);
	
	
	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;	
	}

	@Override
	public void run() {
		int gameID;
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			gameID = in.readInt();
		}
		catch (IOException e) {
			readError();
			return;
		}
		
		if (gameID < 0) {
			try {
				establishNewGame();
			}
			catch (InterruptedException e) {
				interruptedError();
				return;
			}
		}
		else {
			try {
				joinExistingGame(gameID);
			}
			catch (NullPointerException e) {
				wrongIDError();
				return;
			}
			catch (IOException e) {
				communicationError();
				return;
			}
		}

		try {
			readWriteLoop();
		}
		catch (IOException e) {
			communicationError();
			return;
		}
		catch (ClassNotFoundException e) {
		}

		try {
			clientSocket.close();
		}
		catch (IOException e) {
			socketError();
			return;	
		}
	}

	private void establishNewGame() throws InterruptedException {
		int gameID = nextID.getAndIncrement();
		waitingClients.put(gameID, this);
		System.out.println("Created gameID = " + gameID + ". Waiting for peer.");
		waitForPeer();
		System.out.println("ID = " + gameID + " finished waiting for peer.");
	}

	private void joinExistingGame(int gameID) throws NullPointerException, IOException {
		out	= new ObjectOutputStream(waitingClients.get(gameID).peerArrived(clientSocket.getOutputStream()));
		System.out.println("Game with ID = " + gameID + " joined.");
	}

	private void waitForPeer() throws InterruptedException {
		peer.acquire();
	}

	private OutputStream peerArrived(OutputStream out) throws IOException {
		this.out = new ObjectOutputStream(out);
		peer.release();
		return clientSocket.getOutputStream();
	}

	private void readWriteLoop() throws ClassNotFoundException, IOException {
		Object obj = in.readObject();
		while (obj != null) {
			out.writeObject(obj);
			out.flush();
			obj = in.readObject();
		}
	}

	private void readError() {
		System.out.println("Error reading from client. Client thread exiting.");
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

package chessNetwork.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.Queue;

public class ClientHandler implements Runnable, Serializable {
	private static final long serialVersionUID = -6844795542481644889L;
	private static final int MAX_WAITING = 100;//Integer.MAX_VALUE;
	private static final Queue<Integer> ids = new ConcurrentLinkedQueue<Integer>();
	private static final ConcurrentMap<Integer, ClientHandler> waitingClients = new ConcurrentHashMap<Integer, ClientHandler>();

	private Socket clientSocket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	private ObjectOutputStream peerOut;
	private ClientHandler peer;
	private Semaphore peerSemaphore = new Semaphore(0);
	int initialTime;

	public static void establishIDs() {
		for (int i = 0; i <= MAX_WAITING; i++) {
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
		System.out.println("Received a connection!");
		int gameID;
		try {
			gameID = socketIn.readInt();
		}
		catch (IOException e) {
			System.out.println("Error: couldn't read from socket.");
			cleanExit();
			return;
		}
		System.out.println("The received game ID is : " + gameID);

		try {
			if (gameID < 0) {
				//treat the "gameID" as the time limit and try to establish the new game
				initialTime = -gameID;
				if (!establishNewGame()) {
					socketOut.writeInt(-1);
					socketOut.flush();
					cleanExit();
					System.out.println("Not enough room. Client handler exiting.");
					return;
				}
			}
			else {
				if (joinExistingGame(gameID)) {
					//indicate that the connection was successful to the client
					socketOut.writeInt(initialTime);
					socketOut.flush();
					//allow the peer thread to continue
					peer.peerSemaphore.release();
				}
				else {
					System.out.println("Game doesn't exist.");
					socketOut.writeInt(-1);
					socketOut.flush();
					cleanExit();
					System.out.println("Exiting.");
					return;
				}
			}
		}
		catch (InterruptedException e) {
			System.out.println("Error: thread interrupted.");
			cleanExit();
			return;
		}
		catch (IOException e) {
			System.out.println("Error: couldn't write the gameID.");
			cleanExit();
			return;
		}

		try {
			readWriteLoop();
		}
		catch (ClassNotFoundException e) {
			System.out.println("Error: couldn't find class.");
			cleanExit();
			return;
		}
		catch (IOException e) {
			System.out.println("Error: couldn't read from or write to socket.");
			cleanExit();
			return;
		}

		
		cleanExit();
	}

	private boolean establishNewGame() throws IOException, InterruptedException {
		Integer gameID = ids.poll();
		if (gameID == null) {
			return false;
		}

		waitingClients.put(gameID, this);
		socketOut.writeInt(gameID);
		socketOut.flush();

		peerSemaphore.acquire();
		socketOut.writeInt(0);
		socketOut.flush();

		return true;
	}	

	private boolean joinExistingGame(int gameID) throws IOException {
		//get the peer based on the gameID; remove the peer from the list of waiting clients and put
		//the game id back on the available list of ids
		peer = waitingClients.get(gameID);
		if (peer == null) {
			return false;
		}

		waitingClients.remove(gameID);
		ids.add(gameID);

		//set the initial time to the peer's initial time
		initialTime = peer.initialTime;
		
		//set the peer's output socket to...the peer's output socket
		peerOut = peer.socketOut;

		//set the peer's peer to this, and the peer's peer's output socket to the current output
		//socket
		peer.peer = this;
		peer.peerOut = socketOut;
	
		return true;
	}
		
	private void readWriteLoop() throws ClassNotFoundException, IOException {
		Object obj = socketIn.readObject();
		while (obj != null) {
			System.out.println("Writing receieved object.");
			peerOut.writeObject(obj);
			peerOut.flush();
			obj = socketIn.readObject();
		}
	}

	private void cleanExit() {
		try {
			clientSocket.close();
		}
		catch (IOException e) {
			System.out.println("Error: couldn't close clientSocket.");
			return;
		}
	}
}

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
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			int gameID = in.readInt();
			if (gameID < 0) {
				gameID = nextID.getAndIncrement();
				waitingClients.put(gameID, this);
				System.out.println("Created gameID = " + gameID + ". Waiting for peer.");
				waitForPeer();
				System.out.println("ID = " + gameID + " finished waiting for peer.");
			}
			else {
				ClientHandler other = waitingClients.get(gameID);
				if (other != null) {
					out	= new ObjectOutputStream(other.peerArrived(clientSocket.getOutputStream()));
				}
				else {
					return;
				}

				System.out.println("Game with ID = " + gameID + " joined.");
			}

			Object obj = in.readObject();
			while (obj != null) {
				out.writeObject(obj);
				out.flush();
				obj = in.readObject();
			}


			clientSocket.close();

		}
		catch (IOException e) {
			System.out.println("Caught an IOException. Exiting.");
		}
		catch (InterruptedException e) {
			System.out.println("Caught an InterruptedException. Exiting.");
		}
		catch (ClassNotFoundException e) {
			System.out.println("Caught a ClassNotFoundException. Exiting.");
		}


	}

	private void waitForPeer() throws InterruptedException {
		peer.acquire();
	}

	private OutputStream peerArrived(OutputStream out) throws IOException {
		this.out = new ObjectOutputStream(out);
		peer.release();
		return clientSocket.getOutputStream();
	}
}

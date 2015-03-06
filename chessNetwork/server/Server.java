package chessNetwork.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

/**
  * A fairly generic server class that listens on port 9879; waits for clients to connect, then
  * passes the client's socket off to a ClientHandler object in its own thread
  */
public class Server {
	private static final int PORT = 9879;

	/**
	  * Runs the server; establishes the server socket then indefinitely listens on it for clients
	  * to connect, passing them off to handlers as it does so
	  */
	public static void main(String args[]) {
		//when a client connects trying to create a new game, he is given an ID while he waits;
		//making this call will establish all of the possible IDs for waiting clients (of which
		//there are finitely many)
		ClientHandler.establishIDs();
		System.out.println("Done with IDs.");
		ServerSocket socket;
		try {
			socket = new ServerSocket(PORT);
		}
		catch (IOException e) {
			System.out.println("Couldn't create socket on port " + PORT);
			return;
		}

		while (true) {
			Socket clientSocket;
			try {
				clientSocket = socket.accept();
			}
			catch (IOException e) {
				System.out.println("Error accepting connection on socket.");
				return;
			}

			try {
				new Thread(new ClientHandler(clientSocket)).start();
			}
			catch (IOException e) {
				System.out.println("Couldn't create a new ClientHandler.");
				//no return	
			}
		}
	}
}

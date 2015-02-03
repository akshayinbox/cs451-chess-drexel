package chessNetwork;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 9879;

	public static void main(String args[]) {
		ClientHandler.establishIDs();
		System.out.println("Done with IDS.");
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

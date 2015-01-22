package chessNetwork;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 9878;

	public static void main(String args[]) {
		try {
			ServerSocket socket = new ServerSocket(PORT);

			while (true) {
				new Thread(new ClientHandler(socket.accept())).start();
			}
		}
		catch (IOException e) {
			System.out.println("Caught an IOException.");
		}
	}
}

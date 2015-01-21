package chessNetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 9878;
	public static void main(String args[]) {
		try {
			ServerSocket socket = new ServerSocket(PORT);

			while (true) {
				Socket clientSocket = socket.accept();
				new Thread(new ClientHandler(clientSocket)).start();
			}

		}
		catch (IOException e) {
			System.out.println("Caught an IOException.");
			return;
		}
	}
}

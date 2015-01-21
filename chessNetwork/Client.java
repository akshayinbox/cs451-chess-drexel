package chessNetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static final int PORT = 9878;

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Please enter a host to connect to.");
			return;
		}
		
		try {
			Socket socket = new Socket(args[0], PORT);
			PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String toWrite;
			do {
				toWrite = stdIn.readLine();
				socketOut.println(toWrite);
				System.out.println("Received: " + socketIn.readLine());
			} while (!toWrite.equals("end"));

			stdIn.close();
			socketOut.close();
			socketIn.close();
			socket.close();
		}
		catch (IOException e) {
			System.out.println("Caught an IOException.");
			return;
		}
	}
}

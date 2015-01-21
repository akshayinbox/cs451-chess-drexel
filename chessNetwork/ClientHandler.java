package chessNetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket clientSocket;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;	
	}

	@Override
	public void run() {
		try {
			PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String next;
			do {
				next = socketIn.readLine();
				System.out.println("Received: " + next);
				socketOut.println(next);
			} while (!next.equals("end"));

			/*
			ObjectOutputStream objStream = new ObjectOutputStream(clientSocket.getOutputStream());
			String tmp = "blahblabhalh";
			objStream.writeObject(tmp);
			objStream.close();
			*/

			socketOut.close();
			socketIn.close();
			clientSocket.close();
		}
		catch (IOException e) {
			System.out.println("Caught an IOException in a client thread.");
			return;
		}
	}
}

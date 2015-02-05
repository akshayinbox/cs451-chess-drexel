package chessNetwork;

import chessNetwork.client.Client;
import chessNetwork.messages.TestProcessor;

import java.io.IOException;

import java.util.Scanner;

public class MainTest {

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Plase include the game ID as a command line argument.");
			return;
		}

		int gameID;
		try {
			gameID = Integer.parseInt(args[0]);
		}
		catch (NumberFormatException e) {
			System.out.println("Please use only an integer as the game ID.");
			return;
		}
	
		Client client;
		try {
			client = new Client();
		}
		catch (IOException e) {
			System.out.println("Error: couldn't connect.");
			return;
		}

		try {
			if (gameID < 0) {
				gameID = client.createNewGame();
				if (gameID >= 0) {
					System.out.println("Your game ID is " + gameID);
					System.out.println("Waiting...");
					System.out.println("Got " + client.waitForPeer() + ". Can continue.");
				}
				else {
					System.out.println("Too many players waiting. Please try again momentarily.");
					return;
				}
			}
			else {
				System.out.println("Joining game. Wating...");
				if (client.joinExistingGame(gameID)) {
					System.out.println("Got true. Can continue.");
				}
				else {
					System.out.println("No game with that ID exists. Try again.");
					return;
				}
			}
		}
		catch (IOException e) {
			System.out.println("Error: couldn't create new game.");
			return;
		}

		Scanner scanner = new Scanner(System.in);
		client.readWrite(new TestProcessor());
		String s = scanner.nextLine();
		while (!s.equals("end")) {
			client.send(s);
			s = scanner.nextLine();
		}

		try {
			client.close();
		}
		catch (InterruptedException e) {
			System.out.println("Interrupted waiting for threads to die.");
		}
		catch (IOException e) {
			System.out.println("Error: couldn't close the connection to the server.");
			return;
		}
	}
}

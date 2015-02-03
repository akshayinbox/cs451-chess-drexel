package chessNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Scanner;

public class Client {
	private static final int PORT = 9879;

	private static class Read implements Runnable {
		private ObjectInputStream socketIn;
		public Read(ObjectInputStream socketIn) {
			this.socketIn = socketIn;
		}

		@Override
		public void run() {
			try {
				String s = (String) socketIn.readObject();
				while (s != null) {
					System.out.println(s);
					s = (String) socketIn.readObject();
				}
			}
			catch (Exception e) {
				System.out.println("Read error.");
				return;
			}
		}
	}

	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Please enter a host to connect to and a game id to join.");
			return;
		}

		int game;
		try {
			game = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e) {
			System.out.println("Please use only an integer for the game id.");
			return;
		}
		
		try {
			Socket socket = new Socket(args[0], PORT);
			ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.writeInt(game);
			socketOut.flush();
			ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream()); 
			if (game < 0)
			{
				int gameID = socketIn.readInt();
				if (gameID < 0) {
					System.out.println("Couldn't connect (too many games). Quitting.");
					return;
				}
				else {
					System.out.println("Your game ID is: " + gameID);
				}
			}
		
			new Thread(new Read(new ObjectInputStream(socket.getInputStream()))).start();

			Scanner in = new Scanner(System.in);
			String s = in.nextLine();
			while (!s.equals("end")) {
				socketOut.writeObject(s);
				s = in.nextLine();
			}
			
			socket.close();
		}
		catch (IOException e) {
			System.out.println("Caught an IOException.");
			return;
		}
	}
}

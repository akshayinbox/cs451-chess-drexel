package chessNetwork.client;

import chessNetwork.messages.ChatMessage;
import chessNetwork.messages.EndMessage;
import chessNetwork.messages.Message;
import chessNetwork.messages.MessageProcessor;
import chessNetwork.messages.MoveMessage;
import chessNetwork.messages.PromotionMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import chessBoard.Move;
import chessBoard.Promotion;

public class Client implements Serializable {
	private static final long serialVersionUID = -4556458826512852351L;
	private static final int PORT = 9879;
	private static final String URLs[] = new String[] {
	                                                    "localhost",
	                                                    "tux64-11.cs.drexel.edu",
	                                                    "tux64-12.cs.drexel.edu",
	                                                    "tux64-13.cs.drexel.edu",
	                                                    "tux64-14.cs.drexel.du"
													  };

	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	private ClientReader reader = null;
	private ClientWriter writer = null;
	private Thread readerThread = null;
	private Thread writerThread = null;

	/**
	  * Creates a new Client object by connecting to one of the pre-determined, internal URLs.
	  * Throws an IOException if it cannot connect.
	  */
	public Client() throws IOException {
		for (int i = 0; i < URLs.length; i++) {
			if (tryConnect(URLs[i])) {
				socketOut = new ObjectOutputStream(socket.getOutputStream());
				socketIn = new ObjectInputStream(socket.getInputStream());
				return;
			}
		}

		throw new IOException();
	}

	/**
	  * Tries to connect to the given host on the internally held PORT variable, returning true upon
	  * successful connection
	  * @param host the host to be connected to
	  * @return true if the connection was successful
	  */
	private boolean tryConnect(String host) {
		try {
			socket = new Socket(host, PORT);	
		}
		catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	  * Once initialized, this method will accept a string and send it as a chat message to the
	  * opponent
	  * @param text the chat message to be sent
	  */
	public void send(String text) {
		writer.send(new ChatMessage(text));
	}

	/**
	  * Once initialized, this method will accept a move and send it to the opponent
	  * @param move the move to be sent
	  */
	public void send(Move move) {
		writer.send(new MoveMessage(move));
	}
	
	/**
	  * Once initialized, this method will accept a promotion and send it to the oppoenent
	  * @param p the promotion to be send
	  */
	public void send(Promotion p) {
		writer.send(new PromotionMessage(p));
	}
	
	/**
	  * Once initialized, this method will accept a text string and send it as an EndMessage to the
	  * opponent
	  * @param text the text of the EndMessage to be sent
	  */
	public void sendEnd(String text) {
		writer.send(new EndMessage(text));
	}

	/**
	  * Communicates with the server to create a new game, with a particular time limit, for another
	  * user to connect to, returning the ID of the new game
	  * @param time the time limit in the game to be created
	  * @return the nonnegative ID of the new game or a number less than 0 if the server is too busy
	  */
	public int createNewGameWithTime(int time) throws IOException {
		socketOut.writeInt(-time);
		socketOut.flush();
		return socketIn.readInt();
	}

	/**
	  * After creating a game, forces the calling thread to block until the opponent has connected
	  * @return a meaningless number read from the server
	  */
	public int waitForPeer() throws IOException {
		System.out.println("Waiting for an integer (host)...");
		return socketIn.readInt();
	}

	/**
	  * Joins an exiting game with the corresponding ID on the server, returning the time limit of
	  * the game upon success
	  * @param gameID the ID of the game to be joined
	  * @return if successful, the time limit of the joined game (equals Integer.MAX_INT if there is
	  * no time limit); if not successful, returns a number less than 0
	  */
	public int joinExistingGame(int gameID) throws IOException {
		socketOut.writeInt(gameID);
		socketOut.flush();
		System.out.println("Waiting for an integer (guest)...");
		return socketIn.readInt();
	}

	/**
	  * Given a MessageProcessor to process messages read from the server, this method will create
	  * one thread for continually reading message from the server and one thread for writing
	  * messages to the server
	  * @param processor the MessageProcessor object that handles messages from the server
	  */
	public void readWrite(MessageProcessor processor) {
		reader = new ClientReader(socketIn, processor);
		writer = new ClientWriter(socketOut);
		readerThread = new Thread(reader);
		writerThread = new Thread(writer);
		readerThread.start();
		writerThread.start();
	}

	/**
	  * Shuts down all parts of the Client object, including closing the socket and making sure all
	  * threads exit. Throws an IOException if it cannot close the socket and throws an
	  * InterruptedException if it is interrupted while waiting on one of its child threads
	  */
	public void close() throws IOException, InterruptedException {
		if (writer != null) {
			writer.exit();
		}
		
		socket.close();
		if (readerThread != null) {
			readerThread.join();
		}

		if (writerThread != null) {
			writerThread.join();
		}
	}
}

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

	private boolean tryConnect(String host) {
		try {
			socket = new Socket(host, PORT);	
		}
		catch (IOException e) {
			return false;
		}

		return true;
	}

	//what I imagine happening here is that the UI will have some sort of event handler code (e.g.,
	//chat-button-pressed, or something), and it will then call this method, which will place the
	//message in a queue on the writer object. Another thread solely dedicated to writing messages
	//will then take things out of that queue.
	public void send(String text) {
		writer.send(new ChatMessage(text));
	}

	//see the above comments on how I imagined this could be used.
	public void send(Move move) {
		writer.send(new MoveMessage(move));
	}
	
	public void send(Promotion p) {
		writer.send(new PromotionMessage(p));
	}
	
	public void sendEnd(String text) {
		writer.send(new EndMessage(text));
	}

	public int createNewGame() throws IOException {
		socketOut.writeInt(-1);
		socketOut.flush();
		return socketIn.readInt();
	}

	public int waitForPeer() throws IOException {
		System.out.println("Waiting for an integer (host)...");
		return socketIn.readInt();
	}

	public boolean joinExistingGame(int gameID) throws IOException {
		socketOut.writeInt(gameID);
		socketOut.flush();
		System.out.println("Waiting for an integer (guest)...");
		return socketIn.readInt() >= 0;
	}

	public void readWrite(MessageProcessor processor) {
		reader = new ClientReader(socketIn, processor);
		writer = new ClientWriter(socketOut);
		readerThread = new Thread(reader);
		writerThread = new Thread(writer);
		readerThread.start();
		writerThread.start();
	}

	public void close() throws IOException, InterruptedException {
		if (writer != null)
		{
			writer.exit();
		}
		
		socket.close();
		if (readerThread != null && writerThread != null)
		{
			readerThread.join();
			writerThread.join();
		}
	}
}

package chessNetwork.messages;

/**
  * An interface for a class which can receive Messages and do something to process them
  */
public interface MessageProcessor {
	
	/**
	  * When implemented by a class, will handle all processing of the Message by getting its type
	  * and content
	  * @param message the Message to be processed
	  */
	public void process(Message message);
}

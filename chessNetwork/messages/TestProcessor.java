package chessNetwork.messages;

public class TestProcessor implements MessageProcessor {
	public void process(Message message)
	{
		System.out.println(message.getContent());
	}
}

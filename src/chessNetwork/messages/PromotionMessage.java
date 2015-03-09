package chessNetwork.messages;

import java.io.Serializable;

import chessBoard.Promotion;

/**
  * Message class for sending chess Promotions over the network
  */
public class PromotionMessage extends Message implements Serializable {

	private static final long serialVersionUID = -2827539188834043627L;

	/**
	  * Creates a Message of type PROMOTION with Promotion content p
	  * @param p the Promotion content of the Message
	  */
	public PromotionMessage(Promotion p) {
		super(MessageType.PROMOTION, p);
	}
}

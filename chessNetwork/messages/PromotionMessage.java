package chessNetwork.messages;

import java.io.Serializable;

import chessBoard.Promotion;

public class PromotionMessage extends Message implements Serializable {

	private static final long serialVersionUID = -2827539188834043627L;

	public PromotionMessage(Promotion p) {
		super(MessageType.PROMOTION, p);
	}
}

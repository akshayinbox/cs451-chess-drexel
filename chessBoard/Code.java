package chessBoard;

public enum Code {
	IN_CHECK(-2), NOT_LEGAL(-1), SUCCESS(1), PROMOTION(2), CASTLE_LEFT(3), CASTLE_RIGHT(4), EN_PASSANT(5);
	
    private int code;
	
	private Code(int code) {
		this.code = code;
	}

}

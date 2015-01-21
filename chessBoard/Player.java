package chessBoard;

public enum Player {
	PLAYER1(1), PLAYER2(2);
	
    private int code;
	
	private Player(int code) {
		this.code = code;
	}

}

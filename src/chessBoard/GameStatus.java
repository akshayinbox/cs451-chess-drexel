package chessBoard;

public enum GameStatus {
	STALEMATE(1), CHECKMATE(2), CONTINUE(3);
	
    private int status;
	
	private GameStatus(int status) {
		this.status = status;
	}

}

package chessPieces;

public enum PieceID {
	PAWN("P"), KING("A");
	
    private String id;
	
	private PieceID(String id) {
		this.id = id;
	}

}

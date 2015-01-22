package chessPieces;

public enum PieceID {
	PAWN("P"),ROOK("R"), KING("A");
	
    private String id;
	
	private PieceID(String id) {
		this.id = id;
	}
	
	public String getName() {
		return id;
	}

}

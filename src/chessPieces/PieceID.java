package chessPieces;

public enum PieceID {
	PAWN("P"), ROOK("R"), KNIGHT("K"), BISHOP("B"), QUEEN("Q"), KING("A");
	
    private String id;
	
	private PieceID(String id) {
		this.id = id;
	}
	
	public String getName() {
		return id;
	}

}

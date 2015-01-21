package chessBoard;


public class Move {

	private Coord fromPos, toPos;
	
	public Move(Coord from, Coord to) {
		this.fromPos = from;
		this.toPos = to;
	}
	
	public Coord getFrom() {
		return fromPos;
	}
	
	public Coord getTo() {
		return toPos;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean isEqual= false;

	    if (obj != null && obj instanceof Move) {
	    	Move other = (Move) obj;
	    	
	    	if (other.getFrom().getRow() == this.getFrom().getRow() &&
	    		other.getFrom().getCol() == this.getFrom().getCol())
	    		return true;
	    }

	    return false;
	}
}

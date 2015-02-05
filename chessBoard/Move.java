package chessBoard;

import java.io.Serializable;

public class Move implements Serializable {

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
	
	public String toString() {
		return fromPos.toString() + " to " + toPos.toString();
	}
	
	@Override
	public boolean equals(Object obj) {

	    if (obj != null && obj instanceof Move) {
	    	Move other = (Move) obj;
	    	
	    	//TODO: see if the coords are equal instead of this
	    	//need to override equals for Coord object
	    	if (other.getFrom().equals(fromPos) && other.getTo().equals(toPos))
	    		return true;
	    }

	    return false;
	}
}

package chessBoard;

import java.io.Serializable;

/**
 * A class representing a move from one position on the board to
 * another position.
 */

public class Move implements Serializable {

	private Coord fromPos, toPos;
	private Coord fromPosTranslated, toPosTranslated; //move from opponent's perspective
	
	/**
	 * Constructs a move object consiting of the original position and the new position for a piece.
	 * @param from Original position of piece.
	 * @param to New position of piece.
	 */
	public Move(Coord from, Coord to) {
		this.fromPos = from;
		this.toPos = to;
		this.fromPosTranslated = new Coord(7 - this.fromPos.getRow(), 7 - this.fromPos.getCol());
		this.toPosTranslated =  new Coord(7 - this.toPos.getRow(), 7 - this.toPos.getCol());

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
	
	public String toStringTranslated() {
		
		return fromPosTranslated.toString() + " to " + toPosTranslated.toString();
	}
	
	public Coord getFromTranslated() {
		return this.fromPosTranslated;
	}
	
	public Coord getToTranslated() {
		return this.toPosTranslated;
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

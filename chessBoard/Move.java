package chessBoard;

import java.io.Serializable;

public class Move implements Serializable {

	private Coord fromPos, toPos;
	private Coord fromPosTranslated, toPosTranslated;
	private Integer timeTaken;
	
	public Move(Coord from, Coord to, Integer timeTaken) {
		this.fromPos = from;
		this.toPos = to;
		this.fromPosTranslated = new Coord(7 - this.fromPos.getRow(), 7 - this.fromPos.getCol());
		this.toPosTranslated =  new Coord(7 - this.toPos.getRow(), 7 - this.toPos.getCol());
		this.timeTaken = timeTaken;
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
		//return new Coord(7 - this.fromPos.getRow(), 7 - this.fromPos.getCol());
	}
	
	public Coord getToTranslated() {
		return this.toPosTranslated;
		//return new Coord(7 - this.toPos.getRow(), 7 - this.toPos.getCol());
	}
	
	public Integer getTimeTaken() {
		return this.timeTaken;
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

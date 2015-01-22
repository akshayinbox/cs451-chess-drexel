package chessBoard;

public class Coord {

	private int row, col;
	
	public Coord(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Coord clone() {
		return new Coord(row, col);
	}
	
	public String toString() {
		return row + "," + col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
}

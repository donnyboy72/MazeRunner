/*
 * Tile is a class to keep track of all the cells in the maze
 * It takes in the x and y cords of the tile.
 * Has boolean values for:
 * 		top -> the top wall of tile
 * 		bottom -> the bottom wall of tile
 * 		left -> the left wall of the tile
 * 		right -> the right wall of tile
 * 		visited -> keeps track if tile has been visited
 * 					this is for the DFS method
 * 		start -> keeps track of the starting tile
 * 		end -> keep track of the ending tile
 * 		played -> keeps track of if the user has gone (played)
 * 				  on this tile.
 * 
 * There are getters and setters for all variables
 */
public class Tile {
	int x;
	int y;
	boolean top;
	boolean bottom;
	boolean left;
	boolean right;
	boolean visited;
	boolean start;
	boolean end;
	boolean played;
	
	/*
	 * Constructor
	 * Tile receives the x and y pixels and sets this.x and this.y to those values
	 * Sets all walls to be true
	 * Sets all variables that create the game to false
	 */
	
	Tile(int x, int y)
	{
		this.x =x;
		this.y = y;
		top = true;
		bottom = true;
		left = true;
		right = true;
		visited = false;
		start = false;
		end = false;
		played = false;
	}
	public String toString() {
		return "X value " + this.x + "Y value " + this.y;
	}
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public void setBottom(boolean bottom) {
		this.bottom = bottom;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public void setStart(boolean start) {
		this.start = start;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
	
	public void setPlayed(boolean played) {
		this.played = played;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean getTop() {
		return top;
	}

	public boolean getBottom() {
		return bottom;
	}

	public boolean getLeft() {
		return left;
	}

	public boolean getRight() {
		return right;
	}

	public boolean getVisited() {
		return visited;
	}
	
	public boolean getStart() {
		return start;
	}

	public boolean getEnd() {
		return end;
	}
	
	public boolean getPlayed() {
		return played;
	}

}

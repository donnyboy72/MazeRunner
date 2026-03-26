import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import java.util.Timer;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.Timer;

public class MazeRunner extends JPanel implements ActionListener, KeyListener
{
	
	/**
	 * MazeRunner class inherits from JPanel. This means MazeRunner is a panel it self.
	 * This is how MazeRunner is called in App.java and why it can be add to the JFrame.
	 * Doing this also allows for MazeRunner to draw/paint the maze and user inputs.
	 * 
	 * ActionListener is implemented so we are able to repaint the frame
	 * KeyListener is implemented so we are able to check for user inputs
	 */
	private static final long serialVersionUID = 1L;
	//graphics set up
	int windowW; //width of window
	int windowH; //height of window
	int tileSize = 25; // size of tiles
	
	//maze set up
	int mazeWidth;
	int mazeHeight;
	int start; // index for start value
	int end; // index for end value
	Tile[][] maze;
	
	//Player set up
	PlayerMoves playerMoves;
	
	//logic set up
	Timer gameLoop;
	Random random = new Random();
	
	//Colors
	Color bG = new Color(47,47,47); // gray like most dark modes
	Color bbBlue = new Color(14, 165, 233); // baby blue color
	
	/*
	 * Constructor, this is where the window is created and all the function calls
	 * are made to build the game
	 */
	
	MazeRunner(int width, int height)
	{
		this.windowW = width; // set window width
		this.windowH = height; // set window height
		setPreferredSize(new Dimension(windowW,windowH)); //set windows prefferd size
		setBackground(bG); //set background color to gray
		
		//make this window focus on key presses
		addKeyListener(this);
		setFocusable(true);
		
		//set maze size
		mazeWidth = 39;
		mazeHeight = 23;
		maze = new Tile[mazeWidth][mazeHeight];
		
		//set playerMoves
		playerMoves = new PlayerMoves(1000); //maze size is 897 this gives a cushion to that 
		
		//set player start and end to random size
		start = random.nextInt(mazeHeight);
		end = random.nextInt(mazeHeight);
		
		//set up player cords this push doesn't add tile to stack, it only tells
		//stack where the next tile will start
		playerMoves.push(0,start); 
		
		buildMaze(); //call function
		
		//game loop
		gameLoop = new Timer(16,this); // 60 fps
		gameLoop.start();
	}
	
	
	/*
	 * This method creates a 2D array of tiles from the custom Tile Class.
	 *  
	 * The x and y are multiplied by the tile size and then 100 is added to both.
	 * The reason for multiplying is so the tiles are further apart and the reason
	 * for adding 100 is so the maze starts at pixel 100,100. This centers the maze 
	 * in the window.
	 * 
	 * This also creates the starting and ending tiles for the maze
	 * 
	 * Lastly it calls the DFS function and passes in a random tile.
	 */
	private void buildMaze() 
	{
		
		for(int i = 0; i < mazeWidth; i ++) // x pos 100 -> 1080 jumping by 25
			for(int j = 0; j < mazeHeight; j++) // y pos 100 -> 680 jumping by 25
				maze[i][j] = new Tile(i*tileSize +100,j*tileSize+100); // create new tile
		
		
		//assign start of maze on left side of grid
		maze[0][start].setStart(true);
		maze[0][start].setPlayed(true);
		
		//assign end of maze on right side of grid
		maze[38][end].setEnd(true);
		
		//Call DFS
		DFS(random.nextInt(mazeWidth),random.nextInt(mazeHeight));
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//                                    Graphics
/////////////////////////////////////////////////////////////////////////////////////////
	

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}

	/*
	 * Draw function goes through every tile in the maze and draws it 
	 * 		depending on if tile:
	 * 				has been played
	 * 				is starting tile
	 * 				is ending tile
	 *		
	 *		for every tile it will check if the walls are broken or not 
	 *		and then draw them.
	 *
	 * Lastly if end is played a String value will appear saying you won
	 */
	private void draw(Graphics g)
	{
		
		//draw tile for game
		for(int i = 0; i < mazeWidth; i ++) {
			for(int j = 0; j < mazeHeight; j++) 
			{
				// create tile variable to keep code clean
				Tile tile = maze[i][j];
				int x = tile.x;
				int y = tile.y;
				
				//check if tile has been played
				if(tile.getPlayed()) 
				{
					g.setColor(bbBlue);
					g.fillRect(x, y, tileSize, tileSize);
				}
				
				//check if tile is start tile
				if(tile.getStart()) 
				{
					g.setColor(Color.GREEN);
					g.fillRect(x, y, tileSize, tileSize);
				}
				
				//check if tile is end tile
				if(tile.getEnd()) 
				{
					g.setColor(Color.RED);
					g.fillRect(x, y, tileSize, tileSize);
				}
				
				//draw each line of tile
				g.setColor(Color.WHITE);
				if(tile.getBottom()) g.drawLine(x, y+tileSize, x+tileSize, y+tileSize);
				if(tile.getTop()) g.drawLine(x, y, x+tileSize, y);
				if(tile.getRight()) g.drawLine(x+tileSize, y, x+tileSize, y+tileSize);
				if(tile.getLeft()) g.drawLine(x, y, x, y+tileSize);
			}
		}
		
		//check if end tile has been played
		if(maze[mazeWidth-1][end].getPlayed()) 
		{
			g.setColor(bbBlue);
			g.drawString("You Won", windowW/2-70, 75);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//                             Randomized Depth-First Search
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Depth-First Search uses a random tile and then checks if a random tile next to it
	 * has been visited. If not breaks the walls between the two tiles and contunies doing so.
	 * Once all bordering tiles have been visisted it back tracks until there is an unvisisted
	 * tile. DFS uses recursion to go through all tiles in maze[][] and will back track when the method
	 * runs out of neighbooring Tiles. When back tracking it will then check the neighbooring
	 * tiles and contiune down a new path. This repeats until every tile has been visited.
	 */
	public void DFS(int x, int y)
	{
		Tile currentTile = maze[x][y]; //creates a current tile
		currentTile.setVisited(true); // set current tile to visited
		
		int[] directions = {0,1,2,3}; // create a list of directions
		
		//randomly swaps ints in directions[] to make list random
		for(int i = 3; i > 0; i--)
		{
			int j = random.nextInt(i+1);
			
			int temp = directions[i];
			directions[i] = directions[j];
			directions[j] = temp;
		}
		
		//goes through the random list in directions and moves to that tile
		for(int direction: directions)
		{
			//create new x and y variables so they can be altered
			int newX = x; 
			int newY = y;
			
			//checks what int directions is and sets newX or newY to it's new value
			if(direction == 0) newX = x-1; //moves to the tile to the left
			if(direction == 1) newY = y -1; //moves to the tile up
			if(direction == 2) newX = x+1; // moves to the tile to the right
			if(direction == 3) newY = y+1; // moves to the time down
			
			//checks if the next tile to be called is on the gameboard if not continue through
			//ints in directions
			if(newX < 0 || newY < 0 || newX > mazeWidth-1 || newY > mazeHeight-1) continue;
			
			//create tile for the next one to be called by DFS
			Tile nextTile = maze[newX][newY];
			
			if(nextTile.getVisited()) continue;//checks if next tile has been visited
			
			//checks what int directions is and breaks the walls inbetween the two tiles
			if(direction == 0)
			{
				currentTile.setLeft(false);
				nextTile.setRight(false);
			}
			
			if(direction == 1)
			{
				currentTile.setTop(false);
				nextTile.setBottom(false);
			}
			
			if(direction == 2)
			{
				currentTile.setRight(false);
				nextTile.setLeft(false);
			}
			
			if(direction == 3)
			{
				currentTile.setBottom(false);
				nextTile.setTop(false);
			}

			DFS(newX,newY); //call DFS for the new Tile's x and y
		}
	}
	
	/*
	*this was for earlier testing no longer needed
	*public boolean checkIfAllTilesVisited()
	*{
	*	for(int i = 0; i < mazeWidth; i ++) {
	*		for(int j = 0; j < mazeHeight; j++) {
	*			Tile tile = maze[i][j];
	*			if(!tile.visited) return false;
	*		}
	*	}
	*	//System.out.println("Done");
	*	return true;
	*}
	*/
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//                             Player moves
	/////////////////////////////////////////////////////////////////////////////////////////
	
	
	/*
	 * This class is to track player movement
	 * It uses a stack to add which direction the player moves
	 * 		0 = left
	 * 		1 = up
	 * 		2 = right
	 * 		3 = down
	 * 
	 * Variables are:
	 * 		maxSize -> the size of the stack
	 * 		moves -> the array for the stack
	 * 		top -> used for indexing the top value of the stack
	 * 		startX -> tracks the x value of the tile being played
	 * 				  is index of of 2D array maze[][]
	 * 		startY -> tracks the y value of the tile being played
	 * 				  is index of of 2D array maze[][]
	 * 
	 * Uses a method directions, this method is simular to a Delimiter by 
	 * checking the previous value of the stack and comparing to the new
	 * value inputted.
	 */
	class PlayerMoves{
		
		private int maxSize; // maxe size of stack
		private int[] moves; // stack array
		private int top;     // top of stack index
		private int startX;  // starting x value
		private int startY;  // starting y value
		
		/*
		 * Constructor
		 * passes in parameter size this set the maxSize
		 * moves is now an int array with size of maxSize
		 * top = -1
		 */
		public PlayerMoves(int size) 
		{
			maxSize = size; //set max size
			moves = new int[maxSize]; // set moves to new array
			top = -1;
		}
		
		//push adds number to stack and increases top
		public void push(int num)
		{
			moves[++top] = num;
		}
		
		//this push sets the start x and y values. This is only called
		// once, when the start tile is created.
		public void push(int x, int y)
		{
			this.startX = x;
			this.startY = y;	
		}
		
		//pop removes top value and lowers top index
		public int pop()
		{
			return moves[top--];
		}
		
		/*
		 * Passes in a number to be added to stack. 
		 * If number is opposite direction than prevoius top 
		 * do not add number instead pop will be called. 
		 *
		 * When push or pop is called it also changing the current tile's 
		 * played value from false -> true and true -> false.
		 */
		public void dircetions(int num)
		{
			switch(num)
			{
				case 0: // left direction
					if(startX <= 0) return; //keeps from going of left side of board
					if(top >= 0 && moves[top] == 2) //checks if top is more than zero and prevouis top value
					{
						pop();//removes prevouis top
						maze[startX--][startY].setPlayed(false);// set played to false

					}
					else 
					{
						if(maze[startX][startY].getLeft()) return; //checks for wall to left
						push(num);
						maze[--startX][startY].setPlayed(true);//moves to tile to the left
					}
					break;
					
				case 1: // up direction
					if(startY <= 0) return; // keeps from going off top part of board
					if(top >= 0 && moves[top] == 3)  //checks if top is more than zero and prevouis top value
					{
						pop(); // remove prevouis top 
						maze[startX][startY--].setPlayed(false); //set played to false
					}
					else 
					{
						if(maze[startX][startY].getTop()) return; //checks for wall to top
						push(num);
						maze[startX][--startY].setPlayed(true);//moves to tile to the top
					}
					break;
					
				case 2: // right direction
					if(startX >= mazeWidth) return; // keeps from going off right side of board
					if(top >= 0 && moves[top] == 0) //checks if top is more than zero and prevouis top value
					{
						pop();// remove prevouis top
						maze[startX++][startY].setPlayed(false); // set played to false
					}
					else 
					{
						if(maze[startX][startY].getRight()) return; //checks for wall to right
						push(num);
						maze[++startX][startY].setPlayed(true);//moves to tile to the tight
					}
					break;
					
				case 3:  // down direction
					if(startY >= mazeHeight) return; // keeps from going bottom part of board
					if(top >= 0 && moves[top] == 1) //checks if top is more than zero and prevouis top value
					{
						pop(); // remove prevouis top
						maze[startX][startY++].setPlayed(false); // set played to false
					}
					else 
					{
						if(maze[startX][startY].getBottom()) return; //checks for wall to bottom
						push(num);
						maze[startX][++startY].setPlayed(true);//moves to tile to the bottom
					}
					break;
					
				default:
					push(num);//adds number if stack is empty
			}
		}
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//                             Actions performed
	/////////////////////////////////////////////////////////////////////////////////////////
		
	@Override
	public void actionPerformed(ActionEvent e) { 
		repaint(); //updates draw every 60fps
		if(maze[mazeWidth-1][end].getPlayed()) // checks if end tile has been played
			gameLoop.stop(); // end game
	}

	/*
	 * key pressed function checks what input the user clicks. Then calls directions in player
	 * moves class and passes corisponding number to the direction the user wants to move.
	 */
	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_W,KeyEvent.VK_UP:
			playerMoves.dircetions(1);
			break;
		case KeyEvent.VK_S,KeyEvent.VK_DOWN:
			playerMoves.dircetions(3);
			break;
		case KeyEvent.VK_D,KeyEvent.VK_RIGHT:
			playerMoves.dircetions(2);
			break;
		case KeyEvent.VK_A,KeyEvent.VK_LEFT:
			playerMoves.dircetions(0);
			break;
		}
	}
	

	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

}
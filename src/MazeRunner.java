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
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//graphics set up
	int windowW; //width of window
	int windowH; //height of window
	int tileSize = 25; // size of tiles
	
	//maze set up
	int mazeWidth;
	int mazeHeight;
	int start;
	int end;
	Tile[][] maze;
	
	//Player
	int startX = 0;
	int startY = start;
	PlayerMoves playerMoves;
	
	//logic
	Timer gameLoop;
	Random random = new Random();
	
	//Colors
	Color bG = new Color(47,47,47);
	Color bbBlue = new Color(14, 165, 233);
	/*
	 * Constructor, this is where the window is created and all the function calls
	 * are made to build the game
	 */
	
	MazeRunner(int width, int height)
	{
		this.windowW = width;
		this.windowH = height;
		setPreferredSize(new Dimension(windowW,windowH));
		setBackground(bG);
		
		addKeyListener(this);
		setFocusable(true);
		
		
		mazeWidth = 39;
		mazeHeight = 23;
		maze = new Tile[mazeWidth][mazeHeight];
		
		playerMoves = new PlayerMoves(1000); //maze size is 897 this gives a cushion to that 
		
		start = random.nextInt(mazeHeight);
		end = random.nextInt(mazeHeight);
		
		//set up player cords
		playerMoves.push(0,start);
		
		buildMaze();
		
		gameLoop = new Timer(1,this);
		gameLoop.start();
	}

	private void buildMaze() 
	{
		
		for(int i = 0; i < mazeWidth; i ++) // x pos 100 -> 1080 jumping by 25
			for(int j = 0; j < mazeHeight; j++) // y pos 100 -> 680 jumping by 25
				maze[i][j] = new Tile(i*tileSize +100,j*tileSize+100);
		
		//assign start of maze on left side of grid

		maze[0][start].setStart(true);
		maze[0][start].setPlayed(true);
		
		//assign end of maze on right side of grid
		maze[38][end].setEnd(true);
		
		
		
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

	private void draw(Graphics g)
	{
		
		//draw tile for game
		for(int i = 0; i < mazeWidth; i ++) {
			for(int j = 0; j < mazeHeight; j++) {
				Tile tile = maze[i][j];
				int x = tile.x;
				int y = tile.y;
				
				if(tile.getPlayed()) 
				{
					g.setColor(bbBlue);
					g.fillRect(x, y, tileSize, tileSize);
				}
				
				if(tile.getStart()) 
				{
					g.setColor(Color.GREEN);
					g.fillRect(x, y, tileSize, tileSize);
				}
				
				if(tile.getEnd()) 
				{
					g.setColor(Color.RED);
					g.fillRect(x, y, tileSize, tileSize);
				}
				
				
				
				g.setColor(Color.WHITE);
				if(tile.getBottom()) g.drawLine(x, y+tileSize, x+tileSize, y+tileSize);
				if(tile.getTop()) g.drawLine(x, y, x+tileSize, y);
				if(tile.getRight()) g.drawLine(x+tileSize, y, x+tileSize, y+tileSize);
				if(tile.getLeft()) g.drawLine(x, y, x, y+tileSize);
				
				
			}
		}
		
		if(maze[mazeWidth-1][end].getPlayed()) 
		{
			g.setColor(bbBlue);
			g.drawString("You Won", windowW/2-70, 75);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//                             Randomized Depth-First Search
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public void DFS(int x, int y)
	{
		Tile currentTile = maze[x][y];
		currentTile.setVisited(true);
		
		int[] directions = {0,1,2,3};
		for(int i = 3; i > 0; i--)
		{
			int j = random.nextInt(i+1);
			
			int temp = directions[i];
			directions[i] = directions[j];
			directions[j] = temp;
		}
		for(int direction: directions)
		{
			int newX = x;
			int newY = y;
			
			if(direction == 0) newX = x-1;
			if(direction == 1) newY = y -1;
			if(direction == 2) newX = x+1;
			if(direction == 3) newY = y+1;
			
			if(newX < 0 || newY < 0 || newX > mazeWidth-1 || newY > mazeHeight-1) continue;
			
			Tile neighborTile = maze[newX][newY];
			
			if(neighborTile.getVisited()) continue;
			
			if(direction == 0)
			{
				currentTile.setLeft(false);
				neighborTile.setRight(false);
			}
			
			if(direction == 1)
			{
				currentTile.setTop(false);
				neighborTile.setBottom(false);
			}
			
			if(direction == 2)
			{
				currentTile.setRight(false);
				neighborTile.setLeft(false);
			}
			
			if(direction == 3)
			{
				currentTile.setBottom(false);
				neighborTile.setTop(false);
			}
			
			DFS(newX,newY);
		}
		
		
	}

	public boolean checkIfAllTilesVisited()
	{
		for(int i = 0; i < mazeWidth; i ++) {
			for(int j = 0; j < mazeHeight; j++) {
				Tile tile = maze[i][j];
				if(!tile.visited) return false;
			}
		}
		//System.out.println("Done");
		return true;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//                             Player moves
	/////////////////////////////////////////////////////////////////////////////////////////
	
	class PlayerMoves{
		
		private int maxSize;
		private int[] moves;
		private int top;
		private int startX;
		private int startY;
		
		public PlayerMoves(int size) {
			maxSize = size;
			moves = new int[maxSize];
			top = -1;
		}
		
		public void push(int num)
		{
			moves[++top] = num;
		}
		
		public void push(int x, int y)
		{
			this.startX = x;
			this.startY = y;
			
		}
		public int pop()
		{
			return moves[top--];
		}
		
		public boolean isEmpty()
		{
			return (top == -1);
		}
		
		public void dircetions(int num)
		{
			switch(num)
			{
				case 0: // left direction
					if(startX <= 0) return; //keeps from going of left side of board
					if(top >= 0 && moves[top] == 2) {
						pop();
						//maze[startX][startY].setPlayed(false);
						maze[startX--][startY].setPlayed(false);

					}
					else {
						if(maze[startX][startY].getLeft()) return;
						push(num);
						maze[--startX][startY].setPlayed(true);
					}
					break;
					
				case 1: // up direction
					if(startY <= 0) return; // keeps from going off top side of board
					if(top >= 0 && moves[top] == 3){
						pop();
						//maze[startX][startY].setPlayed(false);
						maze[startX][startY--].setPlayed(false);
					}
					else {
						if(maze[startX][startY].getTop()) return;
						push(num);
						maze[startX][--startY].setPlayed(true);
					}
					break;
					
				case 2: // right direction
					if(startX >= mazeWidth) return; // keeps from going off right side of board
					if(top >= 0 && moves[top] == 0){
						pop();
						//maze[startX][startY].setPlayed(false);
						maze[startX++][startY].setPlayed(false);
					}
					else {
						if(maze[startX][startY].getRight()) return;
						push(num);
						maze[++startX][startY].setPlayed(true);
					}
					break;
					
				case 3:  // down direction
					if(startY >= mazeHeight) return; // keeps from going bottom top side of board
					if(top >= 0 && moves[top] == 1){
						pop();
						//maze[startX][startY].setPlayed(false);
						maze[startX][startY++].setPlayed(false);
					}
					else {
						if(maze[startX][startY].getBottom()) return;
						push(num);
						maze[startX][++startY].setPlayed(true);
					}
					break;
					
				default:
					push(num);
			}
		}
		
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void actionPerformed(ActionEvent e) { 
		repaint();
		if(maze[mazeWidth-1][end].getPlayed())
			gameLoop.stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

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
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_W,KeyEvent.VK_UP:
			
			break;
		case KeyEvent.VK_S,KeyEvent.VK_DOWN:
			
			break;
		case KeyEvent.VK_D,KeyEvent.VK_RIGHT:
			
			break;
		case KeyEvent.VK_A,KeyEvent.VK_LEFT:
			
			break;
		}
		
	}

}
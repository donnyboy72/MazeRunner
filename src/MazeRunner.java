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
	
	//Player set up
	int playerX;
	int playerY;
	int velocityX;
	int velocityY;
	
	Timer gameLoop;
	Random random = new Random();
	/*
	 * Constructor, this is where the window is created and all the function calls
	 * are made to build the game
	 */
	
	MazeRunner(int width, int height)
	{
		this.windowW = width;
		this.windowH = height;
		setPreferredSize(new Dimension(windowW,windowH));
		setBackground(new Color(47,47,47));
		
		addKeyListener(this);
		setFocusable(true);
		
		
		mazeWidth = 39;
		mazeHeight = 23;
		maze = new Tile[mazeWidth][mazeHeight];
		
		
		start = random.nextInt(mazeHeight);
		buildMaze();
		
		playerX = maze[0][start].getX() + 3;
		playerY = maze[0][start].getY() + 3;
		
		
		velocityX = 0;
		velocityY = 0;
		
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
		
		//set up player cords
		
		
		//assign end of maze on right side of grid
		end = random.nextInt(mazeHeight);
		maze[38][end].setEnd(true);
		
		
		
		DFS(random.nextInt(mazeWidth),random.nextInt(mazeHeight));
	}
	
	public void move() 
	{
		playerX += velocityX;
		playerY += velocityY;
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
		
		//draw player
		g.setColor(Color.BLUE);
		g.fillOval(playerX, playerY, 20,20);
			
		
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//if(checkIfAllTilesVisited()) 
			move();
			repaint();
		
		
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
			velocityX = 0;
			velocityY = -2;
			break;
		case KeyEvent.VK_S,KeyEvent.VK_DOWN:
			velocityX = 0;
			velocityY = 2;
			break;
		case KeyEvent.VK_D,KeyEvent.VK_RIGHT:
			velocityX = 2;
			velocityY = 0;
			break;
		case KeyEvent.VK_A,KeyEvent.VK_LEFT:
			velocityX = -2;
			velocityY = 0;
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_W,KeyEvent.VK_UP:
			velocityX = 0;
			velocityY = 0;
			break;
		case KeyEvent.VK_S,KeyEvent.VK_DOWN:
			velocityX = 0;
			velocityY = 0;
			break;
		case KeyEvent.VK_D,KeyEvent.VK_RIGHT:
			velocityX = 0;
			velocityY = 0;
			break;
		case KeyEvent.VK_A,KeyEvent.VK_LEFT:
			velocityX = 0;
			velocityY = 0;
			break;
		}
		
	}

}
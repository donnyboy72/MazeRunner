import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import java.util.Timer;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.Timer;

public class MazeRunner extends JPanel implements ActionListener, KeyListener
{
	
	//graphics set up
	int windowW; //width of window
	int windowH; //height of window
	int tileSize = 25; // size of tiles
	
	//maze set up
	int mazeWidth;
	int mazeHeight;
	Tile[][] maze;
	
	//Prims set up
	
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
		mazeWidth = 39;
		mazeHeight = 23;
		maze = new Tile[mazeWidth][mazeHeight];
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
		maze[0][random.nextInt(mazeHeight)].setStart(true);
		
		//assign end of maze on right side of grid
		maze[38][random.nextInt(mazeHeight)].setEnd(true);
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
			
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//                             Randomized Depth-First Search
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean checkIfallTilesVisited()
	{
		for(int i = 0; i < mazeWidth; i ++) {
			for(int j = 0; j < mazeHeight; j++) {
				Tile tile = maze[i][j];
				if(!tile.visited) return false;
			}
		}
		System.out.println("Done");
		return true;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!checkIfallTilesVisited())
			//randomRemoveLines();
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import java.util.Timer;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.Timer;

public class MazeRunner extends JPanel implements ActionListener, KeyListener
{
		
	int windowW; //width of window
	int windowH; //height of window
	int tileSize = 10; // size of tiles
	
	Tile[][] maze;
	Tile start;	
	
	ArrayList<Tile> mazeGrid;
	Timer gameLoop;
	
	Random random;
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
		mazeGrid = new ArrayList<>();
		buildGrid();
		random = new Random();
		System.out.println(mazeGrid.size());
		gameLoop = new Timer(1,this);
		gameLoop.start();
	}

	private void randomRemoveLines() {
	
		int number = random.nextInt(5841);
		int ranNum = random.nextInt(4);
	
			if(ranNum == 1) mazeGrid.get(number).setTop(false);
			if(ranNum == 2) mazeGrid.get(number).setBottom(false);
			if(ranNum == 3) mazeGrid.get(number).setRight(false);
			if(ranNum == 4) mazeGrid.get(number).setLeft(false);
	}

	private void buildGrid() 
	{
		
		for(int i = tileSize; i < 109; i ++) // x pos 100 -> 1080 jumping by 10
			for(int j = tileSize; j < 69; j++) // y pos 100 -> 680 jumping by 10
				mazeGrid.add(new Tile(i*tileSize, j*tileSize));
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}

	private void draw(Graphics g)
	{
		
		//draw tile for game
		for(Tile tile: mazeGrid)
		{
			g.setColor(Color.WHITE);
			int x = tile.x;
			int y = tile.y;
			if(tile.getBottom()) g.drawLine(x, y+tileSize, x+tileSize, y+tileSize);
			if(tile.getTop()) g.drawLine(x, y, x+tileSize, y);
			if(tile.getRight()) g.drawLine(x+tileSize, y, x+tileSize, y+tileSize);
			if(tile.getLeft()) g.drawLine(x, y, x, y+tileSize);
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		randomRemoveLines();
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
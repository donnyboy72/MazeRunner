import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;


public class Maze extends JPanel implements ActionListener{
	
	/*
	 * This class is to break the window up into tiles.
	 * The playable character will use the tile class as
	 * well as the game grid. 
	 * 
	 * The variables for this class are: x which is the the 
	 * x position of the top left corner of tile; y which is
	 * the y position of the top left corner of the tile;
	 * 
	 * and isThere which is a 1 or 0 to show wither the tile
	 * should be drawn
	 */
	class Tile
	{
		int x;
		int y;
		int isThere;
		Tile(int startX, int startY, int there)
		{
			x = startX; y = startY; 
			checkValue(there); // checks if the value is 1 or zero 
		}
		
		private void checkValue(int there)
		{
			if(there % 2 == 0) isThere = 1;
			else {
				isThere = 0;
			}
		}
	}
	
	int windowW; //width of window
	int windowH; //height of window
	int tileSize = 10; // size of tiles
	
	Random random = new Random(); 
	ArrayList<Tile> mazeGrid;
	Timer gameLoop;
	
	
	/*
	 * Constructor, this is where the window is created and all the function calls
	 * are made to build the game
	 */
	
	Maze(int width, int height)
	{
		this.windowW = width;
		this.windowH = height;
		setPreferredSize(new Dimension(windowW,windowH));
		setBackground(new Color(47,47,47));
		mazeGrid = new ArrayList<>();
		buildGrid();
		
		//gameLoop = new Timer(16,this);
		//gameLoop.start();
	}

	private void buildGrid() 
	{
		
		for(int i = tileSize; i < 109; i ++) // x pos 100 -> 1080 jumping by 10
			for(int j = tileSize; j < 69; j++) // y pos 100 -> 680 jumping by 10
				mazeGrid.add(new Tile(i*tileSize, j*tileSize, i * j % 2));
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
			if(tile.isThere == 1) g.setColor(Color.BLACK);
			
			else {
				g.setColor(Color.WHITE);
			}
			g.fillRect(tile.x,tile.y,tileSize,tileSize);
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

import javax.swing.JFrame;

public class App {
	public static void main(String[] args) {
		int width = 1200;//set width of window
		int height = 800; //set height of window
		
		JFrame f = new JFrame("Maze Runner");//create new JFrame for app
		
		f.setSize(width, height);//set size base of width and height
		f.setLocationRelativeTo(null);//sets the window to open in middle of screen
		f.setResizable(false);//keeps user from changing the size of the window
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ends app when you exit window
		
		MazeRunner mazeRunner = new MazeRunner(width,height);//create new maze runner class
		f.add(mazeRunner);//add maze runner to JFrame
		f.pack();//resizes window to all preferred sizes set
		
		f.setVisible(true);//make window visibile

	}
}

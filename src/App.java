import javax.swing.JFrame;

public class App {

	public static void main(String[] args) {
		int width = 1200;
		int height = 800; 
		
		JFrame f = new JFrame("Maze Runner");
		
		f.setSize(width, height);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Maze evo = new Maze(width,height);
		f.add(evo);
		f.pack();
		
		f.setVisible(true);

	}

}

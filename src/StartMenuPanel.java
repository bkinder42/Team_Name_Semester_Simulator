import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class StartMenuPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private Image background;

	public StartMenuPanel() {
		super();
		try {
			background = ImageIO.read(this.getClass().getResourceAsStream("UMBC-Library-with-Pond.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public StartMenuPanel(String fileName) {
		super();
		try {
			background = ImageIO.read(this.getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public StartMenuPanel(Image img) {
		super();
		background = img;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
	}
}

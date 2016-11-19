import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LoginImgPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private Image background;

	public LoginImgPanel() {
		super();
		try {
			background = ImageIO.read(this.getClass().getResourceAsStream("LoginBackground.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LoginImgPanel(String fileName) {
		super();
		try {
			background = ImageIO.read(this.getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LoginImgPanel(Image img) {
		super();
		background = img;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
		g.setColor(new Color(0,0,0,.7f));
		int offsetX = (getWidth() / 2) / 2;
		offsetX += offsetX / 2;
		int startX = getWidth() / 2 - offsetX;
		int lengthX = ((getWidth() / 2) + offsetX) - startX;
		int offsetY = (getHeight() / 2) / 2;
		offsetY += offsetY / 5;
		int startY = getHeight() / 2 - offsetY;
		int lengthY = ((getHeight() / 2) + offsetY) - startY;
		g.fillRect(startX, startY, lengthX, lengthY);
	}
}
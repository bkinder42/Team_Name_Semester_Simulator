import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;

public class StartMenu {

	private JFrame frame;
	private int opacity;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartMenu window = new StartMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		opacity = 230;
		
		frame = new JFrame();
		frame.setBounds(100, 100, 741, 617);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StartMenuPanel contentPane = new StartMenuPanel();
		contentPane.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		TransparentJButton btnStart = new TransparentJButton("Start");
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(new Color(btnStart.getBackground().getRed(), btnStart.getBackground().getBlue(),
				btnStart.getBackground().getGreen(), opacity));
		btnStart.setBounds(304, 121, 146, 35);
		contentPane.add(btnStart);
		btnStart.repaint();

		TransparentJButton btnLogincreateAccount = new TransparentJButton("Login/Create Account");
		btnLogincreateAccount.setForeground(Color.BLACK);
		btnLogincreateAccount.setOpaque(false);
		btnLogincreateAccount.setBackground(new Color(btnLogincreateAccount.getBackground().getRed(),
				btnLogincreateAccount.getBackground().getBlue(), btnLogincreateAccount.getBackground().getGreen(), opacity));
		btnLogincreateAccount.setBorderPainted(false);
		btnLogincreateAccount.setBounds(233, 187, 289, 35);
		contentPane.add(btnLogincreateAccount);

		TransparentJButton btnSettings = new TransparentJButton("Settings");
		btnSettings.setBorderPainted(false);
		btnSettings.setOpaque(false);
		btnSettings.setForeground(Color.BLACK);
		btnSettings.setBackground(new Color(btnSettings.getBackground().getRed(), btnSettings.getBackground().getBlue(),
				btnSettings.getBackground().getGreen(), opacity));
		btnSettings.setBounds(304, 249, 141, 35);
		contentPane.add(btnSettings);

		TransparentJButton btnExit = new TransparentJButton("Exit");
		btnExit.setOpaque(false);
		btnExit.setBackground(new Color(btnExit.getBackground().getRed(), btnExit.getBackground().getBlue(),
				btnExit.getBackground().getGreen(), opacity));
		btnExit.setBounds(309, 314, 141, 35);
		contentPane.add(btnExit);
	}
}

class TransparentJButton extends JButton{
	
	public TransparentJButton(String string) {
		super(string);
	}
	
	public TransparentJButton(){
		super();
	}

    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBackground().getAlpha() < 255) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}



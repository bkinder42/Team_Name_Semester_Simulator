import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class InfoFrame extends JFrame{

	private JPanel contentPane;
	private JTextField databaseName;
	private JTextField txtTble1;
	private JTextField txtTble2;
	private JTextArea txtInfoField;
	int width = 200, height = 300;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InfoFrame frame = new InfoFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InfoFrame() {
		setUndecorated(true);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, width, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 0, 0, 0));
		
		databaseName = new JTextField();
		databaseName.setFocusable(false);
		databaseName.setFocusTraversalKeysEnabled(false);
		databaseName.setEditable(false);
		databaseName.setFont(new Font("Monospaced", Font.BOLD, 17));
		databaseName.setForeground(new Color(50, 205, 50));
		databaseName.setOpaque(false);
		databaseName.setRequestFocusEnabled(false);
		databaseName.setText("DB: final_project");
		contentPane.add(databaseName);
		databaseName.setColumns(10);
		
		txtTble1 = new JTextField();
		txtTble1.setEditable(false);
		txtTble1.setFocusTraversalKeysEnabled(false);
		txtTble1.setFocusable(false);
		txtTble1.setOpaque(false);
		txtTble1.setForeground(new Color(50, 205, 50));
		txtTble1.setFont(new Font("Monospaced", Font.BOLD, 17));
		txtTble1.setText("TB: user_accounts");
		contentPane.add(txtTble1);
		txtTble1.setColumns(10);
		
		txtTble2 = new JTextField();
		txtTble2.setEditable(false);
		txtTble2.setFocusTraversalKeysEnabled(false);
		txtTble2.setFocusable(false);
		txtTble2.setOpaque(false);
		txtTble2.setFont(new Font("Monospaced", Font.BOLD, 17));
		txtTble2.setForeground(new Color(50, 205, 50));
		txtTble2.setText("TB: scoreboard");
		contentPane.add(txtTble2);
		txtTble2.setColumns(10);
		
		txtInfoField = new JTextArea();
		txtInfoField.setFocusTraversalKeysEnabled(false);
		txtInfoField.setEditable(false);
		txtInfoField.setFocusable(false);
		txtInfoField.setForeground(new Color(50, 205, 50));
		txtInfoField.setOpaque(false);
		txtInfoField.setLineWrap(true);
		txtInfoField.setFont(new Font("Monospaced", Font.BOLD, 17));
		txtInfoField.setText("EXIT(Cntrl+E):         Exits");
		contentPane.add(txtInfoField);
		txtInfoField.setColumns(10);
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}

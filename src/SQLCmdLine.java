import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.border.MatteBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class SQLCmdLine extends JPanel {
	private HashMap<String, String> conMap;
	private JTextArea sqlEnter;
	private JTextArea sqlConsole;
	private SQLConnection sqlCon;
	private InfoFrame infoFrame;
	private boolean exit;
	private String[] reservedWords;

	public SQLCmdLine(HashMap<String, String> conMap, JPanel parent) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image;
		try {
			image = ImageIO.read(this.getClass().getResourceAsStream("Reaper Cursor Curve.png"));
			Cursor c = toolkit.createCustomCursor(image , new Point(this.getX(), 
			           this.getY()), "img");
			setCursor (c);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		reservedWords = new String[] { "CREATE", "DROP", "TRUNCATE" };
		infoFrame = new InfoFrame();
		infoFrame.show();
		infoFrame.setVisible(false);
		Thread mouseLoc = new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println("Mouse Loc:(" + MouseInfo.getPointerInfo().getLocation().x + ","
							+ MouseInfo.getPointerInfo().getLocation().y + ")");
					if (infoFrame.isVisible()) {
						infoFrame.setBounds(MouseInfo.getPointerInfo().getLocation().x + 10,
								MouseInfo.getPointerInfo().getLocation().y + 10, infoFrame.getWidth(),
								infoFrame.getHeight());
					}
				}
			}
		});
		mouseLoc.start();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				SQLCmdLine cmd = null;
				for (Component c : parent.getComponents()) {
					if (c instanceof SQLCmdLine) {
						cmd = (SQLCmdLine) c;
					}
				}
				if (cmd.isVisible()) {
					if (e.getID() == KeyEvent.KEY_PRESSED) {
						if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
							infoFrame.setVisible(true);
						} else if (e.getKeyCode() == KeyEvent.VK_E && e.isControlDown()) {
							exit = true;
						}
					}
				}
				if (e.getID() == KeyEvent.KEY_RELEASED) {
					if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
						infoFrame.setVisible(false);
					}
				}
				return false;
			}
		});
		this.conMap = conMap;
		sqlCon = new SQLConnection(conMap);
		sqlCon.test();
		setLayout(new BorderLayout(0, 0));

		sqlConsole = new JTextArea();
		sqlConsole.setCaretColor(new Color(50, 205, 50));
		sqlConsole.setFont(new Font("Monospaced", Font.PLAIN, 20));
		sqlConsole.setEditable(false);
		sqlConsole.setLineWrap(true);
		sqlConsole.setForeground(new Color(50, 205, 50));
		sqlConsole.setSelectedTextColor(new Color(0, 0, 0));
		sqlConsole.setSelectionColor(new Color(50, 205, 50));
		sqlConsole.setBackground(new Color(0, 0, 0));
		JScrollPane consoleScroll = new JScrollPane(sqlConsole);
		add(consoleScroll, BorderLayout.CENTER);

		sqlEnter = new JTextArea();
		sqlEnter.setLineWrap(true);
		sqlEnter.setCaretColor(new Color(50, 205, 50));
		sqlEnter.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (sqlEnter.getText().equals("EXIT")){
						exit = true;
					}
					else if (sqlEnter.getText().toLowerCase().equals("raven")) {
						sqlConsole.setText(sqlConsole.getText() + "\nR4V3N:(*)>\n");
					} else if (!blockedStatement(sqlEnter.getText())) {
						sqlConsole.setText(sqlConsole.getText() + "\n" + sqlCon.runCmd(sqlEnter.getText()) + "\n");
						sqlEnter.setText("");
					} else {
						sqlConsole.setText(sqlConsole.getText()
								+ "\nAccess to some part of that command blocked\nReserved Key Words:\n\t"
								+ reservedWordsToString());

					}
				}
			}

			private String reservedWordsToString() {
				String toReturn = "";
				for (String s : reservedWords)
					toReturn += s + "\n\t";
				return toReturn;
			}

			private boolean blockedStatement(String query) {
				query = query.toUpperCase();
				// CREATE TABLE, DROP TABLE, truncate,
				for (String s : reservedWords)
					if (query.contains(s))
						return true;
				return false;
			}
		});
		sqlEnter.setFont(new Font("Monospaced", Font.PLAIN, 40));
		sqlEnter.setSelectedTextColor(new Color(0, 0, 0));
		sqlEnter.setSelectionColor(new Color(50, 205, 50));
		sqlEnter.setForeground(new Color(50, 205, 50));
		sqlEnter.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(50, 205, 50)));
		sqlEnter.setBackground(new Color(0, 0, 0));
		JScrollPane sqlEnterScroll = new JScrollPane(sqlEnter);
		sqlEnterScroll.setOpaque(true);
		add(sqlEnterScroll, BorderLayout.NORTH);
		sqlEnter.setColumns(10);
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

}

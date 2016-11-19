import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartMenu {

	private JFrame startMenu;
	private int opacity;
	private HashMap<String, ArrayList<String>> songs;
	private final Account userAccount = new Account();
	private HashMap<String, String> conConfig;
	private HashMap<String, Integer> soundConfig;
	private TransparentJButton btnAccount;
	private JLabel lblUsername;
	private int jServerPort;
	private String jServerIP;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartMenu window = new StartMenu();
					window.startMenu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void loadConfig() {
		File configFile = new File("Config.txt");
		if (configFile.exists() && !configFile.isDirectory()) {
			Scanner inFile;
			try {
				inFile = new Scanner(configFile);
				// Builds the configuration Map for the SQL Database connection
				conConfig = new HashMap<String, String>();
				String lineDiscard = inFile.nextLine();
				conConfig.put("Address", inFile.nextLine());
				conConfig.put("Username", inFile.nextLine());
				conConfig.put("Password", inFile.nextLine());
				conConfig.put("Database", inFile.nextLine());
				String tables = "";
				int tableNum = inFile.nextInt();
				lineDiscard = inFile.nextLine();
				System.out.println("Table Num: " + tableNum);
				for (int c1 = 0; c1 < tableNum; c1++) {
					tables += inFile.nextLine() + "/";
				}
				System.out.println("Tables: " + tables);
				conConfig.put("Tables", tables);

				soundConfig = new HashMap<String, Integer>();
				System.out.println();
				System.out.println(inFile.nextLine());
				soundConfig.put("Volume", inFile.nextInt());
				soundConfig.put("Language", inFile.nextInt());
				printConfigs();

				lineDiscard = inFile.nextLine();
				lineDiscard = inFile.nextLine();
				jServerPort = inFile.nextInt();
				lineDiscard = inFile.nextLine();
				jServerIP = inFile.nextLine();
				System.out.println("JServer Conn: " + jServerIP + ":" + jServerPort);
				inFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			buildConfig();
		}
	}

	private void buildConfig() {
		conConfig = new HashMap<String, String>();
		conConfig.put("Address", "jdbc:mysql://localhost:3306/");
		conConfig.put("Username", "root");
		conConfig.put("Password", "Ignitus1");
		conConfig.put("Database", "final_project");
		conConfig.put("Tables", "user_accounts/scoreboard");

		soundConfig = new HashMap<String, Integer>();
		soundConfig.put("Volume", 100);
		soundConfig.put("Language", 0);

		jServerPort = 1234;
		jServerIP = "127.0.0.1";
		saveConfig();
	}

	private void saveConfig() {
		try {
			System.out.println(configToString());
			PrintWriter writer = new PrintWriter(new FileWriter(new File("Config.txt")));
			writer.write(configToString());
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printConfigs() {
		String output = "Connection Config\n";
		for (String s : conConfig.keySet()) {
			output += s + "[" + conConfig.get(s) + "]\n";
		}
		output += "\nSound Config\n";
		for (String s : soundConfig.keySet()) {
			output += s + "[" + soundConfig.get(s) + "]\n";
		}
		System.out.println(output);
	}

	public String configToString() {
		String output = "//Server Info(String): Ip then user/pass/databaseName/Num of Tables Loading/Tables";
		output += "\n" + conConfig.get("Address");
		output += "\n" + conConfig.get("Username");
		output += "\n" + conConfig.get("Password");
		output += "\n" + conConfig.get("Database");
		output += "\n" + conConfig.get("Tables").split("/").length;
		for (int c1 = 0; c1 < conConfig.get("Tables").split("/").length; c1++)
			output += "\n" + conConfig.get("Tables").split("/")[c1];
		output += "\n//Sound(Int): Level/Language(English[0], Spanish[1])";
		output += "\n" + soundConfig.get("Volume");
		output += "\n" + soundConfig.get("Language");
		output += "\n//J4v4 Server Port Num\n1235\n127.0.0.1";
		return output;
	}

	/**
	 * Create the application.
	 */
	public StartMenu() {
		initialize();

	}

	public StartMenu(int id, String username, String password) {
		this.userAccount.setId(id);
		this.userAccount.setUser(username);
		this.userAccount.setPass(password);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loadConfig();

		opacity = 230;

		songs = loadSongs();
		System.out.println(songs.keySet());
		System.out.println(songs.get("Menu"));
		MusicThread musicThread = new MusicThread(songs.get("Menu"), soundConfig);
		musicThread.start();

		startMenu = new JFrame();
		startMenu.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				saveConfig();
				System.exit(0);
			}
		});
		startMenu.setBounds(100, 100, 741, 617);
		startMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		StartMenuPanel contentPane = new StartMenuPanel();
		contentPane.setBounds(0, 0, startMenu.getWidth(), startMenu.getHeight());
		startMenu.setContentPane(contentPane);
		contentPane.setLayout(null);

		lblUsername = new JLabel("User");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(399, 490, 92, 37);
		contentPane.add(lblUsername);

		TransparentJButton btnStart = new TransparentJButton("Start");
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(new Color(btnStart.getBackground().getRed(), btnStart.getBackground().getBlue(),
				btnStart.getBackground().getGreen(), opacity));
		btnStart.setBounds(309, 147, 146, 35);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// MainSimulator sim = new MainSimulator();
				// sim.run();
				// SidePaneTest test = new SidePaneTest(conConfig, jServerPort,
				// jServerIP, userAccount);
				// test.show();
				SQLConsoleTest test = new SQLConsoleTest(conConfig);
				test.show();
				startMenu.setVisible(false);
			}
		});
		contentPane.add(btnStart);
		btnStart.repaint();

		// MODIFY TO BECOME LOGOUT BUTTON IF LOGGED IN
		btnAccount = new TransparentJButton("Login/Create Account");
		btnAccount.setForeground(Color.BLACK);
		btnAccount.setOpaque(false);
		btnAccount.setBackground(new Color(btnAccount.getBackground().getRed(), btnAccount.getBackground().getBlue(),
				btnAccount.getBackground().getGreen(), opacity));
		btnAccount.setBorderPainted(false);
		loginGUIMod();
		contentPane.add(btnAccount);

		TransparentJButton btnSettings = new TransparentJButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsFrame settings = new SettingsFrame(musicThread);
				settings.show();
				startMenu.setVisible(false);
				Thread settingsListener = new Thread(new Runnable() {
					public void run() {
						while (!settings.isFinished() && !settings.isCancel()) {
							System.out.println("Modding Settings");
						}

						if (settings.isFinished()) {
							soundConfig.replace("Volume", (int) settings.getVolume());
							printConfigs();
							musicThread.setSoundConfig(soundConfig);
						} else {
							musicThread.getPlayer().changeVolume(soundConfig.get("Volume") / 10.0);
						}

						settings.dispose();
						startMenu.setVisible(true);
					}
				});
				settingsListener.start();
			}
		});
		btnSettings.setBorderPainted(false);
		btnSettings.setOpaque(false);
		btnSettings.setForeground(Color.BLACK);
		btnSettings.setBackground(new Color(btnSettings.getBackground().getRed(), btnSettings.getBackground().getBlue(),
				btnSettings.getBackground().getGreen(), opacity));
		btnSettings.setBounds(309, 228, 141, 35);
		contentPane.add(btnSettings);

		TransparentJButton btnExit = new TransparentJButton("Exit");
		btnExit.setOpaque(false);
		btnExit.setBackground(new Color(btnExit.getBackground().getRed(), btnExit.getBackground().getBlue(),
				btnExit.getBackground().getGreen(), opacity));
		btnExit.setBounds(309, 302, 141, 35);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveConfig();
				System.exit(0);
			}
		});
		contentPane.add(btnExit);
	}

	public HashMap<String, ArrayList<String>> loadSongs() {

		System.out.println("Loading Songs");
		ArrayList<String> menuSongs = readIn("MenuSongs.txt");
		ArrayList<String> happySongs = readIn("HappySongs.txt");
		ArrayList<String> neutralSongs = readIn("NeutralSongs.txt");
		ArrayList<String> sadSongs = readIn("SadSongs.txt");
		ArrayList<String> soundBites = readIn("SoundBites.txt");
		HashMap<String, ArrayList<String>> songListMap = new HashMap<String, ArrayList<String>>();
		songListMap.put("Menu", menuSongs);
		songListMap.put("Happy", happySongs);
		songListMap.put("Neutral", neutralSongs);
		songListMap.put("Sad", sadSongs);
		songListMap.put("SoundBites", soundBites);
		return songListMap;
	}

	private ArrayList<String> readIn(String file) {
		ArrayList<String> fileNames = new ArrayList<String>();
		Scanner inFile = new Scanner(this.getClass().getResourceAsStream(file));
		while (inFile.hasNextLine()) {
			String line = inFile.nextLine();
			System.out.println(line);
			fileNames.add(line);
		}
		inFile.close();
		return fileNames;
	}

	private void loginGUIMod() {
		if (userAccount.getUser() != "" && userAccount.getPass() != "") {
			btnAccount.setText("Logout");
			btnAccount.removeActionListener(btnAccount.getActionListeners()[0]);
			btnAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					userAccount.setUser("");
					userAccount.setPass("");
					userAccount.setId(0);
					loginGUIMod();
				}
			});
			btnAccount.setBounds(243, 490, 289 / 2, 35);
			lblUsername.setText(userAccount.getUser());
			lblUsername.setVisible(true);
		} else {
			btnAccount.setText("Login/Create Account");
			btnAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startMenu.setVisible(false);
					LoginFrame loginFrame = new LoginFrame(conConfig);
					loginFrame.show();
					Thread loginManager = new Thread(new Runnable() {
						public void run() {
							while (!loginFrame.isLoggedIn() && !loginFrame.isCanceled()) {
								System.out.println("Logging in");
							}

							if (loginFrame.isLoggedIn()) {
								userAccount.setUser(loginFrame.getUser());
								userAccount.setPass(loginFrame.getPass());
								userAccount.setId(loginFrame.getID());
								System.out.println(userAccount.toString());
								loginFrame.dispose();
								startMenu.setVisible(true);
								loginGUIMod();
							} else {
								loginFrame.dispose();
								startMenu.setVisible(true);
							}
						}
					});
					loginManager.start();
				}
			});
			btnAccount.setBounds(243, 490, 289, 35);
			lblUsername.setText("User");
			lblUsername.setVisible(false);
		}
	}
}

class TransparentJButton extends JButton {

	public TransparentJButton(String string) {
		super(string);
	}

	public TransparentJButton() {
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

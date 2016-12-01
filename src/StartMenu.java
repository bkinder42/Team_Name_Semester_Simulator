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
import javax.swing.JOptionPane;

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
					//Builds the frame and starts the program
					StartMenu window = new StartMenu();
					window.startMenu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void loadConfig() {
		//Loads in the file
		File configFile = new File("Config.txt");
		if (configFile.exists() && !configFile.isDirectory()) {//Checks for file existence
			Scanner inFile;
			try {
				//Used to pull content from file
				inFile = new Scanner(configFile);
				// Builds the configuration Map for the SQL Database connection
				conConfig = new HashMap<String, String>();
				String lineDiscard = inFile.nextLine();
				conConfig.put("Address", inFile.nextLine());
				conConfig.put("Username", inFile.nextLine());
				conConfig.put("Password", inFile.nextLine());
				conConfig.put("Database", inFile.nextLine());
				//Tables list is seperated by a / each time, gets int for num of tables then reads in tables
				String tables = "";
				int tableNum = inFile.nextInt();
				lineDiscard = inFile.nextLine();
				System.out.println("Table Num: " + tableNum);
				for (int c1 = 0; c1 < tableNum; c1++) {
					tables += inFile.nextLine() + "/";
				}
				System.out.println("Tables: " + tables);
				conConfig.put("Tables", tables);

				//Loads the sound configuration properties
				soundConfig = new HashMap<String, Integer>();
				System.out.println();
				System.out.println(inFile.nextLine());
				soundConfig.put("Volume", inFile.nextInt());
				soundConfig.put("Language", inFile.nextInt());
				printConfigs();

				//Removes some unnecessary lines
				lineDiscard = inFile.nextLine();
				lineDiscard = inFile.nextLine();
				//pulls in port of the JServer
				jServerPort = inFile.nextInt();
				lineDiscard = inFile.nextLine();
				//pulls in IP of JServer
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
		//Creates the config directly 
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
		//Saves the congig
		try {
			System.out.println(configToString());
			PrintWriter writer = new PrintWriter(new FileWriter(new File("Config.txt")));//Writer for config file
			writer.write(configToString());// Pushes the string of the config file
			writer.close();//Closes and flushes the string to the file

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printConfigs() {
		String output = "Connection Config\n";//Creates the output variable
		//Reads the settings from the Connection map and appends them to the ouput
		for (String s : conConfig.keySet()) {
			output += s + "[" + conConfig.get(s) + "]\n";
		}
		//Same as above but with sound
		output += "\nSound Config\n";
		for (String s : soundConfig.keySet()) {
			output += s + "[" + soundConfig.get(s) + "]\n";
		}
		System.out.println(output);
	}

	public String configToString() {
		//Creates the output for the config txt
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
	 * Default constructor calls the initialize method which handles the windo building
	 */
	public StartMenu() {
		initialize();

	}
	/**
	 * Alternative constructor used for testing with a passed in id(int), username(String), and password(String)
	 * @param id
	 * @param username
	 * @param password
	 */
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
		//Loads the config and builds the config variables
		loadConfig();

		//Sets the default opacity value
		opacity = 230;

		//Loads in the songs: Keys are Strings of Song Types / Maps to ArrayList<String> of song names
		songs = loadSongs();
		System.out.println(songs.keySet());
		System.out.println(songs.get("Menu"));
		//Creates the thread to handle music
		MusicThread musicThread = new MusicThread(songs.get("Menu"), soundConfig);
		musicThread.start();

		startMenu = new JFrame();
		//Custom closing operation to save the config before exit
		startMenu.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				saveConfig();
				System.exit(0);
			}
		});
		//Establishes the properties of the fame
		startMenu.setBounds(100, 100, 741, 617);
		startMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		StartMenuPanel contentPane = new StartMenuPanel();
		contentPane.setBounds(0, 0, startMenu.getWidth(), startMenu.getHeight());
		startMenu.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Creates a label with the text User then assigns properties
		lblUsername = new JLabel("User");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(399, 490, 92, 37);
		contentPane.add(lblUsername);

		//Creates the start buttom
		TransparentJButton btnStart = new TransparentJButton("Start");
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(new Color(btnStart.getBackground().getRed(), btnStart.getBackground().getBlue(),
				btnStart.getBackground().getGreen(), opacity));
		btnStart.setBounds(309, 147, 146, 35);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Builds the main simulator
				MainSimulator sim = new MainSimulator();
				sim.run(conConfig, userAccount, songs, musicThread);
				startMenu.setVisible(false);
				//Thread to control window switching
				Thread playing = new Thread(new Runnable() {
					public void run() {
						while (sim.isPlaying()) {
							System.out.println("Sim Playing: " + sim.isPlaying());
						}
						sim.getFrame().setVisible(false);
						sim.getFrame().dispose();
						startMenu.setVisible(true);
						musicThread.setSongs(songs.get("Menu"));
						musicThread.getPlayer().play();
						
					}
				});
				playing.start();
			}
		});
		contentPane.add(btnStart);
		btnStart.repaint();

		//Creates the loginbutton, modified to logout with successful login
		btnAccount = new TransparentJButton("Login/Create Account");
		btnAccount.setForeground(Color.BLACK);
		btnAccount.setOpaque(false);
		btnAccount.setBackground(new Color(btnAccount.getBackground().getRed(), btnAccount.getBackground().getBlue(),
				btnAccount.getBackground().getGreen(), opacity));
		btnAccount.setBorderPainted(false);
		loginGUIMod();
		contentPane.add(btnAccount);

		//Creates the settings button and assigns properties
		TransparentJButton btnSettings = new TransparentJButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsFrame settings = new SettingsFrame(musicThread);
				settings.show();
				startMenu.setVisible(false);
				//Thread to control multiwindow switching between menu and settings
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

		//Exit button and properties
		TransparentJButton btnExit = new TransparentJButton("Exit");
		btnExit.setOpaque(false);
		btnExit.setBackground(new Color(btnExit.getBackground().getRed(), btnExit.getBackground().getBlue(),
				btnExit.getBackground().getGreen(), opacity));
		btnExit.setBounds(309, 302, 141, 35);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Same as default close operation save config then close and kill all threads
				saveConfig();
				System.exit(0);
			}
		});
		contentPane.add(btnExit);
	}

	public HashMap<String, ArrayList<String>> loadSongs() {
		//Loads in the song titles from the specific text files
		System.out.println("Loading Songs");
		ArrayList<String> menuSongs = readIn("MenuSongs.txt");
		ArrayList<String> happySongs = readIn("HappySongs.txt");
		ArrayList<String> neutralSongs = readIn("NeutralSongs.txt");
		ArrayList<String> sadSongs = readIn("SadSongs.txt");
		ArrayList<String> soundBites = readIn("SoundBites.txt");
		HashMap<String, ArrayList<String>> songListMap = new HashMap<String, ArrayList<String>>();
		//Assigns the songlists to keys
		songListMap.put("Menu", menuSongs);
		songListMap.put("Happy", happySongs);
		songListMap.put("Neutral", neutralSongs);
		songListMap.put("Sad", sadSongs);
		songListMap.put("SoundBites", soundBites);
		return songListMap;
	}

	private ArrayList<String> readIn(String file) {
		//Simple readin that returns all lines as seperate elements in an ArrayList
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
	/**
	 * Modifies the login button to reassign properties based on if someone is logged in or not, and controls the JLabel
	 */
	private void loginGUIMod() {
		//When the user is logged in
		if (userAccount.getUser() != "" && userAccount.getPass() != "") {
			btnAccount.setText("Logout");
			btnAccount.removeActionListener(btnAccount.getActionListeners()[0]);
			btnAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Assigns the values to nothing "logging out"
					userAccount.setUser("");
					userAccount.setPass("");
					userAccount.setId(0);
					//Updates button appearance
					loginGUIMod();
				}
			});
			btnAccount.setBounds(243, 490, 289 / 2, 35);
			//Assigns the username labels text to the username and makes visible
			lblUsername.setText(userAccount.getUser());
			lblUsername.setVisible(true);
		} else {//If not logged in
			btnAccount.setText("Login/Create Account");//Button is for logging in
			btnAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startMenu.setVisible(false);
					LoginFrame loginFrame = new LoginFrame(conConfig);
					loginFrame.show();
					//Creates a multiwindow thread for the loginframe
					Thread loginManager = new Thread(new Runnable() {
						public void run() {
							while (!loginFrame.isLoggedIn() && !loginFrame.isCanceled()) {
								System.out.println("Logging in");
							}

							if (loginFrame.isLoggedIn()) {
								userAccount.setUser(loginFrame.getUser());
								System.out.println("userAccount: " + userAccount.getUser());
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
			//Reassigns the text to a default and makes not visible
			lblUsername.setText("User");
			lblUsername.setVisible(false);
		}
	}
}
//Duplication of default button but with a modified paint to allow transparency
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

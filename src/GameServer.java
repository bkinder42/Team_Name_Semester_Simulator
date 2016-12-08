import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GameServer {
	private JFrame frame;
	private boolean sqlManager;
	private boolean chatServer;
	private int port;
	private ServerSocket server;
	private ArrayList<Connection> clients;
	
	public static void main(String[]Args){
		GameServer servers = new GameServer();
	}

	public GameServer() {
		sqlManager = false;
		chatServer = false;
		port = 42450;

		frame = new JFrame();
		GridLayout layout = new GridLayout();
		layout.setColumns(0);
		layout.setRows(2);
		frame.setLayout(new GridLayout());
		frame.setBounds(0, 0, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JButton btnServer = new JButton();
		btnServer.setText("Server Off");
		btnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chatServer) {
					btnServer.setText("Server Off");
				} else
					btnServer.setText("Server On");
				chatServer = !chatServer;
				buildServer();
			}
		});
		frame.add(btnServer, 0, 0);

		final JButton btnSqlCon = new JButton();
		btnSqlCon.setText("SQL Connection Off");
		btnSqlCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sqlManager)
					btnSqlCon.setText("SQL Connection Off");
				else
					btnSqlCon.setText("SQL Connection On");
				sqlManager = !sqlManager;
			}
		});
		frame.add(btnSqlCon, 1, 0);
		frame.show();
		
		/*
		 * truncate table final_project.scoreboard; INSERT INTO
		 * final_project.scoreboard (username, highscore) SELECT username,
		 * highscore FROM final_project.user_accounts order by highscore DESC;
		 * 
		 */
		Thread sqlManagerThread = new Thread(new Runnable() {
			public void run() {
				SQLConnection sqlCon = new SQLConnection(buildMap());
				while (frame.isVisible()) {
//					System.out.println(sqlManager);
					while (sqlManager) {
						System.out.println("Running command");
						sqlCon.runCmd("truncate table final_project.scoreboard;");
						sqlCon.runCmd("insert into final_project.scoreboard (username, highscore) select username, highscore "
								+ "FROM final_project.user_accounts ORDER BY highscore DESC;");
					}
				}
			}

			private HashMap<String, String> buildMap() {
				HashMap<String, String> conMap = new HashMap<String, String>();
				conMap.put("Address", "jdbc:mysql://localhost:3306/");
				conMap.put("Username", "root");
				conMap.put("Password", "Ignitus1");
				conMap.put("Tables", "user_accounts/scoreboard");
				return conMap;
			}
		});
		sqlManagerThread.start();
		buildServer();
	}

	private void buildServer() {
		clients = new ArrayList<Connection>();
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread conAccept = new Thread(new Runnable() {
			public void run() {
				try {
					while (chatServer) {
						Socket client = server.accept();
						Connection connectManager = new Connection(client);
						connectManager.start();
						clients.add(connectManager);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	class Connection extends Thread {
		private DataInputStream is = null;
		private PrintStream os = null;
		private Socket clientSocket = null;

		public Connection(Socket client) {
			this.clientSocket = client;
		}

		public void run() {

			try {
				is = new DataInputStream(clientSocket.getInputStream());
				os = new PrintStream(clientSocket.getOutputStream());
				while (chatServer) {
					String line = is.readLine();
					if (line.equals("<EXIT>"))
						break;
					synchronized (this) {
						for (int i = 0; i < clients.size(); i++) {
							clients.get(i).os.println(line);
						}
					}
				}
				is.close();
				os.close();
				clientSocket.close();
			} catch (IOException e) {
			}
		}
	}
}

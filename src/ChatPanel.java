import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ChatPanel extends JPanel {
	//Info for controlling the chat function
	private JTextField textField;
	private JTextArea chatWindow;
	private int jPort;
	private String jIP;
	private Socket clientSocket;
	private DataInputStream is;
	private PrintStream os;
	private Account userAccount;

	/**
	 * Create the panel.
	 */
	public ChatPanel(int port, String ip, Account acnt) {
		this.jIP = ip;
		this.jPort = port;
		try {
			//Binds a connection socket to the provided IP and Port
			clientSocket = new Socket(jIP, jPort);
			//Creates the output tool
			os = new PrintStream(clientSocket.getOutputStream());
			//Creates the input tool
			is = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout(0, 0));
		//Textfield for sending text, uses enter to send out text
		textField = new JTextField();
		//Keylistener for an enter key to send text
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					String toSend = "<" + userAccount.getUser() + ">" + textField.getText();
					chatWindow.setText(chatWindow.getText() + "\n" + toSend + "\n");
					os.println(toSend);
				}
			}
		});
		textField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		//JTextArea for displaying messages
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		add(chatWindow, BorderLayout.CENTER);

		//Thread to continuously read from the input
		Thread reading = new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						String input = is.readLine();
						chatWindow.setText(chatWindow.getText() + "\n" + input + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}

}

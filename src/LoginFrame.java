import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

	private LoginImgPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPass;
	private int id;
	private Font btnFont;
	private HashMap<String, String>conMap;
	private boolean loggedIn, canceled;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame(new HashMap<String, String>());
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
	public LoginFrame(HashMap<String, String> conMap) {
		id = 0;
		this.conMap = conMap;
		btnFont = new Font("Monospaced", Font.PLAIN, 30);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				canceled = true;
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 661, 490);
		contentPane = new LoginImgPanel("LoginBackground.jpg");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUser = new JLabel("User:");
		lblUser.setFont(new Font("Monospaced", Font.PLAIN, 38));
		lblUser.setForeground(new Color(50, 205, 50));
		lblUser.setBounds(95, 126, 132, 61);
		contentPane.add(lblUser);

		txtUser = new JTextField();
		txtUser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
					login();
			}
		});
		txtPass = new JTextField();

		txtUser.setBorder(new LineBorder(new Color(50, 205, 50)));
		txtUser.setFont(new Font("Monospaced", Font.PLAIN, 30));
		txtUser.setSelectionColor(new Color(50, 205, 50));
		txtUser.setBackground(Color.BLACK);
		txtUser.setForeground(new Color(50, 205, 50));
		txtUser.setCaretColor(new Color(50, 205, 50));
		txtUser.setText("(UserName)");
		txtUser.setBounds(225, 136, 273, 46);
		contentPane.add(txtUser);
		txtUser.setColumns(10);

		txtPass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		txtPass.setBorder(new LineBorder(new Color(50, 205, 50)));
		txtPass.setText("(Password)");
		txtPass.setSelectionColor(new Color(50, 205, 50));
		txtPass.setForeground(new Color(50, 205, 50));
		txtPass.setFont(new Font("Monospaced", Font.PLAIN, 30));
		txtPass.setColumns(10);
		txtPass.setCaretColor(new Color(50, 205, 50));
		txtPass.setBackground(Color.BLACK);
		txtPass.setBounds(225, 218, 273, 46);
		contentPane.add(txtPass);

		JLabel lblPassword = new JLabel("Pass:");
		lblPassword.setForeground(new Color(50, 205, 50));
		lblPassword.setFont(new Font("Monospaced", Font.PLAIN, 38));
		lblPassword.setBounds(95, 208, 132, 61);
		contentPane.add(lblPassword);

		final JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		btnLogin.setFont(new Font("Monospaced", Font.PLAIN, 30));
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnFont = btnLogin.getFont();
				btnLogin.setFont(new Font(btnFont.getFontName(), Font.BOLD, btnFont.getSize()));
			}

			public void mouseExited(MouseEvent arg0) {
				btnLogin.setFont(btnFont);
			}
		});
		btnLogin.setBorderPainted(false);
		btnLogin.setContentAreaFilled(false);
		btnLogin.setBorder(null);
		btnLogin.setBackground(new Color(0, 0, 0));
		btnLogin.setForeground(new Color(50, 205, 50));
		btnLogin.setBounds(95, 274, 177, 61);
		contentPane.add(btnLogin);

		final JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createAccount();
			}
		});
		btnCreateAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnFont = btnCreateAccount.getFont();
				btnCreateAccount.setFont(new Font(btnFont.getFontName(), Font.BOLD, btnFont.getSize()));
			}

			public void mouseExited(MouseEvent arg0) {
				btnCreateAccount.setFont(btnFont);
			}
		});
		btnCreateAccount.setForeground(new Color(50, 205, 50));
		btnCreateAccount.setFont(new Font("Monospaced", Font.PLAIN, 26));
		btnCreateAccount.setContentAreaFilled(false);
		btnCreateAccount.setBorderPainted(false);
		btnCreateAccount.setBorder(null);
		btnCreateAccount.setBackground(Color.BLACK);
		btnCreateAccount.setBounds(317, 267, 225, 72);
		contentPane.add(btnCreateAccount);
		
		final JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				System.out.println("Canceled");
			}
		});
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnFont = btnCancel.getFont();
				btnCancel.setFont(new Font(btnFont.getFontName(), Font.BOLD, btnFont.getSize()));
			}

			public void mouseExited(MouseEvent arg0) {
				btnCancel.setFont(btnFont);
			}
		});
		btnCancel.setForeground(new Color(50, 205, 50));
		btnCancel.setFont(new Font("Monospaced", Font.PLAIN, 30));
		btnCancel.setContentAreaFilled(false);
		btnCancel.setBorderPainted(false);
		btnCancel.setBorder(null);
		btnCancel.setBackground(Color.BLACK);
		btnCancel.setBounds(416, 82, 155, 46);
		contentPane.add(btnCancel);
	}

	protected void login() {
		if (isValidLogin()) {
			SQLConnection sqlCon = new SQLConnection(conMap);
			if (sqlCon.login(txtUser.getText(), txtPass.getText())){
				System.out.println("Logged In");
				id = sqlCon.getID();
				loggedIn = true;
			}else {
				JOptionPane.showMessageDialog(null, "Invalid User/Pass");
			}
		} else if (!isValidLoginLength())
			JOptionPane.showMessageDialog(null, "Invalid length");
		else
			System.out.println("Bad Stuff");
		
	}
	protected void createAccount(){
		SQLConnection sqlCon = new SQLConnection(conMap);
		if(isValidLogin())
			if(sqlCon.createAccount(txtUser.getText(), txtPass.getText()))
				loggedIn = true;
			else
				JOptionPane.showMessageDialog(null, "Failure to create Account");
		else if(!isValidLoginLength())
			JOptionPane.showMessageDialog(null, "Invalid Length");
		else
			System.out.println("Big Problems");
	}
	private boolean isValidLogin(){
		if (!txtUser.getText().equals("(Username)") && !txtPass.getText().equals("(Password)")
				&& isValidLoginLength())
			return true;
		else
			return false;
	}
	
	private boolean isValidLoginLength(){
		if (txtUser.getText().length() <= 0 || txtPass.getText().length() <= 0 || 
				txtUser.getText().length() > 32 || txtPass.getText().length() > 32)
			return false;
		else
			return true;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	public String getUser(){
		return txtUser.getText();
	}
	
	public String getPass(){
		return txtPass.getText();
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
}

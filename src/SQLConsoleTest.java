import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SQLConsoleTest extends JFrame {

	private JPanel contentPane;
	 /**
	 * Create the frame.
	 */
	public SQLConsoleTest(HashMap<String, String> conMap) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(new SQLCmdLine(conMap));
	}

}

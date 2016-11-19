import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SidePaneTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public SidePaneTest(HashMap<String, String> conMap, int jPort, String jIP, Account userAcc) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(new SimSidePanel(conMap, jPort, jIP, userAcc));
	}

}

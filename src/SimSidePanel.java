import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

//500x768
public class SimSidePanel extends JPanel {
	final int width = 500;
	private HashMap<String, String> conMap;
	private String jIP;
	private int jPort;
	private JTextField userInfo;
	private JTextField textField;
	private Account userAcc;
	private JTable scoreboard;
	private SQLConnection sqlCon;

	public SimSidePanel(HashMap<String, String> conMap, int jPort, String jIP, Account userAcc) {
		sqlCon = new SQLConnection(conMap);
		this.conMap = conMap;
		this.jIP = jIP;
		this.jPort = jPort;
		this.userAcc = userAcc;
		setLayout(new GridLayout(0, 1, 0, 0));

		ScoreBoardPane leaderbords = new ScoreBoardPane();
		leaderbords.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		add(leaderbords);
		leaderbords.setLayout(new BorderLayout(0, 0));

		JPanel fullBoard = new JPanel();
		leaderbords.add(fullBoard, BorderLayout.CENTER);
		fullBoard.setLayout(new BorderLayout(0, 0));
		fullBoard.setOpaque(false);

		scoreboard = new JTable();
		scoreboard.setRowHeight(30);
		scoreboard.setForeground(new Color(255,215,0));//Gold
//		scoreboard.setForeground(Color.GREEN);
		scoreboard.setFont(new Font("Tahoma", Font.BOLD, 25));
		scoreboard.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){{
			setOpaque(false);
		}});
		scoreboard.setRowSelectionAllowed(false);
		scoreboard.setOpaque(false);
		
		JScrollPane scrollScore = new JScrollPane(scoreboard);
		scrollScore.setOpaque(false);
		scrollScore.getViewport().setOpaque(false);
		fullBoard.add(scrollScore, BorderLayout.CENTER);

		JPanel userPlace = new JPanel();
		leaderbords.add(userPlace, BorderLayout.SOUTH);
		userPlace.setLayout(new BorderLayout(0, 0));

		userInfo = new JTextField();
		userInfo.setBorder(new MatteBorder(2, 0, 0, 0, (Color) new Color(0, 0, 0)));
		userPlace.add(userInfo, BorderLayout.NORTH);
		userInfo.setEditable(false);
		userInfo.setOpaque(true);
		userInfo.setColumns(10);
		userInfo.setFont(new Font("Takoma", Font.BOLD, 20));
		
		runContentUpdaters();
	}

	private void runContentUpdaters() {
		String scoreBoardText = "", userInfoText = "";

		Thread scoreboardPull = new Thread(new Runnable() {
			public void run() {
				while (true) {
					HashMap<Integer, String[]> scores = sqlCon.getScoreboard();
					DefaultTableModel dtm = new DefaultTableModel(0, 0);
					String[] columnHeads = new String[] { "Rank", "Username", "Score" };
					dtm.setColumnIdentifiers(columnHeads);
					scoreboard.setModel(dtm);

					boolean accountFound = false;
					for (int i : scores.keySet()) {
						dtm.addRow(new String[] { "" + i, scores.get(i)[0], scores.get(i)[1] });
						if (scores.get(i)[0].equals(userAcc.getUser())){
							userInfo.setText(" YOU-> " + i + "th place: " + scores.get(i)[1]);
							accountFound = true;
						}else{
							if(!accountFound)
							userInfo.setText("RETURN TO LOGIN TO SEE SCORE");
						}
					}
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		scoreboardPull.start();
	}
	class ScoreBoardPane extends JPanel{
		public ScoreBoardPane(){
			super();
		}
		public void paintComponent(Graphics g) {
			Image background;
			try {
				background = ImageIO.read(this.getClass().getResourceAsStream("Scoreboards Background.jpg"));
				g.drawImage(background.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
				g.setColor(new Color(100, 100, 100, 170));
				g.fillRect(0, 0, getWidth(), getHeight());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}

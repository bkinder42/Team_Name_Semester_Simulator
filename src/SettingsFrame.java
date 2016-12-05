import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SettingsFrame extends JFrame {

	private JPanel contentPane;
	private boolean finished;
	private int volume;
	private boolean cancel;

	public SettingsFrame(MusicThread playerThread) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				cancel = true;
			}
		});
		UIDefaults defaults = UIManager.getDefaults();    
	       defaults.put("Slider.tackWidth", 800);
		
		setResizable(false);
		setBounds(new Rectangle(0, 0, 480, 491));
		volume = 100;
		finished = false;
		cancel = false;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 480, 491);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		SettingsPanel settingsPanel = new SettingsPanel();
		contentPane.add(settingsPanel, BorderLayout.CENTER);
		settingsPanel.setLayout(null);


		final JSlider volumeSlide = new JSlider();
		volumeSlide.setToolTipText("Volume Level");
		volumeSlide.setOpaque(false);
		volumeSlide.setBounds(149, 172, 200, 43);
		volumeSlide.setValue((int) (playerThread.getPlayer().getVolume() * 10));
		volumeSlide.setMaximum(10);
		volumeSlide.setMinimum(0);
		volumeSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				double setVolume = volumeSlide.getValue() / 10.0;
				volume = volumeSlide.getValue();
				playerThread.getPlayer().changeVolume(setVolume);
			}
		});
		settingsPanel.add(volumeSlide);

		JLabel lblMusic = new JLabel("Volume");
		lblMusic.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblMusic.setForeground(new Color(255, 215, 0));
		lblMusic.setBackground(new Color(255, 215, 0));
		lblMusic.setBounds(178, 125, 139, 26);
		settingsPanel.add(lblMusic);
		
		final JButton btnFinished = new JButton("Finished");
		btnFinished.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnFinished.setForeground(new Color(255, 215, 0));
		btnFinished.setContentAreaFilled(false);
		btnFinished.setBorder(null);
		btnFinished.setBounds(58, 395, 141, 35);
		btnFinished.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				finished = true;
			}
		});
		btnFinished.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnFinished.setFont(new Font("Tahoma", Font.BOLD, 24));
			}
			
			public void mouseExited(MouseEvent arg0){
				btnFinished.setFont(new Font("Tahoma", Font.PLAIN, 24));
			}
		});
		settingsPanel.add(btnFinished);
		
		final JButton btnCancel = new JButton("Cancel");
		btnCancel.setContentAreaFilled(false);
		btnCancel.setForeground(new Color(255, 215, 0));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnCancel.setBorder(null);
		btnCancel.setBounds(265, 395, 141, 35);
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cancel = true;
			}
		});
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnCancel.setFont(new Font("Tahoma", Font.BOLD, 24));
			}
			
			public void mouseExited(MouseEvent arg0){
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 24));
			}
		});
		settingsPanel.add(btnCancel);
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
}

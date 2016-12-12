import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.soap.Text;

import controlP5.CColor;
import controlP5.ControlP5;
import controlP5.Textfield;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PSurface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * * File: MainSimulator * Author: Ben Kinder, Andrew Knox, Aaron Wood * Date
 * Created: 11/1/16 * Class: COMP 101 * Email: bkinder1@umbc.edu * Email:
 * andrewk6@umbc.edu * Description
 */

public class MainSimulator extends PApplet {
	// GUI Vars
	private static JFrame gameFrame;
	public final String processingCard = "Processing", sqlCard = "Sql";
	private boolean playing;
	private boolean saving;
	private HashMap<String, String> conMap;
	private Account userAcc;

	// Sound Vars
	private float soundHappyLvl;
	private int curClip;
	private Clip clip;

	private ControlP5 cp5;
	private float creditHours;
	private float workHours;
	private float classTime;
	private float studyTime;
	private float academicVisit;
	private float partyTime;
	private float wealthWeekly;
	private float wealthTotal;
	private float happyWeekly;
	private float happySum;
	private float happyAverage;
	private float gradeWeekly;
	private float gradeSum;
	private float gradeAverage;

	// Temporary variables for math shenanigans
	private float work;
	private float study;
	private float classt;
	private float aca;
	private float party;
	private float math;

	private Textfield creditHoursField;
	private Textfield workHoursField;
	private Textfield classTimeField;
	private Textfield studyTimeField;
	private Textfield academicVisitField;
	private Textfield partyTimeField;
	private PFont font;

	private int fieldRow1x;
	private int fieldRow2x;
	private int textRow1x;
	private int textRow2x;
	public int week;
	public int weekNum;
	private float brkTestHappy = 96; // int to stand in for happiness until
										// merged with Aaron's code
	private String errorText;

	private int randNum;
	private final float BASE_HAPPINESS = 50;
	private final float BASE_WEALTH = 0;
	private final float BASE_GRADE_POTENTIAL = 90;
	private final float HOURS_MAX = 160;
	private final float MIN_CREDITS = 12;
	private final float MAX_CREDITS = 24;
	private final float MAX_WORK = 40;

	public MainSimulator() {
		super();
	}

	public void run(HashMap<String, String> conMap, Account userAcnt, MusicThread soundThread) {
		this.conMap = conMap;
		this.userAcc = userAcnt;
		soundThread.getPlayer().stop();// Pauses the soundtrack from the main
										// menu
		playing = true;// Control variable for the multiwindow threading
		saving = false;
		// create your JFrame
		gameFrame = new JFrame("Main Game");
		gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Custom closing function to allow transition back to main menu
		gameFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				playing = false;
				System.out.println("Playing false");
			}
		});

		// create your sketch
		MainSimulator pt = new MainSimulator();

		// get the PSurface from the sketch
		PSurface ps = pt.initSurface();

		// initialize the PSurface
		ps.setSize(1024, 768);

		// get the SmoothCanvas that holds the PSurface
		SmoothCanvas smoothCanvas = (SmoothCanvas) ps.getNative();

		// Adds a JPanel to right of processing
		SimSidePanel sidePanel = new SimSidePanel(conMap, 0, "", userAcnt);
		sidePanel.setBounds(1024, 0, 500, 768);
		gameFrame.add(sidePanel, BorderLayout.EAST);

		// make your JFrame visible
		gameFrame.setSize(1030 + 500, 768);
		gameFrame.setVisible(true);

		// Builds Card Layout and Panels
		final JPanel processingPanel = new JPanel();
		processingPanel.add(smoothCanvas);// Adds the actual processing function
		final JPanel mainPane = new JPanel();// The main pane, has a card layout for
										// controlling stiching gameplay element
										// visible
		final SQLCmdLine sqlConsole = new SQLCmdLine(conMap, mainPane);// Pane to run
																	// SQL
																	// Commands
		mainPane.setLayout(new CardLayout());// Allows multiple panes to be
												// "stacked" only shows top,
		mainPane.add(processingPanel, processingCard); // Adds the
														// processingPanel and
														// assigns it a
														// reference string
		mainPane.add(sqlConsole, sqlCard);// Adds the sql panel and adds a
											// reference string
		final CardLayout cl = (CardLayout) (mainPane.getLayout());// Creates a
															// reference
															// cardlayout for
															// performing
															// functions
		cl.show(mainPane, processingCard);// Shows the default panel, the
											// processing panel
		// Creates a universal keyboard listener for switching panels
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (processingPanel.isVisible())
					if (e.getID() == KeyEvent.KEY_PRESSED)
						if (e.getKeyCode() == KeyEvent.VK_C && e.isShiftDown() && e.isControlDown()) {
							cl.show(mainPane, sqlCard);
							sqlConsole.setExit(false);
							// Creates a thread to handle the listening for the
							// exit from SQL panel
							class SQLExitThread extends Thread {
								public SQLExitThread() {
									super();
								}

								public void run() {
									while (!sqlConsole.isExit()) {
										System.out.println("In Console");
									}
									cl.show(mainPane, processingCard);
									this.stop();
								}
							}
							// Builds then starts the thread
							SQLExitThread sqlExit = new SQLExitThread();
							sqlExit.start();
							// Returns false to free the event for dispatching
							// elsewhere
							return false;
						}
				// Returns false to free the event for dispatching elsewhere
				return false;
			}
		});
		gameFrame.setResizable(false);
		gameFrame.add(mainPane);

		// start your sketch
		ps.startThread();
	}

	public static void main(String[] args) {
		// create your JFrame
		gameFrame = new JFrame("JFrame Test");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create your sketch
		MainSimulator pt = new MainSimulator();

		// get the PSurface from the sketch
		PSurface ps = pt.initSurface();

		// initialize the PSurface
		ps.setSize(1024, 768);

		// get the SmoothCanvas that holds the PSurface
		SmoothCanvas smoothCanvas = (SmoothCanvas) ps.getNative();

		// SmoothCanvas can be used as a Component
		gameFrame.add(smoothCanvas);

		// Adds a JPanel to right of processing
		JPanel panelTest = new JPanel();
		panelTest.setBackground(Color.white);
		panelTest.setBounds(1024, 0, 500, 768);
		gameFrame.add(panelTest);

		// make your JFrame visible
		gameFrame.setSize(1024 + 500, 768);
		gameFrame.setVisible(true);

		// start your sketch
		ps.startThread();
	}

	public void settings() {
		size(1024, 768);
	}
	
	public void loadGame(){
		try {
			BufferedReader inFile = new BufferedReader(new FileReader("LoadTemp.txt"));
			String line = inFile.readLine();
			System.out.println("Test Line: " + line);
			setWealth(Float.parseFloat(line));
			System.out.println("Wealth: " + wealthTotal);
			setCreditHours(Float.parseFloat(inFile.readLine()));
			System.out.println("Credit hours: " + creditHours);
			setGradeSum(Float.parseFloat(inFile.readLine()));
			System.out.println("Grade Sum: " + gradeSum);
			setHappySum(Float.parseFloat(inFile.readLine()));
			System.out.println("Happy Sum: " + happySum);
			setWeek(Integer.parseInt(inFile.readLine()));
			System.out.println("Week: " + week);
			inFile.close();
			Files.deleteIfExists(new File("LoadTemp.txt").toPath());
			if(happySum >= 75){
				startMusic(2);
			}else if(happySum < 75 && happySum >= 45){
				startMusic(1);
			}else{
				startMusic(0);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		happyWeekly = BASE_HAPPINESS;
		gradeWeekly = BASE_GRADE_POTENTIAL;
		soundHappyLvl = happyWeekly;
	}

	public void setup() {
		if((new File("LoadTemp.txt")).exists()){
			loadGame();
			System.out.println("File Exists" + (new File("LoadTemp.txt")).exists());
		}else{
			startMusic(1);
			firstWeekStats();
		}
		settings();
		errorText = "";
		fieldRow1x = 115;
		fieldRow2x = 500;
		textRow1x = 20;
		textRow2x = 390;
		weekNum = 1;
		font = createFont("arial", 16);

		background(128, 0, 128);
		cp5 = new ControlP5(this);
		fill(255);
        cp5.addButton("nextWeek").setSize(50, 20).setPosition(900, 700).setLabel("Next Week");
        cp5.addButton("weekStart").setSize(50, 20).setPosition(900, 500).setLabel("Start Week");
        cp5.addButton("creditSelect").setSize(50, 20).setPosition(330, fieldRow1x - 15).setLabel("Credits");


	}
	public void allThePrettyThings(){
        text("Credit Hours: ", textRow1x, 115);
        text("Work Hours: ", textRow1x, 165);
        text("Class Time: ", textRow1x, 215);
        text("Study Time: ", textRow1x, 265);
        text("Academic Visits: ", textRow2x, 115);
        text("Party Time: ", textRow2x, 165);
        text("1", 350, fieldRow1x - 20);
        text("2", 920, 495);
        text("3", 920, 695);

        text(errorText, 500, 500);


        text("Happiness: " + happyWeekly, 500, 300);
        text("Weekly Wealth: " + wealthWeekly, 500, 350);
        text("Weekly Grades: " + gradeWeekly, 500, 400);
        text("All your money: " + wealthTotal, 500, 500);
        text("Week: " + week, 500, 450);
        text("Class Time: " + "0 - " + creditHours, 200, 530);


        text("Ranges:", 200, 450);
        text("Work: " + "0 - 40", 200, 515);

        text("Study Time: " + "0 - 50", 200, 545);
        text("Academic Time: " + "0 - 30", 200, 560);
        text("Leisure Time: " + "0 - 60", 200, 575);

        creditHoursField = cp5.addTextfield("creditHours").setPosition(fieldRow1x, 100).setSize(200, 20)
                .setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
                .setFont(font);
        workHoursField = cp5.addTextfield("workHours").setPosition(fieldRow1x, 150).setSize(200, 20)
                .setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
                .setFont(font);
        classTimeField = cp5.addTextfield("classTime").setPosition(fieldRow1x, 200).setSize(200, 20)
                .setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
                .setFont(font);
        studyTimeField = cp5.addTextfield("studyTime").setPosition(fieldRow1x, 250).setSize(200, 20)
                .setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
                .setFont(font);
        academicVisitField = cp5.addTextfield("academicVisit").setPosition(fieldRow2x, 100).setSize(200, 20)
                .setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
                .setFont(font);
        partyTimeField = cp5.addTextfield("partyTime").setPosition(fieldRow2x, 150).setSize(200, 20)
                .setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
                .setFont(font);

    }

	public void draw() {
        background(128, 0, 128);
		weeklyRun();
	}


	public void firstWeekStats() {
		wealthWeekly = 0;
		happySum = 0;
		gradeSum = 0;
		week = 1;
		happyWeekly = BASE_HAPPINESS;
		gradeWeekly = BASE_GRADE_POTENTIAL;
		soundHappyLvl = happyWeekly;
	}
	public void theEnd(){
        endWeekStats();
	    endWeekMath();
        clip.stop();
    }


	public synchronized void weeklyRun() {
		if (isSchoolOver()){theEnd();}
		else allThePrettyThings();

	}

	public synchronized boolean isSchoolOver() {
		if (week > 5) {
			return true;
		} else
			return false;
	}
	public void endWeekMath(){
		happySum = happyAverage;
		happyAverage /= week;
		gradeSum = gradeAverage;
		gradeAverage /= week;
		scoreTally();

	}
	public void endWeekStats(){
        text("Final Happiness: " + happySum, 500, 300);
        text("Final Grades: " + gradeSum, 500, 400);
        text("Final Wealth: " + wealthTotal, 500, 350);
//        workHoursField.clear();
//        academicVisitField.clear();
//        studyTimeField.clear();
//        partyTimeField.clear();
//        classTimeField.clear();
        JFrame top = new JFrame();
        top.setAlwaysOnTop(true);
        top.setVisible(false);
        int exit = -1;
    }

	public void exportData() {
		try {
			if (week >= 1) {
				PrintWriter writer = new PrintWriter(new FileWriter(new File("Temp.txt")));
				writer.write(getWealth() + "\n");
				writer.write(getCreditHours() + "\n");
				writer.write(getGrades() + "\n");
				writer.write(getHappy() + "\n");
				writer.write(getWeek() + "");
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void weeklyReset() {
		happySum += happyWeekly;
		wealthTotal += wealthWeekly;
		gradeSum += gradeWeekly;
		wealthWeekly = 0;
		happyWeekly = BASE_HAPPINESS;
		gradeWeekly = BASE_GRADE_POTENTIAL;
		week += 1;
		exportData();
	}

	public synchronized void mathForWeek() {
		float negative;
		// credits
		if (creditHours >= 12) {
			happyWeekly -= 1.25f * creditHours;
			gradeWeekly -= 1.5f * creditHours;
		}
		// work
		wealthWeekly += (5 * workHours);
		if (workHours >= 21) {
			happyWeekly -= .5 * workHours;
			gradeWeekly -= .25 * workHours;
		} else
			happyWeekly -= .25 * workHours;
		if (wealthWeekly >= 55) {
			happyWeekly += 5;
		}
		// class time
		happyWeekly -= .5 * classTime;
		gradeWeekly += .25 * classTime;
		// study
		gradeWeekly += .25 * studyTime;
		happyWeekly -= .25 * studyTime;
		// academic visit
		gradeWeekly += .25 * academicVisit;
		happyWeekly -= .25 * academicVisit;
		// Leisure time
		if (partyTime <= 0) {
			gradeWeekly -= 50;
		}
		gradeWeekly -= 1.5 * partyTime;
		wealthWeekly -= 3 * partyTime;
		if ((wealthWeekly - 3 * partyTime) <= 0) {
			negative = (wealthWeekly -= 4.5 * partyTime);
			wealthTotal += negative;
			if (wealthTotal <= 0) {
				wealthTotal = 0;
			}
			wealthWeekly = 0;
			// happyWeekly = .25f*partyTime; probably not used
		} else
			happyWeekly += 1.5 * partyTime;
		// happy check
		if (happyWeekly <= 0) {
			happyWeekly = 0;
		}
		if (gradeWeekly <= 0) {
			gradeWeekly = 0;
		}
		startMusic(modSound());
	}

	public int modSound() {
		soundHappyLvl = happyWeekly / week;
		if (soundHappyLvl >= 75) {
			return 2;
		} else if (soundHappyLvl < 75 && happyWeekly >= 45) {
			return 1;
		} else if (soundHappyLvl < 45) {
			return 0;
		} else {
			return -1;
		}
	}

	public void startMusic(int val) {
		String song = "";
		switch (val) {
		case 0:
			song = "Sad_1.wav";
			break;
		case 1:
			song = "Neutral_1.wav";
			break;
		case 2:
			song = "Happy_1.wav";
			break;
		}
		try {
			if (clip != null)
				clip.stop();
			clip = AudioSystem.getClip();
			AudioInputStream inputStream;
			inputStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(song));
			clip.open(inputStream);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public float scoreTally() {
		float score = (happyAverage + gradeAverage + (wealthTotal / 1000));
		return score;
	}

	// GETTERS
	public synchronized float getWealth() {
		return wealthTotal;
	}

	public synchronized float getCreditHours() {
		return creditHours;
	}

	public synchronized float getGrades() {
		return gradeSum;
	}

	public synchronized float getHappy() {
		return happySum;
	}

	public synchronized int getWeek() {
		return week;
	}

	//////
	// SETTERS
	public synchronized void setWealth(float wealthTotal) {
		this.wealthTotal = wealthTotal;
	}

	public synchronized void setCreditHours(float creditHours) {
		this.creditHours = creditHours;
	}

	public synchronized void setGradeSum(float gradeSum) {
		this.gradeSum = gradeSum;
	}

	public synchronized void setHappySum(float happySum) {
		this.happySum = happySum;
	}

	public synchronized void setWeek(int week) {
		this.week = week;
	}
	////

	private boolean numberCheck() {
		study = Float.parseFloat(studyTimeField.getText());
		math = (HOURS_MAX - creditHours - work - classt - study - aca);
		work = Float.parseFloat(workHoursField.getText());
		classt = Float.parseFloat(classTimeField.getText());
		aca = Float.parseFloat(academicVisitField.getText());
		party = Float.parseFloat(partyTimeField.getText());

		if ((work >= 0 && work <= MAX_WORK) &&
				(classt >= 0 && classt <= creditHours)
				&& (study >= 0 && study <= 50) &&
				(aca >= 0 && aca <= 30)
				&& (party >= 0 && party <= 60) && !isSchoolOver()) {
			return true;
		}
		else JOptionPane.showMessageDialog(null, "Please check your ranges.");
		return false;
	}

	// Buttons

	public synchronized void weekStart() {
		if (numberCheck()) {
			workHours = Float.parseFloat(workHoursField.getText());
			workHoursField.lock();
			classTime = Float.parseFloat(classTimeField.getText());
			classTimeField.lock();
			studyTime = Float.parseFloat(studyTimeField.getText());
			studyTimeField.lock();
			academicVisit = Float.parseFloat(academicVisitField.getText());
			academicVisitField.lock();
			partyTime = Float.parseFloat(partyTimeField.getText());
			partyTimeField.lock();
			mathForWeek();
			randomEventerator();
		} else
            JOptionPane.showMessageDialog(null, "Please check your ranges.");

	}

	public synchronized int emotionValue() {
		if (happyWeekly >= 75) {
			return 2;
		}
		if (happyWeekly < 75 && happyWeekly >= 45) {
			return 1;
		}
		if (happyWeekly < 45) {
			return 0;
		} else
			return -1;
	}

	public void creditSelect() {
		float cred = Float.parseFloat(creditHoursField.getText());
		if (cred >= MIN_CREDITS && cred <= MAX_CREDITS) {
			creditHours = Float.parseFloat(creditHoursField.getText());
			creditHoursField.lock();
		} else
			JOptionPane.showMessageDialog(null, "Please pick a value between 12 and 24");
	}

	public void nextWeek() {
		if (numberCheck()) {
			workHoursField.unlock();
			classTimeField.unlock();
			studyTimeField.unlock();
			academicVisitField.unlock();
			partyTimeField.unlock();
			weeklyReset();
		} else
			JOptionPane.showMessageDialog(null, "Please check your ranges.");
	}

	public int randomPositiveGenerator() {
		Random rand = new Random();
		return randNum = rand.nextInt(100) + ((int)happyWeekly / 100);
	}
	public int randomNegativeGenerator(){
		Random rand = new Random();
		return randNum = rand.nextInt(100) - ((int)happyWeekly / 100);
	}

	public void randomEventerator(){
	    if (randomNegativeGenerator() <= 100 && (week > 1 && gradeSum < 10000 )){
            JOptionPane.showMessageDialog(null, "Your laziness has incurred the wrath of the Tonberry...");
            happyWeekly = 0;
            gradeWeekly = 0;
            wealthWeekly = 0;
        }
		else if (randomPositiveGenerator() > 60 && randomPositiveGenerator() < 65){
			JOptionPane.showMessageDialog(null, "A professor offers an easy extra credit assignment. Your grade and happiness have increased slightly.");
			happyWeekly += 10;
			gradeWeekly += 15;
		}
		else if (randomPositiveGenerator() > 66 && randomPositiveGenerator() < 71){
			JOptionPane.showMessageDialog(null, "You binge Game of Thrones and fall asleep to the beautiful sound of Kit Harrington's voice...without working on your Comp101 project.");
			happyWeekly += 10;
			gradeWeekly -= 10;
		}
        else if (randomNegativeGenerator() > 20 && randomPositiveGenerator() < 30){
			JOptionPane.showMessageDialog(null, "You decide a night of partying would be better than homework. Your happiness has increased, but grade has decreased");
			happyWeekly += 5;
			gradeWeekly -= 10;
			wealthWeekly -= 7;
		}
		else if (randomPositiveGenerator() > 80 && randomPositiveGenerator() < 90){
			JOptionPane.showMessageDialog(null, "You stay up very late, getting a lot of studying done. Your happiness has decreased, but grade has increased significantly");
			happyWeekly -= 7;
			gradeWeekly += 20;
		}
		else if (randomNegativeGenerator() > 45 && randomPositiveGenerator() < 35){
			JOptionPane.showMessageDialog(null, "You decide to work overtime for a few hours. Your happiness has decreased and wealth has increased");
			happyWeekly -= 7;
			wealthWeekly += 25;
		}
		else if (randomNegativeGenerator() > 10 && randomPositiveGenerator() < 0){
			JOptionPane.showMessageDialog(null, "You get in an argument with a friend. Your happiness has decreased significantly");
			happyWeekly -=25;
		}
	}


	public JFrame getFrame() {
		return gameFrame;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public void exit() {
		SQLConnection con = new SQLConnection(conMap);
		if (userAcc != null) {
			if (userAcc.getId() > 0) {
				con.setHighscore((int) scoreTally(), userAcc.getId());
			}
		}
		gameFrame.setVisible(false);
		if (!isSchoolOver()) {
			int save = JOptionPane.showOptionDialog(null, "Save Game?", "Save", 1, 1, null,
					new String[] { "Save", "Don't Save" }, 0);
			if (save == 0) {
				saving = true;

			} else {
				saving = false;
			}
		} else {
			saving = false;
		}
	}

	public void setSaving(boolean saving) {
		this.saving = saving;
	}

	public boolean getSaving() {
		return saving;
	}
}

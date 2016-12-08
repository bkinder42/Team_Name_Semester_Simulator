import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.soap.Text;

import controlP5.Slider;
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

/**
 * * File: MainSimulator
 * * Author: Ben Kinder, Andrew Knox, Aaron Wood
 * * Date Created: 11/1/16
 * * Class: COMP 101
 * * Email: bkinder1@umbc.edu
 * * Email: andrewk6@umbc.edu
 * * Description
 */

public class MainSimulator extends PApplet {
	//GUI Vars
    private static JFrame gameFrame;
    public final String processingCard = "Processing", sqlCard = "Sql";
    private boolean playing;
    private boolean saving;
    private HashMap<String, String> conMap;
    private Account userAcc;
    
    //Sound Vars
    private HashMap<String, ArrayList<String>> songLists;
    
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
    private float brkTestHappy = 96; //int to stand in for happiness until merged with Aaron's code
    private String errorText;

    private int randNum;
    private final float BASE_HAPPINESS = 50;
    private final float BASE_WEALTH = 0;
    private final float BASE_GRADE_POTENTIAL = 90;
    private final float HOURS_MAX = 160;
    private final float MIN_CREDITS = 12;
    private final float MAX_CREDITS = 24;
    private final float MAX_WORK = 40;
    
    public MainSimulator(){
    	super();
    }

    public void run(HashMap<String, String> conMap, Account userAcnt, HashMap<String, ArrayList<String>> songLists, MusicThread soundThread) {
    	this.conMap = conMap;
    	this.userAcc = userAcnt;
    	soundThread.getPlayer().stop();//Pauses the soundtrack from the main menu
    	this.songLists = songLists;
    	playing = true;//Control variable for the multiwindow threading
    	saving = false;
        //create your JFrame
        gameFrame = new JFrame("Main Game");
        gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Custom closing function to allow transition back to main menu
		gameFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				playing = false;
				System.out.println("Playing false");
			}
		});

        //create your sketch
        MainSimulator pt = new MainSimulator();

        //get the PSurface from the sketch
        PSurface ps = pt.initSurface();

        //initialize the PSurface
        ps.setSize(1024, 768);

        //get the SmoothCanvas that holds the PSurface
        SmoothCanvas smoothCanvas = (SmoothCanvas)ps.getNative();

        //Adds a JPanel to right of processing
        SimSidePanel sidePanel = new SimSidePanel(conMap, 0, "", userAcnt);
        sidePanel.setBounds(1024, 0, 500, 768);	
        gameFrame.add(sidePanel, BorderLayout.EAST);

        //make your JFrame visible
        gameFrame.setSize(1030 + 500, 768);
        gameFrame.setVisible(true);
        
        //Builds Card Layout and Panels
        JPanel processingPanel = new JPanel();
        processingPanel.add(smoothCanvas);//Adds the actual processing function
        JPanel mainPane = new JPanel();//The main pane, has a card layout for controlling stiching gameplay element visible
        SQLCmdLine sqlConsole = new SQLCmdLine(conMap, mainPane);//Pane to run SQL Commands
        mainPane.setLayout(new CardLayout());//Allows multiple panes to be "stacked" only shows top,
        mainPane.add(processingPanel, processingCard); //Adds the processingPanel and assigns it a reference string
        mainPane.add(sqlConsole, sqlCard);//Adds the sql panel and adds a reference string
        CardLayout cl = (CardLayout)(mainPane.getLayout());//Creates a reference cardlayout for performing functions
        cl.show(mainPane, processingCard);//Shows the default panel, the processing panel
        //Creates a universal keyboard listener for switching panels
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (processingPanel.isVisible())
					if (e.getID() == KeyEvent.KEY_PRESSED)
						if (e.getKeyCode() == KeyEvent.VK_C && e.isShiftDown() && e.isControlDown()){
							cl.show(mainPane, sqlCard);
							sqlConsole.setExit(false);
							//Creates a thread to handle the listening for the exit from SQL panel
							class SQLExitThread extends Thread{
								public SQLExitThread(){
									super();
								}
								public void run(){
									while(!sqlConsole.isExit()){
										System.out.println("In Console");
									}
									cl.show(mainPane, processingCard);
									this.stop();
								}
							}
							//Builds then starts the thread
							SQLExitThread sqlExit = new SQLExitThread();
							sqlExit.start();
							//Returns false to free the event for dispatching elsewhere
							return false;
						}
				//Returns false to free the event for dispatching elsewhere
				return false;
			}
		});
        gameFrame.setResizable(false);
        gameFrame.add(mainPane);
    	Thread musicController = new Thread(new Runnable(){
    		public void run(){
    			try{
    				int emotion = emotionValue();
    				switch(emotion){
    				case 0: soundThread.getPlayer().setSongs(songLists.get("Sad")); break;
    				case 1: soundThread.getPlayer().setSongs(songLists.get("Neutral")); break;
    				case 2: soundThread.getPlayer().setSongs(songLists.get("Happy"));; break;
    				}
    			}catch(NullPointerException e){
    				System.out.println("Temp null");
    			}
    		}
    	});
		soundThread.getPlayer().setSongs(songLists.get("Happy"));
    	musicController.start();
    	soundThread.getPlayer().play();

        //start your sketch
        ps.startThread();
    }
    public static void main(String[]args){
        //create your JFrame
        gameFrame = new JFrame("JFrame Test");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create your sketch
        MainSimulator pt = new MainSimulator();

        //get the PSurface from the sketch
        PSurface ps = pt.initSurface();

        //initialize the PSurface
        ps.setSize(1024, 768);

        //get the SmoothCanvas that holds the PSurface
        SmoothCanvas smoothCanvas = (SmoothCanvas)ps.getNative();

        //SmoothCanvas can be used as a Component
        gameFrame.add(smoothCanvas);

        //Adds a JPanel to right of processing
        JPanel panelTest = new JPanel();
        panelTest.setBackground(Color.white);
        panelTest.setBounds(1024, 0, 500, 768);
        gameFrame.add(panelTest);

        //make your JFrame visible
        gameFrame.setSize(1024 + 500, 768);
        gameFrame.setVisible(true);

        //start your sketch
        ps.startThread();
    }

    public void settings() {
        size(1024, 768);
    }

    public void setup() {
        firstWeekStats();
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
        creditHoursField = cp5.addTextfield("creditHours")
                .setPosition(fieldRow1x, 100)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);
        workHoursField = cp5.addTextfield("workHours")
                .setPosition(fieldRow1x, 150)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);
        classTimeField = cp5.addTextfield("classTime")
                .setPosition(fieldRow1x, 200)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);
        studyTimeField = cp5.addTextfield("studyTime")
                .setPosition(fieldRow1x, 250)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);
        academicVisitField = cp5.addTextfield("academicVisit")
                .setPosition(fieldRow2x, 100)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);
        partyTimeField = cp5.addTextfield("partyTime")
                .setPosition(fieldRow2x, 150)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);

        cp5.addButton("nextWeek").setSize(50, 20).setPosition(900, 700).setLabel("Next Week");
        cp5.addButton("weekStart").setSize(50, 20).setPosition(900, 500).setLabel("Start Week");
        cp5.addButton("creditSelect").setSize(50, 20).setPosition(330, fieldRow1x - 15).setLabel("Credits");
        cp5.addSlider("Test Slider").setPosition(500,500).setRange(0,100);


    }

    public void draw() {
        background(128, 0, 128);
        text("Credit Hours: ", textRow1x, 115);
        text("Work Hours: ", textRow1x, 165);
        text("Class Time: ", textRow1x, 215);
        text("Study Time: ", textRow1x, 265);
        text("Academic Visits: ", textRow2x, 115);
        text("Party Time: ", textRow2x, 165);
        text("1", 350, fieldRow1x - 20);
        text("2", 920, 495);
        text("3", 920, 695);
        weeklyStats();
        text(errorText, 500, 500);
        drawFaceImage();
    }

    private void drawFaceImage() {
    }

    public void firstWeekStats(){
        wealthWeekly = 0;
        happySum = 0;
        gradeSum = 0;
        week = 1;
        happyWeekly = BASE_HAPPINESS;
        gradeWeekly = BASE_GRADE_POTENTIAL;
    }
    public void weeklyStats() {
        if (!isSchoolOver()) {
            text("Happiness: " + happyWeekly, 500, 300);
            text("Weekly Wealth: " + wealthWeekly, 500, 350);
            text("Weekly Grades: " + gradeWeekly, 500, 400);
            text("All your money: " + wealthTotal, 500, 500);
            text("Week: " + week, 500, 450);
            //Below is not working properly
            text("Ranges:", 200, 450);
            text("Work:    " + "0 - 40", 200, 515);
            text("Class Time:    " + "0 - Max Credits", 200, 530);
            text("Study Time:    " + "0 - 2x Credits", 200, 545);
            text("Academic Time:    " + "0 - 1/2x Study Time", 200, 560);
            text("Leisure Time:    " + "0 - " + math, 200, 575);
        } else {
            text("Final Happiness: " + happySum, 500, 300);
            text("Final Grades: " + gradeSum, 500, 400);
            text("Final Wealth: " + wealthTotal, 500, 350);
            workHoursField.lock();
            academicVisitField.lock();
            studyTimeField.lock();
            partyTimeField.lock();
            classTimeField.lock();
            scoreTally();
            JFrame top = new JFrame();
            top.setAlwaysOnTop(true);
            top.setVisible(false);
            int exit = -1;
            JOptionPane.showMessageDialog(null, "Close To Return to Menu");
        }
    }
    public boolean isSchoolOver() {
        if (week > 15) {
            happySum = happyAverage;
            happyAverage /= week;
            gradeSum = gradeAverage;
            gradeAverage /= week;
            return true;
        } else return false;

    }

    public void weeklyReset(){
        happySum += happyWeekly;
        wealthTotal += wealthWeekly;
        gradeSum += gradeWeekly;
        wealthWeekly = 0;
        happyWeekly = BASE_HAPPINESS;
        gradeWeekly = BASE_GRADE_POTENTIAL;
        week += 1;
    }

    public void mathForWeek() {
        float negative;
        //credits
        if (creditHours >= 12) {
            happyWeekly -= 1.25f * creditHours;
            gradeWeekly -= 1.5f * creditHours;
        }
        //work
        wealthWeekly += (5 * workHours);
        if (workHours >= 21) {
            happyWeekly -= .5 * workHours;
            gradeWeekly -= .25 * workHours;
        } else happyWeekly -= .25 * workHours;
        if (wealthWeekly >= 55) {
            happyWeekly += 5;
        }
        //class time
        happyWeekly -= .5 * classTime;
        gradeWeekly += .25 * classTime;
        //study
        gradeWeekly += .25 * studyTime;
        happyWeekly -= .25 * studyTime;
        //academic visit
        gradeWeekly += .25 * academicVisit;
        happyWeekly -= .25 * academicVisit;
        //Leisure time
        if (partyTime <= 0) {
            gradeWeekly -= 50;
        }
        gradeWeekly -= 1.5 * partyTime;
        wealthWeekly -= 1 * partyTime;
        if ((wealthWeekly - 3 * partyTime) <= 0) {
            negative = (wealthWeekly -= 4.5 * partyTime);
            wealthTotal += negative;
            if (wealthTotal <= 0) {
                wealthTotal = 0;
            }
            wealthWeekly = 0;
            //happyWeekly = .25f*partyTime; probably not used
        } else happyWeekly += 1.5 * partyTime;
        // happy check
        if (happyWeekly <= 0) {
            happyWeekly = 0;
        }
        if (gradeWeekly <= 0) {
            gradeWeekly = 0;
        }
    }
    public float scoreTally(){
        float score = (happyAverage + gradeAverage + (wealthTotal / 1000));
        return score;
    }
    //GETTERS
    public float getWealth(){
        return wealthTotal;
    }
    public float getCreditHours(){
        return creditHours;
    }
    public float getGrades(){
        return gradeSum;
    }
    public float getHappy(){
        return happySum;
    }
    public float getWeek(){
        return week;
    }

    //////
    //SETTERS
    public void setWealth(int wealthTotal) {
        this.wealthTotal = wealthTotal;
    }
    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }
    public void setGradeSum(int gradeSum) {
        this.gradeSum = gradeSum;
    }
    public void setHappySum(int happySum) {
        this.happySum = happySum;
    }
    public void setWeek(int week) {
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
                (classt >= 0 && classt <= MAX_CREDITS) &&
                (study >= 0 && study <= (2*creditHours)) &&
                (aca >= 0 && aca <= (.5*study)) &&
                (party >= 0 && party <= math) &&
                !isSchoolOver()){
            return true;
        } return false;
    }


    //Buttons

    public void weekStart() {
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
            System.out.println("Credit Hours:\t\t" + creditHours + "\n" +
                    "Work Hours:\t\t\t" + workHours + "\n" +
                    "Class Time:\t\t\t" + classTime + "\n" +
                    "Study Time:\t\t\t" + studyTime + "\n" +
                    "Academic Visits:\t" + academicVisit + "\n" +
                    "Leisure Time:\t\t\t" + partyTime);
        }
        else System.out.println("you broke it");


    }

    public int emotionValue(){
        if (happyWeekly >= 75){return 2;}
        if (happyWeekly < 75 && happyWeekly >= 45){return 1;}
        if (happyWeekly < 45){return 0;}
        else return 0;
    }

    public void creditSelect() {
        float cred = Float.parseFloat(creditHoursField.getText());
        if (cred >= MIN_CREDITS && cred <= MAX_CREDITS) {
            creditHours = Float.parseFloat(creditHoursField.getText());
            creditHoursField.lock();
        } else System.out.println("Pick a value between 12 and 24");
    }

    public void nextWeek() {
        if (numberCheck()) {
            workHoursField.unlock().clear();
            classTimeField.unlock().clear();
            studyTimeField.unlock().clear();
            academicVisitField.unlock().clear();
            partyTimeField.unlock().clear();

            System.out.println("Total Wealth\t\t\t" + wealthTotal + "\n" +
                    "Total Grades\t\t\t" + gradeSum);
            weeklyReset();
        }
        else if (isSchoolOver()){
            text("EVERYTHING IS DONE", 500, 500);
        }
        else System.out.println("is brok");
    }

    public void randomGenerator(){
        Random rand = new Random();

        randNum = rand.nextInt(6) + 1;
    }

    private void randomEvents(){
        if(randNum == 1) {
            System.out.println("You decide a night of partying would be better than homework.");
            //decrease wealth and grades by small amount, increase happiness by medium amount
        }
        if(randNum == 2) {
            System.out.println("A professor offers an easy extra credit assignment.");
            //increase happiness and grade by small amount
        }
        if(randNum == 3) {
            System.out.println("You binge Game of Thrones and fall asleep to the beautiful\n" +
                    "sound of Kit Harrington's voice...without working on your Comp project.");
            //decrease grade by small amount and raise happiness by small amount
        }
        if(randNum == 4) {
            System.out.println("You stay up very late, getting a lot of studying done.");
            //decrease happiness by small amount and raise grade by medium amount
        }
        if(randNum == 5) {
            System.out.println("You decide to work overtime for a few hours.");
            //decrease grade by small amount and raise wealth and happiness by small amount
        }
        if(randNum == 6) {
            System.out.println("You get in an argument with a friend.");
            //decrease happiness by medium amount
        }
    }

    public JFrame getFrame(){
    	return gameFrame;
    }
	public boolean isPlaying() {
		return playing;
	}
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	public void exit(){
		SQLConnection con = new SQLConnection(conMap);
		if(userAcc != null){
			if(userAcc.getId() > 0){
				con.setHighscore((int)scoreTally(), userAcc.getId());
			}
		}
		gameFrame.setVisible(false);
		int save = JOptionPane.showOptionDialog(null, "Save Game?", "Save", 0, 0, null, new String[]{"Save", "Don't Save"}, 0);
		if(save == 0){
			saving = true;
		}else{
			saving = false;
		}
	}
	public void setSaving(boolean saving){
		this.saving = saving;
	}
	public boolean getSaving(){
		return saving;
	}
}

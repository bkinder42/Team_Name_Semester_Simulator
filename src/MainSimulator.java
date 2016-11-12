import java.awt.Color;
import java.awt.geom.Arc2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.soap.Text;

import controlP5.CColor;
import controlP5.ControlP5;
import controlP5.Textfield;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PSurface;

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
	private static JFrame gameFrame;
    private ControlP5 cp5;
    private float creditHours, workHours, classTime, studyTime, academicVisit, partyTime, wealthWeekly, wealthTotal,
                                happyWeekly, happySum, happyAverage, gradeWeekly, gradeSum, gradeAverage,
                                work, study, classt, aca, party, math;
    private Textfield creditHoursField, workHoursField, classTimeField, studyTimeField, academicVisitField,
            partyTimeField;
    private PFont font;
    private int fieldRow1x, fieldRow2x, textRow1x, textRow2x;
    private final float BASE_HAPPINESS = 50,
                        BASE_WEALTH = 0,
                        BASE_GRADE_POTENTIAL = 90,
                        HOURS_MAX = 160,
                        MIN_CREDITS = 12,
                        MAX_CREDITS = 24,
                        MAX_WORK = 40;
    
    public static void run(){
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
        fieldRow1x = 115;
        fieldRow2x = 500;
        textRow1x = 20;
        textRow2x = 390;
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

        cp5.addButton("submit").setSize(50, 20).setPosition(900, 700).setLabel("Submit");
        cp5.addButton("creditSelect").setSize(50,20).setPosition(330,fieldRow1x-15).setLabel("Credits");
    }

    public void draw() {
        background(128, 0, 128);
        text("Credit Hours: ", textRow1x, 115);
        text("Work Hours: ", textRow1x, 165);
        text("Class Time: ", textRow1x, 215);
        text("Study Time: ", textRow1x, 265);
        text("Academic Visits: ", textRow2x, 115);
        text("Party Time: ", textRow2x, 165);
    }

    public void weeklyReset(){
        wealthWeekly = 0;
        happyWeekly = BASE_HAPPINESS;
        gradeWeekly = BASE_GRADE_POTENTIAL;
    }

    public void matheWereDeclared(){
        happyWeekly = happyWeekly;
    }
    public void mathWereDeclaredAgain(){
        happySum += happyWeekly;
        wealthTotal += wealthWeekly;
        gradeSum += gradeWeekly;

    }
    private boolean numberCheck() {

        math = (HOURS_MAX - creditHours - work - classt - study - aca);
        work = Float.parseFloat(workHoursField.getText());
        classt = Float.parseFloat(classTimeField.getText());
        aca = Float.parseFloat(academicVisitField.getText());
        party = Float.parseFloat(partyTimeField.getText());

        if ((work >= 0 && work <= MAX_WORK) &&
                (classt >= 0 && classt <= MAX_CREDITS) &&
                (study >= 0 && study <= (2*creditHours)) &&
                (aca >= 0 && aca <= (.5*study)) &&
                (party >= 0 && party <= math)) {
            return true;
        } else return false;
    }

    public void creditSelect() {
        float cred = Float.parseFloat(creditHoursField.getText());
        if (cred >= MIN_CREDITS && cred <= MAX_CREDITS) {
            creditHours = Float.parseFloat(creditHoursField.getText());
            creditHoursField.lock();
        } else System.out.println("Pick a value between 12 and 24");
    }

    public void submit() {
        study = Float.parseFloat(studyTimeField.getText());
        if (numberCheck()) {
            workHours = Float.parseFloat(workHoursField.getText());
            workHoursField.setText("0");
            classTime = Float.parseFloat(classTimeField.getText());
            classTimeField.setText("0");
            studyTime = Float.parseFloat(studyTimeField.getText());
            studyTimeField.setText("0");
            academicVisit = Float.parseFloat(academicVisitField.getText());
            academicVisitField.setText("0");
            partyTime = Float.parseFloat(partyTimeField.getText());
            partyTimeField.setText("0");

            System.out.println("Credit Hours:\t\t" + creditHours + "\n" +
                    "Work Hours:\t\t\t" + workHours + "\n" +
                    "Class Time:\t\t\t" + classTime + "\n" +
                    "Study Time:\t\t\t" + studyTime + "\n" +
                    "Academic Visits:\t" + academicVisit + "\n" +
                    "Leisure Time:\t\t\t" + partyTime);
            mathWereDeclaredAgain();
        }
        else System.out.println("is brok");
    }

    public JFrame getFrame(){
    	return gameFrame;
    }
}

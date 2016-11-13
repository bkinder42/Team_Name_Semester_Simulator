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
                                happyWeekly, happySum, happyAverage, gradeWeekly, gradeSum, gradeAverage;

    private float work, study, classt, aca, party, math; // Temporary variables for math shenanigans

    private Textfield creditHoursField, workHoursField, classTimeField, studyTimeField, academicVisitField,
            partyTimeField;
    private PFont font;
    private int fieldRow1x, fieldRow2x, textRow1x, textRow2x, week;
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
        firstWeekStats();
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

        cp5.addButton("nextWeek").setSize(50, 20).setPosition(900, 700).setLabel("Next Week");
        cp5.addButton("weekStart").setSize(50, 20).setPosition(900, 500).setLabel("Start Week");
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
        text("Happiness: " + happyWeekly, 500, 300);
        text("Weekly Wealth: " + wealthWeekly, 500, 350);
        text("Weekly Grades: " + gradeWeekly, 500, 400);
        text("Week: " + week, 500, 450);

    }

    public void firstWeekStats(){
        wealthWeekly = 0;
        happySum = 0;
        gradeSum = 0;
        week = 1;
        happyWeekly = BASE_HAPPINESS;
        gradeWeekly = BASE_GRADE_POTENTIAL;
    }

    public boolean weekCounter(){
        if (week >= 14){
            return true;
        }else return false;

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
    public void test(){}

    public void mathForWeek(){
        float negative;
        //credits
        if(creditHours >= 12){
            happyWeekly -= 1.25f*creditHours;
            gradeWeekly -= 1.5f*creditHours;
            }
        //work
        wealthWeekly += (5*workHours);
        if(workHours >= 21){
            happyWeekly -= 1*workHours;
            gradeWeekly -= .5*workHours;
        } else happyWeekly -= .5*workHours;
        if(wealthWeekly >= 55){ happyWeekly += 5; }
        //class time
        happyWeekly -= 1*classTime;
        gradeWeekly += .5*classTime;
        //study
        gradeWeekly += .5*studyTime;
        happyWeekly -= .5*studyTime;
        //academic visit
        gradeWeekly += .5*academicVisit;
        happyWeekly -= .5*academicVisit;
        //Leisure time
        if(partyTime <= 0) {gradeWeekly -= 50;}
        gradeWeekly -= 3*partyTime;
        wealthWeekly -= 6*partyTime;
        if ((wealthWeekly - 6*partyTime) <= 0 ){
            negative = (wealthWeekly -= 7*partyTime);
            wealthTotal += negative;
            if (wealthTotal <= 0){
                wealthTotal = 0;
                System.out.println("YOU BROKE BRUH");
            }
            wealthWeekly = 0;
            happyWeekly = .25f*partyTime;
        } else happyWeekly += 3*partyTime;
        // happy check
        if (happyWeekly <= 0){ happyWeekly = 0;}




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


    //Buttons

    public void weekStart() {
        study = Float.parseFloat(studyTimeField.getText());
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
    public void creditSelect() {
        float cred = Float.parseFloat(creditHoursField.getText());
        if (cred >= MIN_CREDITS && cred <= MAX_CREDITS) {
            creditHours = Float.parseFloat(creditHoursField.getText());
            creditHoursField.lock();
        } else System.out.println("Pick a value between 12 and 24");
    }

    public void nextWeek() {
        study = Float.parseFloat(studyTimeField.getText());
        if (numberCheck()) {
            workHoursField.unlock();
            workHoursField.clear();
            classTimeField.unlock();
            classTimeField.clear();
            studyTimeField.unlock();
            studyTimeField.clear();
            academicVisitField.unlock();
            academicVisitField.clear();
            partyTimeField.unlock();
            partyTimeField.clear();

            System.out.println("Total Wealth\t\t\t" + wealthTotal + "\n" +
                    "The happiness and gp average go here");
            weeklyReset();
        }
        else System.out.println("is brok");
        if (weekCounter()){
            System.out.println("FINAL WEEK");
        }
    }

    public JFrame getFrame(){
    	return gameFrame;
    }
}

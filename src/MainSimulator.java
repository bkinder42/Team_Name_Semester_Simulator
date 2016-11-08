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
 * * Author: Ben Kinder, Andrew Knox
 * * Date Created: 11/1/16
 * * Class: COMP 101
 * * Email: bkinder1@umbc.edu
 * * Email: andrewk6@umbc.edu
 * * Description
 */

public class MainSimulator extends PApplet {
    private ControlP5 cp5;
    private float creditHours, workHours, classTime, studyTime, academicVisit, partyTime;
    private Textfield creditHoursField, workHoursField, classTimeField, studyTimeField, academicVisitField,
            partyTimeField;
    private PFont font;
    private int fieldRow1x, fieldRow2x, textRow1x, textRow2x;
    
    public static void main(String[]args){
    	//create your JFrame
        JFrame gameFrame = new JFrame("JFrame Test");
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

    public void submit() {
        creditHours = Float.parseFloat(creditHoursField.getText());
        creditHoursField.clear();
        workHours = Float.parseFloat(workHoursField.getText());
        workHoursField.clear();
        classTime = Float.parseFloat(classTimeField.getText());
        classTimeField.clear();
        studyTime = Float.parseFloat(studyTimeField.getText());
        studyTimeField.clear();
        academicVisit = Float.parseFloat(academicVisitField.getText());
        academicVisitField.clear();
        partyTime = Float.parseFloat(partyTimeField.getText());
        partyTimeField.clear();

        System.out.println("Credit Hours:\t\t" + creditHours + "\n" +
                "Work Hours:\t\t\t" + workHours + "\n" +
                "Class Time:\t\t\t" + classTime + "\n" +
                "Study Time:\t\t\t" + studyTime + "\n" +
                "Academic Visits:\t" + academicVisit + "\n" +
                "Party Time:\t\t\t" + partyTime);
    }
}

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controlP5.ControlP5;
import controlP5.Textfield;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PSurface;

/**
 * * File: MainSimulator
 * * Author: Ben Kinder
 * * Date Created: 11/1/16
 * * Class: COMP 101
 * * Email: bkinder1@umbc.edu
 * * Description
 */

public class MainSimulator extends PApplet {
    private ControlP5 cp5;
    private String creditHours = "", workHours = "";
    private Textfield creditHoursField;
    private Textfield workHoursField;
    private PFont font;
    
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
        font = createFont("arial", 16);
        background(128, 0, 128);
        cp5 = new ControlP5(this);
        fill(255);
        text("Credit Hours: ", 20, 115);
        creditHoursField = cp5.addTextfield("creditHours")
                .setPosition(100, 100)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabelVisible(false)
                .setLabel("")
                .setColor(color(0))
                .setFont(font);
        text("Work Hours: ", 20, 165);
        workHoursField = cp5.addTextfield("workHours")
                .setPosition(100, 150)
                .setSize(200, 20)
                .setColorBackground(color(255, 255, 255))
                .setLabel("")
                .setColor(color(0))
                .setFont(font);

        cp5.addButton("submit").setSize(50, 20).setPosition(900, 700).setLabel("Submit");
    }

    public void draw() {
        text(creditHours, 400, 115);
        text(workHours, 400, 165);
    }

    public void submit() {
        creditHours = creditHoursField.getText();
        creditHoursField.clear();
        workHours = workHoursField.getText();
        workHoursField.clear();
    }
}

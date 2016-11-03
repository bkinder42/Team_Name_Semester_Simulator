import controlP5.ControlP5;
import controlP5.Textfield;
import processing.core.PApplet;
import processing.core.PFont;

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

    public static void main(String[] args) {
        PApplet.main("MainSimulator");
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

    public void benMethod() {
        System.out.println("test!");
    }
}

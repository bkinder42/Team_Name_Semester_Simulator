import controlP5.*;
import processing.core.PApplet;

/**
 * * File: TestGUITextInput
 * * Author: Ben Kinder
 * * Date Created: 11/1/16
 * * Class: COMP 101
 * * Email: bkinder1@umbc.edu
 * * Description
 */
public class TestGUITextInput extends PApplet {
    ControlP5 test;
    String text = "";

    public static void main(String[] args) {
        PApplet.main("TestGUITextInput");
    }

    public void settings() {
        size(1024, 768);
    }

    public void setup() {
        background(255);
        test = new ControlP5(this);
        test.addTextfield("testField").setPosition(100, 100).setSize(200, 20);
        test.addButton("button").setSize(100, 20).setPosition(320, 100).setLabel("Return");
    }

    public void draw() {
        fill(0);
        text(text, 100, 300);
    }

    public void keyPressed() {
        if (key == ENTER) {
            text = test.get(Textfield.class, "testField").getText();
        }
    }

    public void button() {
        text = test.get(Textfield.class, "testField").getText();
        test.get(Textfield.class, "testField").clear();
    }


}

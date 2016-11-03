import processing.core.PApplet;

/**
 * * File: MainSimulator
 * * Authors: 
 * 		Ben Kinder
 * 		Andrew Knox
 * * Date Created: 11/1/16
 * * Class: COMP 101
 * * Emails: 
 * 		Ben: bkinder1@umbc.edu
 * 		Andrew: andrewk6@umbc.edu/andrew.knox112@gmail.com
 * * Description
 */

public class MainSimulator extends PApplet {
    public static void main(String[] args) {
        PApplet.main("MainSimulator");
    }

    public void settings() {
        size(1024, 768);
    }

    public void setup() {
        background(128, 0, 128);
    }

    public void draw() {
        fill(0);
        textSize(32);
        text("This is a test of Processing in Nothing", 200, 400);
        text("Eclipse > Processing So Suck It", 200, 450);
        //Testing
    }
}

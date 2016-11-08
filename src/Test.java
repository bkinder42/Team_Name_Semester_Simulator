import processing.core.PApplet;
import processing.core.PImage;

/**
 * File: Test
 * Author: Ben Kinder
 * Date Created: 11/8/16
 * Class: COMP 101
 * Email: bkinder1@umbc.edu
 * Description
 **/
public class Test extends PApplet {
    private PImage image;

    public static void main(String[] args) {
        PApplet.main("Test");
    }

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        image = requestImage("95percentHappy.png");
    }

    public void draw() {
        image(image, 0, 0);
    }
}

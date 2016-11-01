import processing.core.PApplet;

/**
 * Created by bkinder1 on 11/1/16.
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
        text("This is a test of Processing in IntelliJ", 200, 400);
    }
}

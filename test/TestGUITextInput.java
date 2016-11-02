import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;

import java.awt.*;

import javax.swing.JOptionPane;

/**
 * * File: TestGUITextInput * Author: Ben Kinder * Date Created: 11/1/16 *
 * Class: COMP 101 * Email: bkinder1@umbc.edu * Description
 */
public class TestGUITextInput extends PApplet {
	private ControlP5 test;
	private String creditHours = "", workHours = "";
	private Textfield creditHoursField;
	private Textfield workHoursField;
	private PFont font;

	public static void main(String[] args) {
		PApplet.main("TestGUITextInput");
	}

	public void settings() {
		size(1024, 768);
	}

	public void setup() {
		Thread textListener = new Thread(new Runnable() {
			private String creditHoursTemp = creditHours;

			public void run() {
				try {
					while (true) {
						if (!creditHoursTemp.equals(creditHours)) {
							int creditHoursNum = Integer.parseInt(creditHours);
						}
						println("Running");
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					println("Error");
				}
			}
		});

		font = createFont("arial", 16);
		background(128, 0, 128);
		test = new ControlP5(this);
		fill(255);
		text("Credit Hours: ", 20, 115);
		creditHoursField = test.addTextfield("creditHours").setPosition(100, 100).setSize(200, 20)
				.setColorBackground(color(255, 255, 255)).setLabelVisible(false).setLabel("").setColor(color(0))
				.setFont(font);
		text("Work Hours: ", 20, 165);
		workHoursField = test.addTextfield("workHours").setPosition(100, 150).setSize(200, 20)
				.setColorBackground(color(255, 255, 255)).setLabel("").setColor(color(0)).setFont(font);

		test.addButton("submit").setSize(50, 20).setPosition(900, 700).setLabel("Submit");
		textListener.start();
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
		JOptionPane.showMessageDialog(null, "Testing");
	}

}

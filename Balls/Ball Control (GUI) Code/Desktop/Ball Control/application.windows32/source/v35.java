import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import processing.serial.*; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class v35 extends PApplet {

/* Ball Control - Version 1.0.0
 Written by Jonathan Jamieson (unigamer@gmail.com)
 www.jonathanjamieson.com
 
 This software is unsupported. You use it at your own risk. If you find any bugs then drop me an email but I can't spend much time helping you set up. Good luck!
 
 If you use this software for performances or anything then I would love to see a video. :)
 */


// Import the required libraries
 // The GUI library used to create controls. It's free to download as is required to compile this software.
 // A serial connection is used to send commands to the microcontroller connected to the computer running Ball Control.
 // Minim is used to play songs during routines.  It's free to download as is required to compile this software.

// Load up an instance of the media player
Minim minim;
AudioPlayer player;

// Load up a serial connection to communicate with Arduino that has a NRF24 transceiver connected to
Serial myPort; 


// These are used for the routine and hold the data
XML xml;
String routine_path;


// The ball numbers that are to be used. Eventually this should be read from a parameter file. Note, 0 (zero) IS NOT a valid ball number. Additionally, because of how the network library for NRF24 works for ball numbers high than 7 (I think) all ball numbers lower than that should be connected.
int[] ball_numbers = {
  1, 2, 3
};

// These are the available effects that can be used
String[] available_effects = {
  "solid", "flash", "fade", "meteor", "meteor_random", "throw_random", "random_colours", "random_fade", "fade_impact", "sync_throw_random", "sync_random", "sync_random_fade", "two_state", "two_state_random", "disco"
};


int[][] colour_array_balls = new int[ball_numbers.length][9]; // This 2D array holds the colour information for each ball. Column 1=R1, Column 2=G1, Column 3=B1, Column 4=R2, Column 5=G2, Column 6=B2, 

int[] colour_array_shared_sync_throw_random = new int[3]; // The effect "sync_throw_random" requires a shared array for the colour information
int[] colour_array_shared_sync_random = new int[3]; // The effect "sync_random" requires a shared array for the colour information
int[][] colour_array_shared_sync_random_fade = new int[2][3]; // The effect "sync_random_random" requires a shared array for the colour information

int[] delay_balls = new int[ball_numbers.length]; // Depending on the effect this is a period of time between colour changes
int[] cycle_balls = new int[ball_numbers.length]; // Is the effect looped
int[] time_since_last_command = new int[ball_numbers.length]; // Used to record time for effects such as Meteor
String[] effect_balls = new String[ball_numbers.length]; // The current effect for each ball
int[] flag_balls = new int[ball_numbers.length]; // If a new effect is applied or a check needs to be performed a ball can be "flagged"
int[] serial_flag_balls = new int[ball_numbers.length]; // If a new serial command has been read for a given ball its value in the array is updated to =1
int[] last_event_balls = new int[ball_numbers.length];
int[] state_balls = new int[ball_numbers.length]; // Is the ball in a hand or in the air. 0=hand, 1=air.
int[] timer_balls = new int[ball_numbers.length]; // Used for timing in effects which require it
int[] time_in_air_balls = new int[ball_numbers.length]; // How long a given ball was in the air during the last through. Meteor is an example of an effect which used this.
String[] xml_sections;
int loop_xml; // 0=no loop, 1 = loop. Basically if a routine should be played on repeat.
int next_section_time; // The time when the next section of a routine should be parsed.
int next_section; // The number of the next section to be parsed
int routine_start_time; // When a routine was started
boolean xml_flag;

GDropList[] dplEffectSelect = new GDropList[ball_numbers.length]; // This is used for the droplist for the effects, it's automatically filled in.
GButton btnRoutineSelect; // Create a button for selecting the routine

GButton btnPhoto;
GButton btnVideo;


GButton btnCalibrateStartStop;
GDropList dplRoutine;

boolean dplRoutineExists = false;
boolean xml_has_time;

boolean sync_flag_throw_random = false;

int sync_timer_throw_random = 0;
int sync_timer_random = 0;
int sync_timer_random_fade = 0;

int sync_random_delay = 1000;
int sync_fade_delay = 8;


// The following GLabels & GKnobs are for the GUI
GLabel[] lblRed1  = new GLabel[ball_numbers.length];
GLabel[] lblGreen1  = new GLabel[ball_numbers.length];
GLabel[] lblBlue1  = new GLabel[ball_numbers.length];

GLabel[] lblRed2  = new GLabel[ball_numbers.length];
GLabel[] lblGreen2  = new GLabel[ball_numbers.length];
GLabel[] lblBlue2  = new GLabel[ball_numbers.length];

GKnob[] knbRed1 = new GKnob[ball_numbers.length];
GKnob[] knbGreen1 = new GKnob[ball_numbers.length];
GKnob[] knbBlue1 = new GKnob[ball_numbers.length];

GKnob[] knbRed2 = new GKnob[ball_numbers.length];
GKnob[] knbGreen2 = new GKnob[ball_numbers.length];
GKnob[] knbBlue2 = new GKnob[ball_numbers.length];

GCheckbox[] cbxRepeat = new GCheckbox[ball_numbers.length];

GCheckbox cbxXmlPlay;

GCustomSlider[] sdrDelay = new GCustomSlider[ball_numbers.length];


int[] previous_colour_at_zero_array = new int[ball_numbers.length]; // This is 

int[] r1 = new int[ball_numbers.length];
int[] g1 = new int[ball_numbers.length];
int[] b1 = new int[ball_numbers.length];

int[] kx1 = new int[ball_numbers.length];
int[] ky1 = new int[ball_numbers.length];

int[] r2 = new int[ball_numbers.length];
int[] g2 = new int[ball_numbers.length];
int[] b2 = new int[ball_numbers.length];

int[] kx2 = new int[ball_numbers.length];
int[] ky2 = new int[ball_numbers.length];

// Input serial data
int numSensors = 5;  // Number of integers to be stored in sensors[]
int sensors[]; // Array holding incoming serial data


public void setup() {
  size(1000, 650);
  xml_flag=false;

  // Prepare the serial port
  myPort = new Serial(this, Serial.list()[0], 57600); // Use the first serial number. This should be changeable in a future release.
  myPort.bufferUntil(10);
  delay(500);
  myPort.write("MEOW");
  myPort.write(10);
  delay(500);

  colour_array_shared_sync_random_fade[1][0] = 255;
  colour_array_shared_sync_random_fade[1][1] = 255;
  colour_array_shared_sync_random_fade[1][2] = 255;

  // Prepare all the balls
  for (int i = 0; i<ball_numbers.length; i++) {
    flag_balls[i] = 1;
    cycle_balls[i] = 0;
    effect_balls[i] = "solid";

    for (int j = 0; j<9; j++) {
      colour_array_balls[i][j] = 255;
    }
  }

  setup_controls(); // Prepare the GUI

  routine_path = "Default Routine.xml";
  process_routine_path();
} 



public void draw() {

  // Following stuff is graphical output =====================================================================================
  background(149, 131, 255);

  int x_centre = 300;
  int x_seperation = 200;

  // Update graphical stuff for each ball
  for (int i = 0; i < ball_numbers.length; i++) {
    textAlign(CENTER);
    textSize(20); 
    fill(255, 255, 255);
    text("Ball Number: "+str(ball_numbers[i]), x_centre+30  + i * x_seperation, 80);
    
    // Update RGB labels
    lblRed1[i].setText(str(colour_array_balls[i][0]));
    lblGreen1[i].setText(str(colour_array_balls[i][1]));
    lblBlue1[i].setText(str(colour_array_balls[i][2]));

    lblRed2[i].setText(str(colour_array_balls[i][3]));
    lblGreen2[i].setText(str(colour_array_balls[i][4]));
    lblBlue2[i].setText(str(colour_array_balls[i][5]));

    // Update the background colour of each of the balls to match their actual colour
    noStroke(); 
    fill(colour_array_balls[i][0], colour_array_balls[i][1], colour_array_balls[i][2]);
    ellipse(328  + i * 200, 238, 150, 150);

    fill(colour_array_balls[i][3], colour_array_balls[i][4], colour_array_balls[i][5]);
    ellipse(328  + i * 200, 428, 150, 150);
  }

  textAlign(CENTER);
  textSize(26); 
  fill(255, 0, 0);
  text("Ball Control", 500, 40);


  textAlign(CENTER);
  textSize(20); 
  fill(160, 6, 99);
  text("www.jonathanjamieson.com", 500, 640);
  // End of Graphical output ==========================================================================================
  

  // Move on to next section of routine if required
  if (dplRoutineExists==true && (millis() - routine_start_time)>next_section_time && cbxXmlPlay.isSelected()==true && xml_has_time == true && xml_flag==true) {
    dplRoutine.setSelected (next_section);
    xml_section_select(routine_path, next_section) ;
  }

  ball_loop(); // Call the ball loop function. Depending on the effect, a ball's colour may need to be changed etc.

}
// The following function is called when a new effect is required for a given ball. It's main purpose to change the GUI to reflect the new effect.
// For instance, the delay of a flash effect is a different range to that of a fade effect so the sliders must be updated. This function also gets the
// correct values for colour and delay from the knobs and sliders.
public void ball_effect_select(int ball_number) {

  if (millis()-time_since_last_command[ball_number] > 20) { // This conditional is used to stop commands being sent too quickly to the balls. For instance, as you drag the slider you go through a range. If the number is small then new commands will be repeatedly sent and saturate the serial and wireless connections.
    time_since_last_command[ball_number] = millis();
 

    if (effect_balls[ball_number].equals("solid")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("flash")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 50, 2000);
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("fade")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 1, 60);
      cycle_balls[ball_number] = PApplet.parseInt(cbxRepeat[ball_number].isSelected());
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("fade_impact")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 1, 60);
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("meteor")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("two_state")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
      colour_array_balls[ball_number][3] = knbRed2[ball_number].getValueI();
      colour_array_balls[ball_number][4] = knbGreen2[ball_number].getValueI();
      colour_array_balls[ball_number][5] = knbBlue2[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("two_state_random")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("meteor_random")) {
      colour_array_balls[ball_number][0] = knbRed1[ball_number].getValueI();
      colour_array_balls[ball_number][1] = knbGreen1[ball_number].getValueI();
      colour_array_balls[ball_number][2] = knbBlue1[ball_number].getValueI();
    }

    if (effect_balls[ball_number].equals("random_colours")) {
      flag_balls[ball_number] = 1;
    }
    
    if (effect_balls[ball_number].equals("disco")) {
      delay_balls[ball_number] = sdrDelay[ball_number].getValueI();
      sdrDelay[ball_number].setLimits(delay_balls[ball_number], 50, 2000);
      flag_balls[ball_number] = 1;
    }

    if (effect_balls[ball_number].equals("random_fade")) {
      flag_balls[ball_number] = 1;
    }
  }
}

public void ball_loop() {

  for (int i = 0; i<ball_numbers.length; i++) { // For each ball check for changes required

    // SOLID
    if (effect_balls[i].equals("solid") == true) {
      if (flag_balls[i] == 1) {
        int[] colours_array = new int[3];
        colours_array[0] = colour_array_balls[i][0];
        colours_array[1] = colour_array_balls[i][1];
        colours_array[2] = colour_array_balls[i][2];
        solid_effect(colours_array, ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // FLASH
    if (effect_balls[i].equals("flash") == true) {
      if (flag_balls[i] == 1) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flash_effect(colours_array, delay_balls[i], ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // DISCO
    if (effect_balls[i].equals("disco") == true) {
      if (flag_balls[i] == 1) {
        disco_effect(delay_balls[i], ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // FADE
    if (effect_balls[i].equals("fade") == true) {
      if (flag_balls[i] == 1) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flag_balls[i] = 0;
        fade_effect(colours_array, delay_balls[i], cycle_balls[i], ball_numbers[i]);
      }
    }

    // FADE IMPACT
    if (effect_balls[i].equals("fade_impact") == true) { // CURRENTLY NOT WORKING ONTHE BALLS!!!
      if (flag_balls[i] == 1) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flag_balls[i] = 0;
        fade_impact_effect(colours_array, delay_balls[i], ball_numbers[i]);
      }
    }
    
    // METEOR
    if (effect_balls[i].equals("meteor") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught   
          println("ball caught " + i);
          time_in_air_balls[i] = millis()-last_event_balls[i];
          println("Time in air" + time_in_air_balls[i] );
        }
        if (state_balls[i] == 1) { // ball has just been thrown
          println("ball thrown " + i);
          timer_balls[i] = millis() + time_in_air_balls[i]/2 -100;
          flag_balls[i] = 1;
          last_event_balls[i] = millis();
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];
          solid_effect(colours_array, ball_numbers[i]);
        }
      }
      if ((millis()>timer_balls[i]) && (flag_balls[i] == 1)) {
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        fade_effect(colours_array, (time_in_air_balls[i]/2)/255, 0, ball_numbers[i]);
        flag_balls[i] = 0;
      }
    }

    // METEOR RANDOM
    if (effect_balls[i].equals("meteor_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught
          println("ball caught " + i);
          time_in_air_balls[i] = millis()-last_event_balls[i];
          println("Time in air" + time_in_air_balls[i] );
        }
        if (state_balls[i] == 1) { // ball has just been thrown
          println("ball thrown " + i);
          timer_balls[i] = millis() + time_in_air_balls[i]/2 -100;
          flag_balls[i] = 1;
          last_event_balls[i] = millis();
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];

          solid_effect(colours_array, ball_numbers[i]);
        }
      }

      if ((millis()>timer_balls[i]) && (flag_balls[i] == 1)) {


        int[][] colours_array = new int[2][3];

        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];

        int[] random_colours = randomColourArrayV2();

        colours_array[1][0] = random_colours[0];
        colours_array[1][1] = random_colours[1];
        colours_array[1][2] = random_colours[2];

        colour_array_balls[i][3] = random_colours[0];
        colour_array_balls[i][4] = random_colours[1];
        colour_array_balls[i][5] = random_colours[2];

        fade_effect(colours_array, (time_in_air_balls[i]/2)/255, 0, ball_numbers[i]);

        flag_balls[i] = 0;
      }
    }

    // TWO STATE
    if (effect_balls[i].equals("two_state") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];
          solid_effect(colours_array, ball_numbers[i]);
        } 
        else if (state_balls[i] == 1) { // ball has just been thrown
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][3];
          colours_array[1] = colour_array_balls[i][4];
          colours_array[2] = colour_array_balls[i][5];
          solid_effect(colours_array, ball_numbers[i]);
        }
      }
    }

    // TWO STATE RANDOM
    if (effect_balls[i].equals("two_state_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 0) { // ball has just been caught
          int[] colours_array = new int[3];
          colours_array[0] = colour_array_balls[i][0];
          colours_array[1] = colour_array_balls[i][1];
          colours_array[2] = colour_array_balls[i][2];
          solid_effect(colours_array, ball_numbers[i]);
        } 
        else if (state_balls[i] == 1) { // ball has just been thrown
          if (millis()>sync_timer_throw_random) {
            sync_flag_throw_random = false;
          }
          if (state_balls[i] == 1) { // ball has just been thrown
            if (sync_flag_throw_random==true) {
              colour_array_balls[i][3] = colour_array_shared_sync_throw_random[0];
              colour_array_balls[i][4] =  colour_array_shared_sync_throw_random[1];
              colour_array_balls[i][5] =  colour_array_shared_sync_throw_random[2];
              int[] random_colours = new int[3];
              random_colours[0] = colour_array_shared_sync_throw_random[0];
              random_colours[1] = colour_array_shared_sync_throw_random[1];
              random_colours[2] = colour_array_shared_sync_throw_random[2];
              solid_effect(random_colours, ball_numbers[i]);
            } 
            else {
              int[] random_colours = randomColourArrayV2();
              colour_array_shared_sync_throw_random[0] = random_colours[0];
              colour_array_shared_sync_throw_random[1] = random_colours[1];
              colour_array_shared_sync_throw_random[2] = random_colours[2];
              colour_array_balls[i][3] = random_colours[0];
              colour_array_balls[i][4] =  random_colours[1];
              colour_array_balls[i][5] =  random_colours[2];
              solid_effect(random_colours, ball_numbers[i]);
              sync_timer_throw_random = millis() + 200;
              sync_flag_throw_random = true;
            }
          }
        }
      }
    }

    // THROW RANDOM
    if (effect_balls[i].equals("throw_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (state_balls[i] == 1) { // ball has just been thrown
          int[] random_colours = randomColourArrayV2_no_repeat(i);
          colour_array_balls[i][0] = random_colours[0];
          colour_array_balls[i][1] =  random_colours[1];
          colour_array_balls[i][2] =  random_colours[2];
          solid_effect(random_colours, ball_numbers[i]);
        }
      }
    }



    if (effect_balls[i].equals("sync_throw_random") == true) {
      if (serial_flag_balls[i] == 1) {
        serial_flag_balls[i] = 0;
        if (millis()>sync_timer_throw_random) {
          sync_flag_throw_random = false;
        }
        if (state_balls[i] == 1) { // ball has just been thrown
          if (sync_flag_throw_random==true) {
            colour_array_balls[i][0] = colour_array_shared_sync_throw_random[0];
            colour_array_balls[i][1] =  colour_array_shared_sync_throw_random[1];
            colour_array_balls[i][2] =  colour_array_shared_sync_throw_random[2];
            int[] random_colours = new int[3];
            random_colours[0] = colour_array_shared_sync_throw_random[0];
            random_colours[1] = colour_array_shared_sync_throw_random[1];
            random_colours[2] = colour_array_shared_sync_throw_random[2];
            solid_effect(random_colours, ball_numbers[i]);
          } 
          else {
            int[] random_colours = randomColourArrayV2();
            colour_array_shared_sync_throw_random[0] = random_colours[0];
            colour_array_shared_sync_throw_random[1] = random_colours[1];
            colour_array_shared_sync_throw_random[2] = random_colours[2];
            colour_array_balls[i][0] = random_colours[0];
            colour_array_balls[i][1] =  random_colours[1];
            colour_array_balls[i][2] =  random_colours[2];
            solid_effect(random_colours, ball_numbers[i]);
            sync_timer_throw_random = millis() + 200;
            sync_flag_throw_random = true;
          }
        }
      }
    }


    // SYNC RANDOM
    if (effect_balls[i].equals("sync_random") == true) {
      if (millis()>sync_timer_random) {
        int[] random_colours = randomColourArrayV2();
        colour_array_shared_sync_random[0] = random_colours[0];
        colour_array_shared_sync_random[1] = random_colours[1];
        colour_array_shared_sync_random[2] = random_colours[2];
        colour_array_balls[i][0] = random_colours[0];
        colour_array_balls[i][1] =  random_colours[1];
        colour_array_balls[i][2] =  random_colours[2];
        sync_timer_random = millis() + sync_random_delay;
        solid_effect(random_colours, ball_numbers[i]);
      } 
      else if (colour_array_balls[i][0]!=colour_array_shared_sync_random[0] || colour_array_balls[i][1]!=colour_array_shared_sync_random[1]  || colour_array_balls[i][2]!=colour_array_shared_sync_random[2]  ) {
        colour_array_balls[i][0] = colour_array_shared_sync_random[0];
        colour_array_balls[i][1] =  colour_array_shared_sync_random[1];
        colour_array_balls[i][2] =  colour_array_shared_sync_random[2];
        int[] random_colours = new int[3];
        random_colours[0] = colour_array_shared_sync_random[0];
        random_colours[1] = colour_array_shared_sync_random[1];
        random_colours[2] = colour_array_shared_sync_random[2];
        solid_effect(random_colours, ball_numbers[i]);
      }
    }


    if (effect_balls[i].equals("sync_random_fade") == true) {
      if (millis()>sync_timer_random_fade) {
        int[] random_colours = randomColourArrayV2();
        //  int[] colour_array_shared_random_fade = new int[2][3]
        if (colour_array_shared_sync_random_fade[1][0]==0 && colour_array_shared_sync_random_fade[1][1]==0 && colour_array_shared_sync_random_fade[1][2]==0) {
          colour_array_shared_sync_random_fade[1][0] = colour_array_balls[i][0];
          colour_array_shared_sync_random_fade[1][1] = colour_array_balls[i][1]  ;
          colour_array_shared_sync_random_fade[1][2] = colour_array_balls[i][2] ;
        }
        colour_array_shared_sync_random_fade[0][0] = colour_array_shared_sync_random_fade[1][0];
        colour_array_shared_sync_random_fade[0][1] = colour_array_shared_sync_random_fade[1][1];
        colour_array_shared_sync_random_fade[0][2] = colour_array_shared_sync_random_fade[1][2];
        if (colour_array_balls[i][3]==255 && colour_array_balls[i][4]==255 && colour_array_balls[i][5]==255 ) { /// THIS IS A HACK FOR STYLO ROUTINE
          colour_array_shared_sync_random_fade[0][0] = colour_array_balls[i][3];
          colour_array_shared_sync_random_fade[0][1] = colour_array_balls[i][4];
          colour_array_shared_sync_random_fade[0][2] = colour_array_balls[i][5];
        }

        colour_array_shared_sync_random_fade[1][0] = random_colours[0];
        colour_array_shared_sync_random_fade[1][1] = random_colours[1];
        colour_array_shared_sync_random_fade[1][2] = random_colours[2];
        colour_array_balls[i][0] = colour_array_shared_sync_random_fade[0][0];
        colour_array_balls[i][1] =  colour_array_shared_sync_random_fade[0][1];
        colour_array_balls[i][2] =  colour_array_shared_sync_random_fade[0][2];
        colour_array_balls[i][3] = colour_array_shared_sync_random_fade[1][0];
        colour_array_balls[i][4] =  colour_array_shared_sync_random_fade[1][01];
        colour_array_balls[i][5] =  colour_array_shared_sync_random_fade[1][2];
        sync_timer_random_fade = millis() + 255*sync_fade_delay+400;
        fade_effect(colour_array_shared_sync_random_fade, sync_fade_delay, 0, ball_numbers[i]);
      } 
      else if (colour_array_balls[i][0]!=colour_array_shared_sync_random_fade[0][0] || colour_array_balls[i][1]!=colour_array_shared_sync_random_fade[0][1]  || colour_array_balls[i][2]!=colour_array_shared_sync_random_fade[0][2] || colour_array_balls[i][3]!=colour_array_shared_sync_random_fade[1][0] || colour_array_balls[i][4]!=colour_array_shared_sync_random_fade[1][1]  || colour_array_balls[i][5]!=colour_array_shared_sync_random_fade[1][2]  ) {
        colour_array_balls[i][0] = colour_array_shared_sync_random_fade[0][0];
        colour_array_balls[i][1] =  colour_array_shared_sync_random_fade[0][1];
        colour_array_balls[i][2] =  colour_array_shared_sync_random_fade[0][2];
        colour_array_balls[i][3] = colour_array_shared_sync_random_fade[1][0];
        colour_array_balls[i][4] = colour_array_shared_sync_random_fade[1][1];
        colour_array_balls[i][5] =  colour_array_shared_sync_random_fade[1][2];
        fade_effect(colour_array_shared_sync_random_fade, sync_fade_delay, 0, ball_numbers[i]);
      }
    }

    // RANDOM COLOURS
    if (effect_balls[i].equals("random_colours") == true) {
      if (flag_balls[i] == 1) {
        timer_balls[i] = millis() + sdrDelay[i].getValueI();
        flag_balls[i] = 0;
      }
      if ((millis()>timer_balls[i])) {
        int[] random_colours = randomColourArrayV2();
        colour_array_balls[i][0] = random_colours[0];
        colour_array_balls[i][1] =  random_colours[1];
        colour_array_balls[i][2] =  random_colours[2];
        solid_effect(random_colours, ball_numbers[i]);
        timer_balls[i] = millis() + sdrDelay[i].getValueI();
      }
    }
    
    // RANDOM FADE
    if (effect_balls[i].equals("random_fade") == true) {
      if (flag_balls[i] == 1) {
        timer_balls[i] = millis() + 255*sdrDelay[i].getValueI();
        flag_balls[i] = 0;
      }
      if ((millis()>timer_balls[i])) {
        int[] random_colours1 = randomColourArrayV2();
        colour_array_balls[i][0] = colour_array_balls[i][3] ;
        colour_array_balls[i][1] =  colour_array_balls[i][4] ;
        colour_array_balls[i][2] =  colour_array_balls[i][5] ;
        colour_array_balls[i][3] = random_colours1[0];
        colour_array_balls[i][4] =  random_colours1[1];
        colour_array_balls[i][5] =  random_colours1[2];
        int[][] colours_array = new int[2][3];
        colours_array[0][0] = colour_array_balls[i][0];
        colours_array[0][1] = colour_array_balls[i][1];
        colours_array[0][2] = colour_array_balls[i][2];
        colours_array[1][0] = colour_array_balls[i][3];
        colours_array[1][1] = colour_array_balls[i][4];
        colours_array[1][2] = colour_array_balls[i][5];
        flag_balls[i] = 0;
        fade_effect(colours_array, sdrDelay[i].getValueI(), 0, ball_numbers[i]);
        timer_balls[i] = millis() + 255*sdrDelay[i].getValueI()+100;
      }
    }
  }
}

// These are the base effects that are actually programmed into the juggling ball. For instance random fade
// just sends a fade command from a specified colour to another. The random colour is not selected onboard the
// juggling ball. This is slowly changing as I add more "smart" functionality" onto the balls. This means less
// processing needs to be done on the main computer. Eventually the central Arduino (or embedded computer)
// could potentially handle all the smart stuff and just host an interface for the user to connect to.
// --------------------------------------------------------------------------------------------------------------

public void solid_effect(int[] colours_array, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   
  print("Ball Number: ");
  print(ball_number);
  println(""); 
  println("Effect type: Solid");
  print("Red: ");
  print(colours_array[0]);
  print("\t Green: ");
  print(colours_array[1]);
  print("\t Blue: ");
  print(colours_array[2]);
  println("");

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 1; // Type of command juggling balll should follow
  payload_data[2] = colours_array[0];
  payload_data[3] = colours_array[1];
  payload_data[4] = colours_array[2];
  payload_data[5] = 0;
  payload_data[6] = 0;
  payload_data[7] = 0;
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = 0;

  send_payload(payload_data);
}


public void flash_effect(int[][] colours_array, int delay, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Flash");
  print("Delay: ");
  print(delay);
  println("");

  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 5; 
  payload_data[2] = colours_array[0][0];
  payload_data[3] = colours_array[0][1];
  payload_data[4] = colours_array[0][2];
  payload_data[5] = colours_array[1][0];
  payload_data[6] = colours_array[1][1];
  payload_data[7] = colours_array[1][2];
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = delay;

  send_payload(payload_data);
}


public void disco_effect(int delay, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Disco");
  print("Delay: ");
  print(delay);
  println("");

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 8; 
  payload_data[2] = 0;
  payload_data[3] = 0;
  payload_data[4] = 0;
  payload_data[5] = 0;
  payload_data[6] = 0;
  payload_data[7] = 0;
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = delay;

  send_payload(payload_data);
}

public void fade_effect(int[][] colours_array, int delay, int cycle, int ball_number) {

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  int effect_case;
  String cycle_string;

  if (cycle==1) { // Cycle means go from one colour to the other and back again until a new command is received
    effect_case = 4;
    cycle_string = "True";
  } 
  else {

    effect_case = 2;
    cycle_string = "False";
  }

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Fade Effect");
  print("Delay: ");
  print(delay);
  println("");
  print("Cycle: ");
  print(cycle_string);
  println("");

  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = effect_case; // Type of command juggling balll should follow
  payload_data[2] = colours_array[0][0];
  payload_data[3] = colours_array[0][1];
  payload_data[4] = colours_array[0][2];
  payload_data[5] = colours_array[1][0];
  payload_data[6] = colours_array[1][1];
  payload_data[7] = colours_array[1][2];
  payload_data[8] = 1;
  payload_data[9] = delay;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = 0;


  send_payload(payload_data);
}


public void fade_impact_effect(int[][] colours_array, int delay, int ball_number) { // CURRENTLY NOT IMPLEMENTED ON THE BALLS!!

  updateKnobs( findIndexofArray(ball_number, ball_numbers));   

  int decrement = 10;
  int effect_impact = 30; // magnitude of change required to trigger a colour change

  print("Ball Number: ");
  print(ball_number);
  println("");
  println("Effect type: Fade Impact Effect");
  print("Delay between each fade step (ms): ");
  print(delay);
  println("");

  print("Red1: ");
  print(colours_array[0][0]);
  print("\t Green1: ");
  print(colours_array[0][1]);
  print("\t Blue1: ");
  print(colours_array[0][2]);
  println("");

  print("Red2: ");
  print(colours_array[1][0]);
  print("\t Green2: ");
  print(colours_array[1][1]);
  print("\t Blue2: ");
  print(colours_array[1][2]);
  println("");

  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 3; // Type of command juggling balll should follow
  payload_data[2] = colours_array[0][0];
  payload_data[3] = colours_array[0][1];
  payload_data[4] = colours_array[0][2];
  payload_data[5] = colours_array[1][0];
  payload_data[6] = colours_array[1][1];
  payload_data[7] = colours_array[1][2];
  payload_data[8] = 1;
  payload_data[9] = delay;
  payload_data[10] = effect_impact;
  payload_data[11] = 0;
  payload_data[12] = 0;

  send_payload(payload_data);
}

// The following functions are used to handle GUI events.

public void handleDropListEvents(GDropList list, GEvent event)
{
  if (list.tag.equals("dpl_effect_select")) {
    String selected_effect =  available_effects[list.getSelectedIndex()];
    effect_balls[list.tagNo] = selected_effect;
    ball_effect_select(list.tagNo);
  }
  
  if (list == dplRoutine)
  {
    if  ((cbxXmlPlay.isSelected() == true) && (xml_has_time==true)) {
      xml_flag=true;
      next_section = dplRoutine.getSelectedIndex();     
      XML[] section_children = xml.getChildren("section");   
      next_section_time = PApplet.parseInt(section_children[next_section].getString("time"));
      routine_start_time = millis()-next_section_time ;
      //   xml_section_select(routine_path, next_section);
    } 
    else {
      xml_flag=true;
      xml_section_select(routine_path, dplRoutine.getSelectedIndex());
    }
  }
  
}

public void handleButtonEvents(GButton button, GEvent event) {
  if ( event == GEvent.CLICKED) {
    if (button.tag.equals("RoutineSelect") == true) {
      selectInput("Select a Routine (.xml)", "fileSelected");
    }
    if (button.tag.equals("Photo") == true) {
      photo();
    }
    if (button.tag.equals("Video") == true) {
      video();
    }
  }
}


public void handleKnobEvents(GValueControl knob, GEvent event) {
  ball_effect_select(knob.tagNo);
}

public void handleSliderEvents(GValueControl slider, GEvent event) {
  ball_effect_select(slider.tagNo);
}

public void handleToggleControlEvents(GToggleControl option, GEvent event) {
  ball_effect_select(option.tagNo);
  if (option.tag.equals("XmlPlay")) {
    if  ((cbxXmlPlay.isSelected() == true) && (xml_has_time==true)) {
      xml_flag=true;
      next_section = dplRoutine.getSelectedIndex();     
      XML[] section_children = xml.getChildren("section");   
      next_section_time = PApplet.parseInt(section_children[next_section].getString("time"));
      routine_start_time = millis()-next_section_time ;
    }
  } 
}


public void fileSelected(File selection)
{

  delay(5000);
  routine_path = (selection.getAbsolutePath());
  xml_flag = false;
  process_routine_path();
}

// Good luck understanding this one!
public void process_routine_path() {
  xml = loadXML(routine_path);
  setup_routine_buttons();
  XML[] section_children = xml.getChildren("section");
  XML routine = section_children[0].getParent();
  int num_balls = routine.getInt("number_balls");
  btnRoutineSelect.setText(routine.getString("name"));  
  if (routine.hasAttribute("loop")==true) {
    String xml_loop_string = routine.getString("loop");
    if ((xml_loop_string.equals("true"))== true) {
      loop_xml = 1;
      println("Loop Routine: True");
    } 
    else {
      loop_xml = 0;
      println("Loop Routine: False");
    }
  } 
  else {
    println("Loop Routine: False");
    loop_xml = 0;
  }
  if (section_children[0].hasAttribute("time")==true) {
    xml_has_time = true;
  } 
  else {
    xml_has_time = false;
  }
  if (routine.hasAttribute("music")==true && cbxXmlPlay.isSelected()==true) {
    String music_path = routine.getString("music");
    minim = new Minim(this);
    player = minim.loadFile(music_path);
    player.play();
  } 
  else {
    //  player.pause();
  }
  xml_flag=true;
  routine_start_time = millis();
  next_section = 0;
  xml_section_select(routine_path, 0); // load first section
}

// The following functions are called when GUI elements need to be created

public void setup_controls() { // This is the main function for creating GUI elements add is called once at startup.

  int x_centre = 300;
  int x_seperation = 200;

  for (int i = 0; i < ball_numbers.length; i++) {
    r1[i] = colour_array_balls[i][0];
    g1[i] = colour_array_balls[i][1];
    b1[i] = colour_array_balls[i][2];
    kx1[i] = x_centre-50  + i * x_seperation;
    ky1[i] = 160;

    kx2[i] = x_centre-50  + i * x_seperation;
    ky2[i] = 350;

    knbRed1[i] = new GKnob(this, kx1[i], ky1[i], 150, 150, 0.8f);
    knbRed1[i].setTurnRange(150, 270);
    knbRed1[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbRed1[i].setArcPolicy(true, true, true);
    knbRed1[i].setLimits(r1[i], 0, 255);
    knbRed1[i].setNbrTicks(9);
    knbRed1[i].setLocalColorScheme(G4P.RED_SCHEME); 
    knbRed1[i].tagNo = i;  
    knbRed1[i].tag = "red1";

    knbGreen1[i] = new GKnob(this, kx1[i] + 8, ky1[i], 150, 150, 0.8f);
    knbGreen1[i].setTurnRange(270, 30);
    knbGreen1[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbGreen1[i].setArcPolicy(true, true, true);
    knbGreen1[i].setLimits(g1[i], 0, 255);
    knbGreen1[i].setNbrTicks(9);
    knbGreen1[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    knbGreen1[i].tagNo = i;  
    knbGreen1[i].tag = "green1"; 

    knbBlue1[i] = new GKnob(this, kx1[i] + 4, ky1[i] + 9, 150, 150, 0.8f);
    knbBlue1[i].setTurnRange(30, 150);
    knbBlue1[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbBlue1[i].setArcPolicy(true, true, true);
    knbBlue1[i].setLimits(b1[i], 0, 255);
    knbBlue1[i].setNbrTicks(9);
    knbBlue1[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    knbBlue1[i].tagNo = i;
    knbBlue1[i].tag = "blue1";  


    knbRed2[i] = new GKnob(this, kx2[i], ky2[i], 150, 150, 0.8f);
    knbRed2[i].setTurnRange(150, 270);
    knbRed2[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbRed2[i].setArcPolicy(true, true, true);
    knbRed2[i].setLimits(r2[i], 0, 255);
    knbRed2[i].setNbrTicks(9);
    knbRed2[i].setLocalColorScheme(G4P.RED_SCHEME); 
    knbRed2[i].tagNo = i;  
    knbRed2[i].tag = "red2";

    knbGreen2[i] = new GKnob(this, kx2[i] + 8, ky2[i], 150, 150, 0.8f);
    knbGreen2[i].setTurnRange(270, 30);
    knbGreen2[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbGreen2[i].setArcPolicy(true, true, true);
    knbGreen2[i].setLimits(g2[i], 0, 255);
    knbGreen2[i].setNbrTicks(9);
    knbGreen2[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    knbGreen2[i].tagNo = i;  
    knbGreen2[i].tag = "green2"; 

    knbBlue2[i] = new GKnob(this, kx2[i] + 4, ky2[i] + 9, 150, 150, 0.8f);
    knbBlue2[i].setTurnRange(30, 150);
    knbBlue2[i].setTurnMode(G4P.CTRL_ANGULAR);
    knbBlue2[i].setArcPolicy(true, true, true);
    knbBlue2[i].setLimits(b2[i], 0, 255);
    knbBlue2[i].setNbrTicks(9);
    knbBlue2[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    knbBlue2[i].tagNo = i;
    knbBlue2[i].tag = "blue2";  

    stroke(0);
    strokeWeight(3);
    rectMode(CORNERS);
  }   

  for (int i = 0; i < lblRed1.length; i++) {
    lblRed1[i] = new GLabel(this, x_centre-50  + i * x_seperation, 135, 60, 18, "Red");
    lblRed1[i].tag = "red1";
    lblRed1[i].setLocalColorScheme(G4P.RED_SCHEME);
    lblRed1[i].tagNo = i;
    lblRed1[i].setText("red1");
  } 


  for (int i = 0; i < lblGreen1.length; i++) {
    lblGreen1[i] = new GLabel(this, x_centre  + i * x_seperation, 135, 60, 18, "Green");
    lblGreen1[i].tag = "green1";
    lblGreen1[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    lblGreen1[i].tagNo = i;
    lblGreen1[i].setText("red1");
  } 


  for (int i = 0; i < lblBlue1.length; i++) {
    lblBlue1[i] = new GLabel(this, x_centre+50  + i * x_seperation, 135, 60, 18, "Blue");
    lblBlue1[i].tag = "blue1";
    lblBlue1[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblBlue1[i].tagNo = i;
    lblBlue1[i].setText("blue1");
  } 



  for (int i = 0; i < lblRed2.length; i++) {
    lblRed2[i] = new GLabel(this, x_centre-50  + i * x_seperation, 330, 60, 18, "Red");
    lblRed2[i].tag = "red2";
    lblRed2[i].setLocalColorScheme(G4P.RED_SCHEME);
    lblRed2[i].tagNo = i;
    lblRed2[i].setText("red2");
  } 


  for (int i = 0; i < lblGreen2.length; i++) {
    lblGreen2[i] = new GLabel(this, x_centre  + i * x_seperation, 330, 60, 18, "Green");
    lblGreen2[i].tag = "green2";
    lblGreen2[i].setLocalColorScheme(G4P.GREEN_SCHEME);
    lblGreen2[i].tagNo = i;
    lblGreen2[i].setText("red2");
  } 


  for (int i = 0; i < lblBlue2.length; i++) {
    lblBlue2[i] = new GLabel(this, x_centre+50  + i * x_seperation, 330, 60, 18, "Blue");
    lblBlue2[i].tag = "blue2";
    lblBlue2[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblBlue2[i].tagNo = i;
    lblBlue2[i].setText("blue2");
  } 

  for (int i = 0; i < dplEffectSelect.length; i++) {
    dplEffectSelect[i] = new GDropList(this, x_centre-40  + i * x_seperation, 90, 150, 100, 6);
    dplEffectSelect[i].tag = "dpl_effect_select";
    dplEffectSelect[i].setItems(available_effects, 0); 
    dplEffectSelect[i].tagNo = i;
    // dplEffectSelect[i].expanded(true);
    //   btnSolid[i].setEnabled(false);
  } 

  btnPhoto  =  new GButton(this, 25, 15, 200, 18, "Photo");
  btnPhoto.tag = "Photo";

  btnVideo  =  new GButton(this, 25, 50, 200, 18, "Video");
  btnVideo.tag = "Video";



  btnRoutineSelect  =  new GButton(this, 25, 90, 200, 18, "No Routine Selected");
  btnRoutineSelect.tag = "RoutineSelect";


  cbxXmlPlay=  new GCheckbox(this, 25, 120, 100, 20, "Play Routine");
  cbxXmlPlay.tag = "XmlPlay";

  cbxXmlPlay.setSelected(false);

  for (int i = 0; i < sdrDelay.length; i++) {
    sdrDelay[i] = new GCustomSlider(this, x_centre-50  + i * x_seperation, 530, 150, 50, "Delay"); 
    sdrDelay[i].tag = "Delay";
    sdrDelay[i].setShowDecor(false, true, true, true);
    sdrDelay[i].setNbrTicks(5);
    sdrDelay[i].tagNo = i;
    sdrDelay[i].setLimits(40, 5, 2000);
  }


  for (int i = 0; i < cbxRepeat.length; i++) {
    cbxRepeat[i] =  new GCheckbox(this, x_centre-25  + i * x_seperation, 590, 100, 20, "Repeat");
    cbxRepeat[i].tag = "Repeat";
    cbxRepeat[i].tagNo = i;
    cbxRepeat[i].setSelected(true);
  }
}

public void setup_routine_buttons()
{
  XML[] section_children = xml.getChildren("section");
  String[] xml_sections = new String[section_children.length];
  for (int j = 0; j<section_children.length; j++) {
    String section_name = section_children[j].getString("name"); // [0] because only one effect per ball
    xml_sections[j] = section_name;
  }
 
  if (dplRoutineExists == false) {
    dplRoutine = new GDropList(this, 25, 150, 200, 500, 20);

    dplRoutineExists = true;
  }
  dplRoutine.setItems(xml_sections, 0); 
  dplRoutine.tag = "section_select";
}

public void serialEvent(Serial myPort) { // Currently this function is used to change the state_balls array to reflect whether or not a ball is in the air or not.
  String myString = myPort.readStringUntil(10); // This function is a bit of a mess and needs to be tidied up. It was written with the intention to be flexible to different kinds of serial events, not just ball changes.
  if (myString != null) {
    myString = trim(myString);
    sensors = PApplet.parseInt(split(myString, ','));
    int ball_index = findIndexofArray(sensors[1], ball_numbers);
      if (sensors[1] > 0) {
        if (sensors[0] == 1) {
          serial_flag_balls[ball_index] = 1;
          if (sensors[3]==1) { // throw so ball in air
            state_balls[ball_index] = 1; // air
          } 
          else {
            state_balls[ball_index] = 0; // hand
          }
        }
      }
  }
}

public void send_payload(int[] payload_data) {
  // case type = 1 for effect commands
  // payload_data - 0:p1 (case type), 1:ball number (not actually sent to ball),  2:p2 , 3:p3, 4:p4 ... 12:p12
  myPort.write("1,"+payload_data[0]+","+payload_data[1]+","+payload_data[2]+","+payload_data[3]+","+payload_data[4]+","+payload_data[5]+","+payload_data[6]+","+payload_data[7]+","+payload_data[8]+","+payload_data[9]+","+payload_data[10]+","+payload_data[11]+","+payload_data[12]); 
  myPort.write(10);
  delay(5);
}

public void photo() {
  // case type = 2 for photo and video commands
  myPort.write("2,1,0,0,0,0,0,0,0,0,0,0,0,0");
  myPort.write(10);
  delay(5);
}

public void video() {
  myPort.write("2,2,0,0,0,0,0,0,0,0,0,0,0,0");
  myPort.write(10);
  delay(5);
}

public int[]  randomColourArray() {
  int temp_flag = 1;
  int[] random_colour_array = new int[3];
  while (temp_flag == 1) {
    for (int j = 0; j < 3; j++) {
      random_colour_array[j] = PApplet.parseInt(random(0, 255));
    }
    if (random_colour_array[0]+random_colour_array[1]+random_colour_array[2]<100 || random_colour_array[0]+random_colour_array[1]+random_colour_array[2]>400) {
      println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  return random_colour_array;
}



public int[]  randomColourArrayV2() {
  int colour_at_zero = PApplet.parseInt(random(0, 3));
  int temp_flag = 1;
  int[] random_colour_array = new int[3];
  while (temp_flag == 1) {
    for (int j = 0; j < 3; j++) {
      if (j==colour_at_zero) {
        random_colour_array[j]=0;
      } 
      else {
        random_colour_array[j] = PApplet.parseInt(random(0, 255));
      }
    }
    if (random_colour_array[0]+random_colour_array[1]+random_colour_array[2]<100 || random_colour_array[0]+random_colour_array[1]+random_colour_array[2]>700) {
      println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  return random_colour_array;
}


public int[]  randomColourArrayV2_no_repeat(int ball_number_zero) {
  int temp_flag = 1;
  int colour_at_zero = 0;
  while (temp_flag == 1) {  
    colour_at_zero = PApplet.parseInt(random(0, 3));

    if (colour_at_zero!=previous_colour_at_zero_array[ball_number_zero]) {

      previous_colour_at_zero_array[ball_number_zero] = colour_at_zero;

      temp_flag = 0;
    }
  }
  temp_flag = 1;

  int[] random_colour_array = new int[3];
  while (temp_flag == 1) {
    for (int j = 0; j < 3; j++) {
      if (j==colour_at_zero) {
        random_colour_array[j]=0;
      } 
      else {
        random_colour_array[j] = PApplet.parseInt(random(0, 255));
      }
    }
    if (random_colour_array[0]+random_colour_array[1]+random_colour_array[2]<100 || random_colour_array[0]+random_colour_array[1]+random_colour_array[2]>700) {
      println("minimum brightness not reached, new combination attempt");
      // i=i-1;
    } 
    else {
      temp_flag = 0;
    }
  }
  return random_colour_array;
}

// The main use of this function is in parsing xml files. If -1 is used then the previous value should be used for the new command. 
public int checkIfprevious(int supplied_value, int current_value) {
  if (supplied_value<0) { // Obviously <0 covers more numbers than just -1 but I decided to cover a wider a range!!
    return current_value;
  }  
  else {
    return supplied_value;
  }
}

// Find the index of an array. Eg ball_numbers=[1,2,3]. Ball number 1 has an index 0 (it's the first one in the array). Ball 2 has an index of 1.
public int findIndexofArray(int value, int[] the_array) {
  int requiredIndex = -1;
  for (int i = 0; i < the_array.length; i++) {
    if (the_array[i] == value)
      requiredIndex = i;
  }
  return requiredIndex;
}


public int findIndexofStringArray(String value, String[] the_array) {
  int requiredIndex = -1;
  for (int i = 0; i < the_array.length; i++) {
    if  (value.equals(the_array[i]) == true) {
      requiredIndex = i;
    }
  }
  return requiredIndex;
}


public void updateKnobs(int i) {
  knbRed1[i].setLimits(colour_array_balls[i][0], 0, 255);
  knbGreen1[i].setLimits(colour_array_balls[i][1], 0, 255);
  knbBlue1[i].setLimits(colour_array_balls[i][2], 0, 255);
  knbRed2[i].setLimits(colour_array_balls[i][3], 0, 255);
  knbGreen2[i].setLimits(colour_array_balls[i][4], 0, 255);
  knbBlue2[i].setLimits(colour_array_balls[i][5], 0, 255);
}

// XML Parsing Stuff
/* This stuff is a bit hard to follow. It's used to read an xml file describing the effects of the balls. This can change over time or be static requiring
the user to change the effects. To understand the format of the xml files it's best to look at example ones but the basic idea is
<routine><section><ball><effect></effect></ball></section></routine>.

xml_section_select() is called up when a new section is required to parsed. It then passes off each ball effect to select_effect_xml().

*/
public void xml_section_select(String xml_name, int section_number) {
  // print("Song Time: ");
  //  print(player.time);
  //  println("");
  print("Routine Time: ");
  print(millis()-routine_start_time);
  println("");
  XML[] section_children = xml.getChildren("section");
  XML[] section_children_ball = section_children[section_number].getChildren("ball");
  String all_balls_check = section_children_ball[0].getString("number");
  String name = section_children[section_number].getString("name");
  print("Section name: ");
  print(name);
  println("");
  
  if ((all_balls_check.equals("all") )== true) {
    for (int j = 0; j<ball_numbers.length; j++) {
      int ball_number = ball_numbers[j];
      XML[] section_children_ball_effect = section_children_ball[0].getChildren("effect");
      select_effect_xml(section_children_ball_effect, ball_number);
    }
  } 
  else {
    for (int j = 0; j<section_children_ball.length; j++) {
      int ball_number = PApplet.parseInt(section_children_ball[j].getString("number"));
      XML[] section_children_ball_effect = section_children_ball[j].getChildren("effect");
      select_effect_xml(section_children_ball_effect, ball_number);
    }
  }
  
  if (xml_has_time == true && cbxXmlPlay.isSelected()==true) {
    if ((next_section+2)>section_children.length) {
      println("ROUTINE COMPLETE");  
      if (loop_xml == 1) {
        next_section = 0;
        next_section_time = 0;
        routine_start_time = millis();
      } 
      else {
        xml_flag=false;
      }
    } 
    else {

      next_section = next_section+1;
      next_section_time = PApplet.parseInt(section_children[next_section].getString("time"));
    }
  }
}



public void select_effect_xml(XML[] section_children_ball_effect, int ball_number) {

  if (ball_number == 0) { // ball_number=0 is used for photo and video effects only. The CANNOT be an actual ball with the number zero.
    String effect_type = section_children_ball_effect[0].getString("type"); // [0] because only one effect per ball
    if ((effect_type.equals("photo") )== true) { 
      photo();
    }
    if ((effect_type.equals("video") )== true) { 
      video();
    }
  } 
  
  else {
    ball_number = findIndexofArray(ball_number, ball_numbers); // Work with the actual index array
    String effect_type = section_children_ball_effect[0].getString("type"); // [0] because only one effect per ball
    effect_balls[ball_number] = effect_type;
    
    // Solid
    if ((effect_type.equals("solid") )== true) { 
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours = (trim(section_children_ball_effect_colour[0].getContent()));
      int[] colour_array = PApplet.parseInt(split(colours, ','));
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array[2], colour_array_balls[ball_number][2]), 0, 255);
      ball_effect_select(ball_number);
    } 
    

    if ((effect_type.equals("flash"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sdrDelay[ball_number].setLimits(PApplet.parseInt(delay_string), 5, 2000);
      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      int[] colour_array2 = PApplet.parseInt(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      ball_effect_select(ball_number);
    } 


    if ((effect_type.equals("fade") )== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      if (section_children_ball_effect[0].hasAttribute("cycle")==true) {
        String fade_cycle = section_children_ball_effect[0].getString("cycle");
        if ((fade_cycle.equals("true"))== true) {
          cbxRepeat[ball_number].setSelected(true);
        } 
        else {
          cbxRepeat[ball_number].setSelected(false);
        }
      } 
      else {
        cbxRepeat[ball_number].setSelected(false);
      }

      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      int[] colour_array2 = PApplet.parseInt(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      sdrDelay[ball_number].setLimits(PApplet.parseInt(delay_string), 5, 100);
      ball_effect_select(ball_number);
    } 

    if ((effect_type.equals("fade_impact"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      int[] colour_array2 = PApplet.parseInt(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      sdrDelay[ball_number].setLimits(PApplet.parseInt(delay_string), 1, 100);
      ball_effect_select(ball_number);
    }



    if ((effect_type.equals("meteor"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      int[] colour_array2 = PApplet.parseInt(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("meteor_random"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      ball_effect_select(ball_number);
    }

    if ((effect_type.equals("two_state"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      String colours2 = (trim(section_children_ball_effect_colour[1].getContent()));
      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      int[] colour_array2 = PApplet.parseInt(split(colours2, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      knbRed2[ball_number].setLimits(checkIfprevious(colour_array2[0], colour_array_balls[ball_number][3]), 0, 255);
      knbGreen2[ball_number].setLimits(checkIfprevious(colour_array2[1], colour_array_balls[ball_number][4]), 0, 255);
      knbBlue2[ball_number].setLimits(checkIfprevious(colour_array2[2], colour_array_balls[ball_number][5]), 0, 255);
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("two_state_random"))== true) {
      XML[] section_children_ball_effect_colour = section_children_ball_effect[0].getChildren("colour");
      String colours1 = (trim(section_children_ball_effect_colour[0].getContent()));
      int[] colour_array1 = PApplet.parseInt(split(colours1, ',')); 
      knbRed1[ball_number].setLimits(checkIfprevious(colour_array1[0], colour_array_balls[ball_number][0]), 0, 255);
      knbGreen1[ball_number].setLimits(checkIfprevious(colour_array1[1], colour_array_balls[ball_number][1]), 0, 255);
      knbBlue1[ball_number].setLimits(checkIfprevious(colour_array1[2], colour_array_balls[ball_number][2]), 0, 255);
      ball_effect_select(ball_number);
    }

    if ((effect_type.equals("throw_random"))== true) {
      ball_effect_select(ball_number);
    }



    if ((effect_type.equals("random_colours"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sdrDelay[ball_number].setLimits(PApplet.parseInt(delay_string), 50, 5000);
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("sync_random"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sync_random_delay =checkIfprevious(PApplet.parseInt(delay_string), sync_random_delay); 
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("sync_random_fade"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sync_fade_delay =checkIfprevious(PApplet.parseInt(delay_string), sync_fade_delay); 
      ball_effect_select(ball_number);
    }

    if ((effect_type.equals("sync_throw_random"))== true) {
      ball_effect_select(ball_number);
    }


    if ((effect_type.equals("random_fade"))== true) {
      XML[] section_children_ball_effect_delay = section_children_ball_effect[0].getChildren("delay");
      String delay_string = (trim(section_children_ball_effect_delay[0].getContent()));
      sdrDelay[ball_number].setLimits(PApplet.parseInt(delay_string), 5, 40);
      ball_effect_select(ball_number);
    }

    dplEffectSelect[ball_number].setItems(available_effects, findIndexofStringArray(effect_type, available_effects)); // Update the dropdown list to the correct effect for the ball
  }
}


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "v35" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

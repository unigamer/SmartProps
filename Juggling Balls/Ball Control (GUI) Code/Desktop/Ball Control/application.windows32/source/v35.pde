/* Ball Control - Version 1.0.0
 Written by Jonathan Jamieson (unigamer@gmail.com)
 www.jonathanjamieson.com
 
 This software is unsupported. You use it at your own risk. If you find any bugs then drop me an email but I can't spend much time helping you set up. Good luck!
 
 If you use this software for performances or anything then I would love to see a video. :)
 */


// Import the required libraries
import g4p_controls.*; // The GUI library used to create controls. It's free to download as is required to compile this software.
import processing.serial.*; // A serial connection is used to send commands to the microcontroller connected to the computer running Ball Control.
import ddf.minim.*; // Minim is used to play songs during routines.  It's free to download as is required to compile this software.

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


void setup() {
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



void draw() {

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

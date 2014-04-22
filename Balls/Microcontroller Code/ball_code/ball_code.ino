// Juggling Ball Code
// Jonathan Jamieson
// unigamer@gmail.com
// www.jonathanjamieson.com

/*
How to send a command to a juggling ball

An ASCII string of comma seperated values should be sent either through the bluetooth>serial module or the 
USB>Serial converter on the central node. There needs to be 14 numbes terminated by a newline "/n". An
example command that would set ball number 1 to red is: 1,1,1,1,255,0,0,0,0,0,0,0,0,0

The structure is:

"1:Central Node Command Type, 2:Ball Number, 3:Ball Command Type, 4:Effect Number, 5:Red 1, 6:Green 1, 7:Blue 1, 8:Red 2, 9:Green 2, 10:Blue 2, 11:Fade Delay, 12:Send State, 13:Unused, 14: Unused"

1. Central Node Command Type = 1
2. Ball Number, currently 1,2 or 3. Alternatively, 0 can be useed to send an effect to all balls
3. Ball Command Type = 1
4. Effect Number, see below for a description of the different effects that can be used and what number to use.
5. Red 1
6. Green 1
7. Blue 1
8. Red 2
9. Green 2
10. Blue 2
11. Fade delay (measured in milliseconds)
12. Send state (0 = don't send data if ball has changed state, 1 = send data if ball changes state). Generally best to leave this = 1.
13. Currently unused but is transmitted to the ball
14. Currently unused but is transmistted to the ball



Effect Numbers
1. Solid Colour (R1, G1, B1)
2. Fade (R1 -> R2, G1 -> G2, B1 -> B2, total time = 255*Fade_Delay)
3. Fade Impact (Currently not working)
4. Fade Cycle (same as #2 Fade but goes back and forth betweem colours)
5. Flash (R1 || R2, G1 || G2, B1 || B2, Fade Delay specifies the period)
6. Send Sensor Data
7. Update Parameters
8. Disco Mode (Fade Delay specifies the period)
9. Two State (Hand: R1,G1,B1 or Air: R2,G2,B2)
10. Throw Random (no parameters needed)
11. Random Fade (Time between colours = 255*Fade_Delay)
12. Sync Throw Random
13. Two State Random
14. Sync Throw All
15. 

For another example, to fade from green to blue on ball number 2 with a a fade delay of 10ms: 1,2,1,2,0,255,0,0,0,255,10,1,0,0

*/

// Required Libraries
#include <math.h>
#include <EEPROM.h>
#include <RF24Network.h>
#include <RF24.h>
#include <SPI.h>

int ball_type; // The initial prototypes used two different kind of Pro Mini's slightly different pin layouts. This code can handle both types by setting up the pins correctly.
int random_colour_array[3];
int previous_colour_at_zero_array = 0;
// Define Pin Numbers
// Accelerometer Pins
int xpin;             
int ypin;                 
int zpin;
int accel_ground;

int command_number = 0;
boolean send_state = true;

// Set up the RF24 Communication
RF24 radio(9,10); 
RF24Network network(radio);
const uint16_t host_node = 0; // The transceiver connected to the computer is the central node. All balls are connected to it.

// Led Pins
// A common cathode tri-colour LED is required (or three individual appropraitely coloured LEDs.
const int led_r_pin = 3; // Red Pin
const int led_g_pin = 5; // Green Pin
const int led_b_pin = 6; // Blue Pin

// Payload Structure
struct payload_t
{
  int p1;
  int p2;
  int p3;
  int p4;
  int p5;
  int p6;
  int p7;
  int p8;
  int p9;
  int p10;
//  int p11;
//  int p12;
};

payload_t payload_rx;
payload_t payload_tx;

// This filter is used to smooth the magnitude of the acceleration output
#define filterSamples   17  // Filter Samples should  be an odd number, no smaller than 3
int sensSmoothArray1[filterSamples];  
int smoothmag;  

int x; // Acceleration in the x-direction
int y; // Acceleration in the y-direction
int z; // Acceleration in the z-direction

double magnitude_accelerometer;

// These values are used to scale the acceleration that is read from the accelerometer. This is required because the voltage varies as the Lipo drains down.
int xMin;
int yMin;
int zMin;
int xMax;
int yMax;
int zMax;

int ball_number; // The number of this ball. Must be unique and between 1-5 inclusive.

int colour_array[2][3];

int state_ball = 0; // 0=hand, 1=air
boolean new_command = true;

unsigned long time;
unsigned long time_2;

int fade_counter = 0; 
int fade_delay = 5; // (ms)
int fade_decrement = 1;
int effect_impact = 35;

int effect_direction = 1; //1== forwards, 0 = backwards

unsigned long impact_time;
int impact_minimum_time = 320;
int impact_minimum_mag = 15;
int impact_average_mag = 9;  // currently not using this average crap

int flag_ball=0;
boolean flag_change_state = 0;

int effect_number = 1;
void setup(void)
{

  // Get Data from EEPROM. Each ball is a "node" with a unique number. The central receiver (node 0) is connected to a computer and is not actually a juggling ball.
  // The EEPROM data is set using, ball_write_eeprom.pde.
  ball_number = EEPROM.read(0);
  effect_number = EEPROM.read(1); // Which effect to start off with. This should be 1 (Solid Colour) under normal circumstances.
  colour_array[0][0] = EEPROM.read(2); // Red PWM to start with
  colour_array[0][1] = EEPROM.read(3); // Green PWM to start with
  colour_array[0][2] = EEPROM.read(4); // Blue PWM to start with

  // Determine which Arduino type to use
  ball_type = EEPROM.read(5);

  // Get the scaling values of the acceleration readings. The multiplication by 4 is because only 0-225 can be sent so it's divided by 4 before being transmitted to the ball and then reconstructed here.
  xMin = EEPROM.read(6)*4; 
  yMin = EEPROM.read(7)*4;
  zMin = EEPROM.read(8)*4;
  xMax = EEPROM.read(9)*4;
  yMax = EEPROM.read(10)*4;
  zMax = EEPROM.read(11)*4;

  // Derek Lang Style
  if (ball_type==1) {
    xpin = A1;
    ypin = A2;
    zpin = A3;
    Serial.print("Ball type 1");

    // Jianlin's style
  } 
  else if (ball_type==2) {
    xpin = A0;
    ypin = A1;
    zpin = A2;
    Serial.print("Ball type 2");
    pinMode(A3, OUTPUT);
    digitalWrite(A3, LOW); 
  }

  // Set Accelerometer pins as inputs
  pinMode(xpin, INPUT);
  pinMode(ypin, INPUT);
  pinMode(zpin, INPUT);

  // Start communications with RF24
  SPI.begin();
  radio.begin();
  network.begin(90,ball_number);
  radio.setRetries(15,1);

  // Transmit ball details through serial
  Serial.begin(57600);

  Serial.print("This is juggling ball number: ");
  Serial.print(ball_number);
  Serial.println("");

  Serial.println(xMin);
  Serial.println(yMin);
  Serial.println(zMin);
  Serial.println(xMax);
  Serial.println(yMax);
  Serial.println(zMax);
}

void loop(void)
{
  check_for_new_command();
  check_ball_state();
  process_effect();
}











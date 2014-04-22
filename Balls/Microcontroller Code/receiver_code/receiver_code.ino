// Juggling Ball Central Node Code
// Jonathan Jamieson
// unigamer@gmail.com

// www.jonathanjamieson.com

/* 
 This is the code for the central node is attached to the computer and/or connected to a bluetooth device
 such as your phone. The central node contains the following hardware: Arduino (I use a Pro Mini + USB>Serial
 adapter, NRF24L01 (2.4Ghz Transceiver), Bluetooth Serial Module and an IR LED to trigger a camera (optional).
 
 The Bluetooth adapter is attached to Pins 2 & 3 for me using the Software Serial library. However, any suitable
 unused digital pins can be used. I have the Software Serial running at the same baud rate as the actual serial
 port (57600) but you can change this if desired. Remember that your Bluetooth Serial Module will need to be
 configured to run at the same speed as the software serial (default speed is 9600 on most modules). The RX pin
 on the Bluetooth should be connected to Pin 2 and Bluetooth TX should be Pin 3.
 
 The wiring for the NRF24L01 is as follows (courtesy of http://arduino-info.wikispaces.com/Nrf24L01-2.4GHz-HowTo):
 
 RF Module      Arduino Pin
 GND (1)        GND
 VCC (2)        3.3v (I used the USB>Serial 3.3v because Pro Mini's don't have an on board regulator)
 CE (3)         9
 CSN (4)        10
 SCK (5)        13
 MOSI (6)       11
 MISO (7)       12
 IRQ(8)         UNUSED
 
 
 I used the more expensive nRF24L01 + Antenna for this central node because I have found it significantly improves
 the range (http://cgi.ebay.co.uk/ws/eBayISAPI.dll?ViewItem&item=131128076671&ssPageName=ADME:L:OC:GB:3160).
 However, you can use the cheaper <$2 module if you're on a budget or need to save space. I use the cheap modules
 in the balls and haven't had any problems.
 
 */

// Required Libraries
#include <RF24Network.h>
#include <RF24.h>
#include <SPI.h>
#include <SoftwareSerial.h>
#include <multiCameraIrControl.h>

Canon D5(4); // Annode of IR LED on Pin 4
RF24 radio(9,10);
RF24Network network(radio);
SoftwareSerial mySerial(3, 2); // RX, TX

const uint16_t this_node = 0; // Address of our node

int ball_number; // Address of the other node
int ball_numbers[] = {
  1,2,3}; // An array of the ball numbers that can be available (However, they don't NEED to be available)
const int num_balls = 3; // This should be the number of elements in the ball_numbers array

// Stuff for received serial commands
const int NUMBER_OF_FIELDS = 14; // how many comma separated fields we expect
int fieldIndex = 0;            // the current field being received
int values[NUMBER_OF_FIELDS];   // array holding values for all the fields

int balls_effects[num_balls]; // A ball is currently in this mode then it's element should =1, else it should =0
int sync_colour_1[3];
int sync_colour_2[3];
int previous_colour_at_zero_array = 0;
int random_colour_array[3];

unsigned long sync_throw_time;
int sync_throw_tolerance = 150;
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
  // int p11;
  // int p12;
};

payload_t payload_rx;
payload_t payload_tx;

void setup(void)
{
  Serial.begin(57600);
  SPI.begin();
  radio.begin();
  network.begin(/*channel*/ 90, /*node address*/ this_node);

  pinMode(A3, OUTPUT); // I used A3 as the GND for my IR LED
  digitalWrite(A3, LOW); 

  mySerial.begin(57600); // MAKE SURE YOUR BLUETOOTH SERIAL MODULE IS CONFIGURED TO WHATEVER SPEED IS HERE!
  mySerial.println("Unleash the power of your smart balls!");
}

void loop(void)
{
  // Pump the network regularly
  network.update();

  while ( network.available() ) // Check network to see if the balls have anything to say
  {
    // If so, grab it and print it out
    RF24NetworkHeader header;
    payload_t payload;
    network.read(header,&payload,sizeof(payload)); 

    payload_rx = payload;
    Serial.print("1"); // type of command
    Serial.print(","); 
    Serial.print(payload_rx.p1); 
    Serial.print(","); 
    Serial.print(payload_rx.p2); 
    Serial.print(",");
    Serial.print(payload_rx.p3); 
    Serial.print(",");
    Serial.print(payload_rx.p4);
    Serial.println("");

    if (balls_effects[payload.p1-1]==12 && payload.p3==1) { // If the ball has just been thrown and it's effect type is Sync Throw Random then we need to send it a new colour
    
      process_sync_throw();
    }
    
    
    
   if (balls_effects[payload.p1-1]==14 && payload.p3==1) { // If the ball has just been thrown and it's effect type is Throw Sync (All) then we need to a new colour to all the balls
      process_sync_throw_all();
    }
    
    
  }

  check_serial(); // Check to see if any serial command is available from either the Bluetooth or USB
}


void serial_command_to_ball() {
  if (values[1] == 0) { // send command to all balls if ball number is zero
    for (int destination_node = 0; destination_node < num_balls; destination_node++)  {
      send_payload(ball_numbers[destination_node]);   


    }
  } 
  else {

    // send command to ball_number values[1]
    send_payload(values[1]);
  }

}


void send_payload(int destination_node) {



  //  Serial.println("");
  ball_number = destination_node;
  payload_tx.p1 = values[2]; // Type of command
  payload_tx.p2 = values[3]; // Effect type if type of command = 1
  payload_tx.p3 = values[4];
  payload_tx.p4 = values[5];
  payload_tx.p5 = values[6];
  payload_tx.p6 = values[7];
  payload_tx.p7= values[8];
  payload_tx.p8 = values[9];
  payload_tx.p9 = values[10];
  payload_tx.p10 = values[11];
  //    payload_tx.p11 = values[12];
  //     payload_tx.p12 = values[13];
  payload_t payload;
  payload = payload_tx;
  RF24NetworkHeader header(ball_number);
  bool ok = network.write(header,&payload,sizeof(payload));

  balls_effects[ball_number-1] = values[3];

} 




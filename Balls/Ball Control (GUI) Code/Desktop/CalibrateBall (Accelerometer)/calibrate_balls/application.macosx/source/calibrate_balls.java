import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class calibrate_balls extends PApplet {




Serial myPort; 

GButton btnsendData ;
int ball_number = 9;

int accel_x, accel_y, accel_z;
int[] accel_values = new int[6];


GLabel[] lblAccelLimits  = new GLabel[6];
GLabel[] lblAccelLabels  = new GLabel[6];
GLabel[] lblAccelValues = new GLabel[3];

GLabel lblBallNumber;
GLabel lblBallNumberText;

int numSensors = 5;  // we will be expecting for reading data from four sensors
int sensors[];       // array to read the 4 values

int new_command = 1;

public void setup() {
  
  btnsendData  =  new GButton(this, 3, 3, 100, 18, "Send Data");
btnsendData.tag = "sendData";

  
  lblBallNumber = new GLabel(this, 150, 3, 60, 18, "Label");
     lblBallNumber.setText(str(ball_number));
     
       lblBallNumberText = new GLabel(this, 110, 3, 60, 18, "Label");
     lblBallNumberText.setText("Ball:");
  
  
          for (int i = 0; i < lblAccelLimits.length; i++) {
    lblAccelLimits[i] = new GLabel(this, 70, 30 + i*30, 60, 18, "Label");
    lblAccelLimits[i].tag = "AccelLimits";
    lblAccelLimits[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblAccelLimits[i].tagNo = i;
    lblAccelLimits[i].setText("Waiting");
  } 
  
            for (int i = 0; i < lblAccelValues.length; i++) {
    lblAccelValues[i] = new GLabel(this, 120, 30 + i*30, 60, 18, "Label");
    lblAccelValues[i].tag = "AccelValues";
    lblAccelValues[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblAccelValues[i].tagNo = i;
    lblAccelValues[i].setText("Waiting");
  } 
  
  
  String[] lblString = {"Min x", "Min y", "Min z", "Max x", "Max y", "Max z"};
  
  
          for (int i = 0; i < lblAccelLabels.length; i++) {
    lblAccelLabels[i] = new GLabel(this, 10, 30 + i*30, 60, 18, "Label");
    lblAccelLabels[i].tag = "AccelLimitsNames";
    lblAccelLabels[i].setLocalColorScheme(G4P.BLUE_SCHEME);
    lblAccelLabels[i].tagNo = i;
    lblAccelLabels[i].setText(lblString[i]);
  } 
  
  accel_values[0] = 1000;
accel_values[1] = 1000;
accel_values[2] = 1000;

  size(200, 200);

  myPort = new Serial(this, Serial.list()[0], 57600);
  // read bytes into a buffer until you get a linefeed (ASCII 10):
  myPort.bufferUntil(10);
  delay(1000);
  myPort.write("MEOW");
  myPort.write(10);
  delay(2000);
  
  int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 6; 
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
  payload_data[12] = 0;

  send_payload(payload_data);
  
} 

public void draw() {
  background(240);

 

}


public void send_payload(int[] payload_data) {

  // payload_data - 0:ball number (not actually sent to ball), 1:p1 (case type), 2:p2 , 3:p3, 4:p4 ... 12:p12

  myPort.write("1,"+payload_data[0]+","+payload_data[1]+","+payload_data[2]+","+payload_data[3]+","+payload_data[4]+","+payload_data[5]+","+payload_data[6]+","+payload_data[7]+","+payload_data[8]+","+payload_data[9]+","+payload_data[10]+","+payload_data[11]+","+payload_data[12]); 
  myPort.write(10);
  delay(2);
  //  myPort.write(payload[0]","+payload[1]+","+payload[2]+","+payload[3]+","+payload[4]+","+payload[5]+","+payload[6]+","+payload[7]+","+payload[8]+","+payload[9]+","+payload[10]+","+payload[11]+","+payload[12]+","+0); 
  //  myPort.write(10);
}

public void handleButtonEvents(GButton button, GEvent event) {

  // Change the colour scheme
  if ( event == GEvent.CLICKED) {
    //  println(button.tagNo);
    //  println(button.tag);

    if (button.tag.equals("sendData") == true) {

    

        int[] payload_data = new int[13];

        payload_data[0] = ball_number;
        payload_data[1] = 7; 
        payload_data[2] = accel_values[0];
        payload_data[3] = accel_values[1];
        payload_data[4] = accel_values[2];
        payload_data[5] = accel_values[3];
        payload_data[6] = accel_values[4];
        payload_data[7] = accel_values[5];
        payload_data[8] = 0;
        payload_data[9] = 0;
        payload_data[10] = 0;
        payload_data[11] = 0;
        payload_data[12] = 0;


        send_payload(payload_data);
    
    }
  }
}
public void keyPressed() {
  
  //if (int(key)==true) {
    
    if (key>48 && key<55) {
      
      
       int[] payload_data = new int[13];

  payload_data[0] = ball_number;
  payload_data[1] = 1; 
  payload_data[2] = 10;
  payload_data[3] = 10;
  payload_data[4] = 10;
  payload_data[5] = 0;
  payload_data[6] = 0;
  payload_data[7] = 0;
  payload_data[8] = 0;
  payload_data[9] = 0;
  payload_data[10] = 0;
  payload_data[11] = 0;
  payload_data[12] = 0;


  send_payload(payload_data);
  
  
  
 ball_number=key-48;
 println(ball_number);
   lblBallNumber.setText(str(ball_number));
   
     accel_values[0] = 1000;
accel_values[1] = 1000;
accel_values[2] = 1000;

  accel_values[3] = 0;
accel_values[4] = 0;
accel_values[5] = 0;

lblAccelLimits[0].setText("Waiting");
lblAccelLimits[1].setText("Waiting");
lblAccelLimits[2].setText("Waiting");
lblAccelLimits[3].setText("Waiting");
lblAccelLimits[4].setText("Waiting");
lblAccelLimits[5].setText("Waiting");

     lblAccelValues[0].setText("Waiting");
     lblAccelValues[1].setText("Waiting");
     lblAccelValues[2].setText("Waiting");
    
//1}

 

  payload_data[0] = ball_number;
  payload_data[1] = 6; 
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
  payload_data[12] = 0;


  send_payload(payload_data);
  }

}

public void serialEvent(Serial myPort) {

  // read the serial buffer:
  String myString = myPort.readStringUntil(10);

  // if you got any bytes other than the linefeed:
  if (myString != null) {

    myString = trim(myString);

    // split the string at the commas
    // and convert the sections into integers:

    sensors = PApplet.parseInt(split(myString, ','));


 



    accel_x = sensors[1];
    accel_y = sensors[2];
    accel_z = sensors[3];
    
    
     lblAccelValues[0].setText(str(accel_x));
     lblAccelValues[1].setText(str(accel_y));
     lblAccelValues[2].setText(str(accel_z));
     









    if (accel_x<accel_values[0] && accel_x>10) {

      accel_values[0] = accel_x;
      lblAccelLimits[0].setText(str(accel_values[0]));
 
    }

    if (accel_y<accel_values[1]&& accel_y>10) {

      accel_values[1] = accel_y;
      lblAccelLimits[1].setText(str(accel_values[1]));
 
    }


    if (accel_z<accel_values[2]&& accel_z>10) {

      accel_values[2] = accel_z;
         lblAccelLimits[2].setText(str(accel_values[2]));
 
    }


    if (accel_x>accel_values[3]) {

      accel_values[3] = accel_x;
          lblAccelLimits[3].setText(str(accel_values[3]));
    }



    if (accel_y>accel_values[4]) {

      accel_values[4] = accel_y;
       lblAccelLimits[4].setText(str(accel_values[4]));

    }



    if (accel_z>accel_values[5]) {

      accel_values[5] = accel_z;
            lblAccelLimits[5].setText(str(accel_values[5]));

    }
    
    
    println("x: "+accel_x+"  y: "+accel_y+"   z: "+accel_z+"   xMin: "+accel_values[0]+"   yMin: "+accel_values[1]+"   zMin: "+accel_values[2]+"   xMax: "+accel_values[3]+"   yMax: "+accel_values[4]+"   zMax: "+accel_values[5]);
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "calibrate_balls" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

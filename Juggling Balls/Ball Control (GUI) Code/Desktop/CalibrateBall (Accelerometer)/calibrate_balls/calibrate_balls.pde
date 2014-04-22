import g4p_controls.*;
import processing.serial.*;

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

void setup() {
  
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

  send_payload(ball_number,payload_data);
  
} 

void draw() {
  background(240);

 

}




void send_payload(int ball_number, int[] payload_data) {
  // case type = 1 for effect commands
  // payload_data - 0:p1 (case type), 1:ball number (not actually sent to ball),  2:p2 , 3:p3, 4:p4 ... 12:p12
  myPort.write("1,"+ball_number+","+payload_data[0]+","+payload_data[1]+","+payload_data[2]+","+payload_data[3]+","+payload_data[4]+","+payload_data[5]+","+payload_data[6]+","+payload_data[7]+","+payload_data[8]+","+payload_data[9]+","+payload_data[10]+","+payload_data[11]); 
  myPort.write(10);
  delay(5);
}


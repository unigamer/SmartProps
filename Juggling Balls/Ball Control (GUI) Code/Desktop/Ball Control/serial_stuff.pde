void serialEvent(Serial myPort) { // Currently this function is used to change the state_balls array to reflect whether or not a ball is in the air or not.
  String myString = myPort.readStringUntil(10); // This function is a bit of a mess and needs to be tidied up. It was written with the intention to be flexible to different kinds of serial events, not just ball changes.
  if (myString != null) {
    myString = trim(myString);
    sensors = int(split(myString, ','));
    int ball_index = findIndexofArray(sensors[1], ball_numbers);
   
  // int ball_index = sensors[1] - 1;
      if (sensors[1] > 0) {
        if (sensors[0] == 1) {
          serial_flag_balls[ball_index] = 1;
          if (sensors[3]==1) { // throw so ball in air
            state_balls[ball_index] = 1; // air
             print("Ball Number IN AIR: ");
             print(ball_index);
               println("");
             
          } 
          else {
            state_balls[ball_index] = 0; // hand
          }
        }
      }
  }
}

void send_payload(int ball_number, int[] payload_data) {
  // case type = 1 for effect commands
  // payload_data - 0:p1 (case type), 1:ball number (not actually sent to ball),  2:p2 , 3:p3, 4:p4 ... 12:p12
  myPort.write("1,"+ball_number+","+payload_data[0]+","+payload_data[1]+","+payload_data[2]+","+payload_data[3]+","+payload_data[4]+","+payload_data[5]+","+payload_data[6]+","+payload_data[7]+","+payload_data[8]+","+payload_data[9]+","+payload_data[10]+","+payload_data[11]); 
  myPort.write(10);
  delay(5);
}

void photo() {
  // case type = 0 for photo and video commands (or anything that is only to be performed on the receiver node
  myPort.write("0,1,0,0,0,0,0,0,0,0,0,0,0,0");
  myPort.write(10);
  delay(5);
}

void video() {
  myPort.write("0,2,0,0,0,0,0,0,0,0,0,0,0,0");
  myPort.write(10);
  delay(5);
}


// "1,1,1,0,255,0,0,0,0,0,0,0,0,0"

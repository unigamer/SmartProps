void keyPressed() {
  
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

void check_ball_state() {

  // Accelerometer Stuff. This is all about detecting if the ball is in your hand on in the air.
  // Check Accelerometer
  x = analogRead(xpin); 
  delay(1); 
  y = analogRead(ypin); 
  delay(1); 
  z = analogRead(zpin);

  // Scale Acceleromater Readings. Use calibration program to set xMin, xMax etc
  int xAng = map(x, xMin, xMax, -90, 90);
  int yAng = map(y, yMin, yMax, -90, 90);
  int zAng = map(z, zMin, zMax, -90, 90);

  magnitude_accelerometer = sqrt(pow(xAng,2)+pow(yAng,2)+pow(zAng,2)); // Determine the magnitude of the acceleration
  smoothmag = digitalSmooth(magnitude_accelerometer, sensSmoothArray1); // Filter the magnitude because it's a noisy signal

  // If ball state is set as in hand, see if now in air
  if (smoothmag<40 && flag_ball==0 && state_ball==0) {
    impact_time = millis() +60;
    flag_ball= 1;
    //Serial.println("Suspect Ball now in air");
  }

  // If ball state is set as in air, see if now in hand
  if (smoothmag>40 && flag_ball==0 && state_ball==1) {
    impact_time = millis() +60;
    flag_ball= 1;
    //Serial.println("Suspect Ball now in hand");
  }

  // If 60ms has past and it's still recorded as a change in state then accept that it really has
  if (millis()>impact_time && flag_ball==1) {
    // Serial.println("Checking ball state because time check reached");

    // Check if ball now in AIR
    if (state_ball==0 && smoothmag<40) {

      command_number = command_number+1;

      //  Serial.println("This ball is definitely in the air!!!!!");
      state_ball=1;
      flag_ball=0;

      if(send_state) {
        payload_tx.p1 = ball_number;
        payload_tx.p2 = magnitude_accelerometer;
        payload_tx.p3 = 1; // in air
        payload_tx.p4 = command_number;
        payload_t payload;
        payload = payload_tx;
        RF24NetworkHeader header(host_node);
        bool ok = network.write(header,&payload,sizeof(payload));
      }
      flag_change_state = true;
    } 
    else {
      // 60ms has past and it seems the ball has not actually changed state so it's a false positive
      flag_ball = 0;
    }

    // Check if ball now in HAND
    if (state_ball==1 && smoothmag>40) {

      command_number = command_number+1;

      // Serial.println("This ball is definitely in the hand!!!!!");
      state_ball=0;
      flag_ball=0;

     if(send_state) {
      payload_tx.p1 = ball_number;
      payload_tx.p2 = magnitude_accelerometer;
      payload_tx.p3 = 0; // in air
      payload_tx.p4 = command_number;
      payload_t payload;
      payload = payload_tx;

      RF24NetworkHeader header(host_node);
      bool ok = network.write(header,&payload,sizeof(payload));
      flag_change_state = true;
     }
    } 
    else {
      // 60ms has past and it seems the ball has not actually changed state so it's a false positive
      flag_ball = 0;
    }
  }

}


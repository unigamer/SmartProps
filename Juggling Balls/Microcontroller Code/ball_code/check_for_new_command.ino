void check_for_new_command() {
  // CHECK FOR NEW COMMANDS
  // Check if there is any payload for this ball
  network.update(); // Refresh the network
  while ( network.available() )
  {
    // If so, grab it and print it out
    RF24NetworkHeader header;
    payload_t payload;
    network.read(header,&payload,sizeof(payload));
    payload_rx = payload;
    // Serial.print("Command Received, Type:");
    //   Serial.print(payload_rx.p1);
    //  Serial.print("\n"); 
    new_command = true; // Tell the effect code that a new effect or different parameters to an effect have been received
    
    if (payload_rx.p1 == 1) {
    effect_number = payload_rx.p2;
    colour_array[0][0] =  payload_rx.p3;
    colour_array[0][1]  = payload_rx.p4;
    colour_array[0][2]  = payload_rx.p5; 
    colour_array[1][0] =  payload_rx.p6;
    colour_array[1][1]  = payload_rx.p7;
    colour_array[1][2]  = payload_rx.p8; 
    fade_delay = payload_rx.p9;
    send_state = int2bool(payload_rx.p10);
    
    }
  }
}


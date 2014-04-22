// The following commands are called by process_effect() depending on what payload_rx.p1 is specified as

void update_parameters_1() {
  // update paramters
  if (new_command==true) {
    new_command=false;
    impact_minimum_time = payload_rx.p2;
    impact_minimum_mag = payload_rx.p3;
    impact_average_mag = payload_rx.p4;  
  }
}


void effect_solid(int colour_row) {

  if (new_command==true) {
    new_command=false;

  }

  analogWrite(led_r_pin, colour_array[colour_row][0]);  
  analogWrite(led_g_pin, colour_array[colour_row][1]); 
  analogWrite(led_b_pin, colour_array[colour_row][2]);  

}


void effect_fade() {

  if (new_command==true) {    
    fade_counter=0;
    new_command = false;
  }
  if ( ( millis() - time ) > fade_delay ) {
    time = millis();
    fade_counter =  constrain((fade_counter + fade_decrement),0,255);

    int colour_out_r =  constrain(map(fade_counter,0,255,colour_array[0][0],colour_array[1][0]),0,255);
    int colour_out_g =  constrain(map(fade_counter,0,255,colour_array[0][1],colour_array[1][1]),0,255);
    int colour_out_b =  constrain(map(fade_counter,0,255,colour_array[0][2],colour_array[1][2]),0,255);

    analogWrite(led_r_pin,  colour_out_r );  
    analogWrite(led_g_pin, colour_out_g ); 
    analogWrite(led_b_pin,  colour_out_b );  
  }

}


void effect_fade_impact() {

  if (new_command==true) {    
    fade_counter=0;
    new_command = false;
    effect_impact = payload_rx.p10;
  }
  if (magnitude_accelerometer>effect_impact) {
    fade_counter = 255+fade_decrement;
  }
  if ( ( millis() - time ) > fade_delay ) {
    time = millis();
    fade_counter =  constrain((fade_counter - fade_decrement),0,255);
    int colour_out_r =  constrain(map(fade_counter,0,255,colour_array[0][0],colour_array[1][0]),0,255);
    int colour_out_g =  constrain(map(fade_counter,0,255,colour_array[0][1],colour_array[1][1]),0,255);
    int colour_out_b =  constrain(map(fade_counter,0,255,colour_array[0][2],colour_array[1][2]),0,255);
    analogWrite(led_r_pin,  colour_out_r );  
    analogWrite(led_g_pin, colour_out_g ); 
    analogWrite(led_b_pin,  colour_out_b );  
  }

}

void effect_cycle_fade() {

  if (new_command==true) {    
    new_command = false;
    effect_direction = 1;
  }

  if ( ( millis() - time ) > fade_delay ) {
    time = millis();
    if (effect_direction == 1) {
      fade_counter =  constrain((fade_counter + fade_decrement),0,255);
      if (fade_counter == 255) {
        effect_direction = 0;
      }
    } 
    else {
      fade_counter =  constrain((fade_counter - fade_decrement),0,255);
      if (fade_counter == 0) {

        effect_direction = 1;
      }
    }
    int colour_out_r =  constrain(map(fade_counter,0,255,colour_array[0][0],colour_array[1][0]),0,255);
    int colour_out_g =  constrain(map(fade_counter,0,255,colour_array[0][1],colour_array[1][1]),0,255);
    int colour_out_b =  constrain(map(fade_counter,0,255,colour_array[0][2],colour_array[1][2]),0,255);
    analogWrite(led_r_pin,  colour_out_r );  
    analogWrite(led_g_pin, colour_out_g ); 
    analogWrite(led_b_pin,  colour_out_b );  
  }

}


void effect_flash_balls()  {
  if (new_command==true) {    
    fade_counter=0;
    new_command = false;
    effect_direction = 1;
  }

  if ( ( millis() - time ) > fade_delay ) {
    time = millis();

    if (effect_direction == 1) {
      analogWrite(led_r_pin,  colour_array[0][0]);  
      analogWrite(led_g_pin, colour_array[0][1] ); 
      analogWrite(led_b_pin,  colour_array[0][2]); 
      effect_direction = 0;
    } 
    else {
      analogWrite(led_r_pin, colour_array[1][0]);  
      analogWrite(led_g_pin, colour_array[1][1] ); 
      analogWrite(led_b_pin,  colour_array[1][2]); 
      effect_direction = 1;
    }
  }
}




void effect_two_state() {

  if (new_command==true) {
    new_command=false;
    if (state_ball == 0) { // ball has just been caught
      effect_solid(0);
    } 
    else if (state_ball == 1) { // ball has just been thrown
      effect_solid(1);
    }
  }

  if (flag_change_state) {
    flag_change_state = false;
    if (state_ball == 0) { // ball has just been caught
      effect_solid(0);
    } 
    else if (state_ball == 1) { // ball has just been thrown
      effect_solid(1);
    }
  }
}


void effect_throw_random() {
  if (flag_change_state && state_ball==1) {
    flag_change_state = false;

    randomColourArrayV2();
    
    colour_array[0][0] = random_colour_array[0];
    colour_array[0][1] = random_colour_array[1];
    colour_array[0][2] = random_colour_array[2];
    
    analogWrite(led_r_pin, colour_array[0][0] );  
    analogWrite(led_g_pin,  colour_array[0][1]); 
    analogWrite(led_b_pin,   colour_array[0][2] ); 
    
  }
}


void effect_two_state_random() {
  if (flag_change_state && state_ball==1) {
    flag_change_state = false;

    randomColourArrayV2();
    

    analogWrite(led_r_pin, random_colour_array[0] );  
    analogWrite(led_g_pin,  random_colour_array[1]); 
    analogWrite(led_b_pin,   random_colour_array[2] ); 
    
  }
  
    if (flag_change_state && state_ball==0) {
    flag_change_state = false;

    analogWrite(led_r_pin, colour_array[0][0] );  
    analogWrite(led_g_pin,  colour_array[0][1]); 
    analogWrite(led_b_pin,   colour_array[0][2] ); 
    
  }
}


void effect_random_fade() {
  
 if (new_command==true) {    
    // new_command = false;
    
    randomColourArrayV2();
    
    colour_array[1][0] = random_colour_array[0];
    colour_array[1][1] = random_colour_array[1];
    colour_array[1][2] = random_colour_array[2];
    
    time_2 = millis() + 255*fade_delay;
    
    
  }
  
  if (millis()>time_2) {
    
    randomColourArrayV2();
    
    colour_array[0][0] = colour_array[1][0];
    colour_array[0][1] = colour_array[1][1];
    colour_array[0][2] = colour_array[1][2];
    
    colour_array[1][0] = random_colour_array[0];
    colour_array[1][1] = random_colour_array[1];
    colour_array[1][2] = random_colour_array[2];
    
    time_2 = millis() + 255*fade_delay;
    new_command=true;
  }
  
  effect_fade();
  

}





void update_parameters_2()
{
  if (new_command==true) { 
    int v1 = payload_rx.p3/4;
    int v2 = payload_rx.p4/4;
    int v3 = payload_rx.p5/4;
    int v4 = payload_rx.p6/4;
    int v5 = payload_rx.p7/4;
    int v6 = payload_rx.p8/4;
    EEPROM.write(6, v1); // xMin
    EEPROM.write(7, v2); // yMin
    EEPROM.write(8, v3); // zMin
    EEPROM.write(9, v4); // xMax
    EEPROM.write(10, v5); // yMax
    EEPROM.write(11, v6); // zMax  
    xMin = payload_rx.p3;
    yMin = payload_rx.p4;
    zMin = payload_rx.p5;
    xMax = payload_rx.p6;
    yMax = payload_rx.p7;
    zMax = payload_rx.p8; 
    // Set ball colour to white
    effect_number = 1;
    colour_array[0][0] = 255;
    colour_array[0][1] = 255;
    colour_array[0][2] = 255;
  }
}


void effect_disco_mode()   {
  if (new_command==true) {    
    new_command = false;
  }

  if ( ( millis() - time ) > fade_delay ) {
    time = millis();
 
    randomColourArrayV2();
    analogWrite(led_r_pin,  random_colour_array[0] );  
    analogWrite(led_g_pin,  random_colour_array[1] ); 
    analogWrite(led_b_pin,   random_colour_array[2] ); 
  }
}


void send_sensor_data() {
  flag_ball = 0; // prevent any commands from being sent to serial except the analog readings
  payload_tx.p1 = x;
  payload_tx.p2 = y;
  payload_tx.p3 = z; 
  payload_tx.p4 = ball_number;
  payload_t payload;
  payload = payload_tx;
  RF24NetworkHeader header(host_node);
  bool ok = network.write(header,&payload,sizeof(payload));
  delay(200);
}




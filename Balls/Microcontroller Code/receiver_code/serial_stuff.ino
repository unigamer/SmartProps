void check_serial() {

  if( Serial.available()) // The USB>Serial Port
  {
    char ch = Serial.read();
    if(ch >= '0' && ch <= '9') // is this an ascii digit between 0 and 9?
    {
      // yes, accumulate the value if the fieldIndex is within range
      // additional fields are not stored
      if(fieldIndex < NUMBER_OF_FIELDS) {
        values[fieldIndex] = (values[fieldIndex] * 10) + (ch - '0'); 
      }
    }
    else if (ch == ',')  // comma is our separator, so move on to the next field
    {
      fieldIndex++;   // increment field index 
    }
    else
    {

      switch (values[0]) { // Receiver Command Type

      case 0:
        if (values[1] == 1) {
          photo();
        }
        if (values[1] == 2) {
          video();
        }
        break;

      case 1: // values[0] is receiver command type, 1 = new effect for ball, 0 = take photo

        if (values[3] != 12 && values[3] !=14) { // Effect 12 is "Sync Throw Random", this is not actually send to the ball but dealt with on the receiver so we don't want to send it to the ball
          serial_command_to_ball(); // Send new effect to the ball(s)
        } 
        else {
          if (values[1] == 0) { // send command to all balls if ball number is zero
            for (int ball_number = 0; ball_number < num_balls; ball_number++)  {
              balls_effects[ball_number] = values[3]; 
            }
          } 
          else {
            // send command to ball_number values[1]
            balls_effects[values[1]-1] = values[3]; 
          }

        }  
        break;
      }

      for(int i=0; i < min(NUMBER_OF_FIELDS, fieldIndex+1); i++)
      {
        values[i] = 0; // set the values to zero, ready for the next message
      }
      fieldIndex = 0;  // ready to start over
    }
  }





  if( mySerial.available()) // Bluetooth Serial Port
  {
    char ch = mySerial.read();
    if(ch >= '0' && ch <= '9') // is this an ascii digit between 0 and 9?
    {
      // yes, accumulate the value if the fieldIndex is within range
      // additional fields are not stored
      if(fieldIndex < NUMBER_OF_FIELDS) {
        values[fieldIndex] = (values[fieldIndex] * 10) + (ch - '0'); 
      }
    }
    else if (ch == ',')  // comma is our separator, so move on to the next field
    {
      fieldIndex++;   // increment field index 
    }
    else
    {
      if (values[0] == 1) {

        if (values[3] != 12 && values[3] !=14) {
          serial_command_to_ball();

        } 
        else {

          if (values[1] == 0) { // send command to all balls if ball number is zero
            for (int ball_number = 0; ball_number < num_balls; ball_number++)  {
              balls_effects[ball_number] = values[3]; 
            }
          } 
          else {
            // send command to ball_number values[1]
            balls_effects[values[1]-1] = values[3]; 
          }

        }

      } 

      else if (values[0] == 0) {
        if (values[1] == 1) {
          photo();
        }
        if (values[1] == 2) {
          video();
        }
      }
      for(int i=0; i < min(NUMBER_OF_FIELDS, fieldIndex+1); i++)
      {
        values[i] = 0; // set the values to zero, ready for the next message
      }
      fieldIndex = 0;  // ready to start over
    }
  }

}








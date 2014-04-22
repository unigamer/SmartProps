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

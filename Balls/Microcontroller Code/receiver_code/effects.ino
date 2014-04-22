void process_sync_throw() {
  
   if (millis()-sync_throw_time<sync_throw_tolerance) { // Another ball has been throw recently (<sync_throw_tolerance) so the ball that has just been thrown needs to be the same colour

        ball_number = payload_rx.p1;
        payload_tx.p1 = 1; // Type of command
        payload_tx.p2 = 1; // Effect type if type of command = 1
        payload_tx.p3 = sync_colour_1[0];
        payload_tx.p4 = sync_colour_1[1];
        payload_tx.p5 = sync_colour_1[2];
        payload_tx.p6 = 0;
        payload_tx.p7= 0;
        payload_tx.p8 = 0;
        payload_tx.p9 = 0;
        payload_tx.p10 = 1;
        //    payload_tx.p11 = values[12];
        //     payload_tx.p12 = values[13];
        payload_t payload;
        payload = payload_tx;
        RF24NetworkHeader header(ball_number);
        bool ok = network.write(header,&payload,sizeof(payload));
      } 
      else { // No other ball has been thrown recently so generate a new random colour

        // Generate a new colour (populate random_colour_array)
        randomColourArrayV2_no_repeat();

        sync_colour_1[0] = random_colour_array[0];
        sync_colour_1[1] = random_colour_array[1];
        sync_colour_1[2] = random_colour_array[2];

        ball_number = payload_tx.p1;
        payload_tx.p1 = 1; // Type of command
        payload_tx.p2 = 1; // Effect type if type of command = 1
        payload_tx.p3 = sync_colour_1[0];
        payload_tx.p4 = sync_colour_1[1];
        payload_tx.p5 = sync_colour_1[2];
        payload_tx.p6 = 0;
        payload_tx.p7= 0;
        payload_tx.p8 = 0;
        payload_tx.p9 = 0;
        payload_tx.p10 = 1;
        //    payload_tx.p11 = values[12];
        //     payload_tx.p12 = values[13];
        payload_t payload;
        payload = payload_tx;
        RF24NetworkHeader header(ball_number);
        bool ok = network.write(header,&payload,sizeof(payload));   

        sync_throw_time = millis();  
      }
  
}



void process_sync_throw_all() {
  
   if (millis()-sync_throw_time>sync_throw_tolerance) { // Another ball has been throw recently (<sync_throw_tolerance) so don't send out a new colour



        // Generate a new colour (populate random_colour_array)
        randomColourArrayV2_no_repeat();

        sync_colour_1[0] = random_colour_array[0];
        sync_colour_1[1] = random_colour_array[1];
        sync_colour_1[2] = random_colour_array[2];

       
        payload_tx.p1 = 1; // Type of command
        payload_tx.p2 = 1; // Effect type if type of command = 1
        payload_tx.p3 = sync_colour_1[0];
        payload_tx.p4 = sync_colour_1[1];
        payload_tx.p5 = sync_colour_1[2];
        payload_tx.p6 = 0;
        payload_tx.p7= 0;
        payload_tx.p8 = 0;
        payload_tx.p9 = 0;
        payload_tx.p10 = 1;
        //    payload_tx.p11 = values[12];
        //     payload_tx.p12 = values[13];
        
        
       for (int destination_node = 1; destination_node < num_balls+1; destination_node++)  {
     
        ball_number = destination_node; 
        payload_t payload;
        payload = payload_tx;
        RF24NetworkHeader header(ball_number);
        bool ok = network.write(header,&payload,sizeof(payload));  
        
        
        }
 

        sync_throw_time = millis();  
      }
  
}
